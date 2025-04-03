package com.cd.courseservice.controller;

import com.cd.courseservice.dto.CourseRequestDTO;
import com.cd.courseservice.dto.CourseResponseDTO;
import com.cd.courseservice.dto.SagaInitiationResponse;
import com.cd.courseservice.entity.Course;

import com.cd.courseservice.event.common.EnrollStudentSagaStartedEvent;
import com.cd.courseservice.service.CourseSagaService;
import com.cd.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseSagaService courseSagaService;


    @GetMapping("/status/check")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Course service is working");
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO dto) {
        Course course = courseService.createCourse(dto);
        CourseResponseDTO responseDTO = new CourseResponseDTO(
                course.getId(),
                course.getInstructorId(),
                course.getTitle(),
                course.getDescription()
        );
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Course> enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {

        Course course = courseService.enrollStudent(courseId, studentId);
        return ResponseEntity.status(201).body(course);
    }

    @GetMapping("/{courseId}/scores")
    public ResponseEntity<Map<?,?>> getScores(@PathVariable Long courseId){
        Map<Long, Integer> studentScores = courseService.getById(courseId).getStudentScores();
        return ResponseEntity.ok(studentScores);
    }

    @GetMapping("/{courseId}/scores/{studentId}")
    public ResponseEntity<Map<?,?>> getScore(@PathVariable Long courseId, @PathVariable Long studentId){
        Map<?, ?> score = courseService.getStudentScore(courseId, studentId);
        return ResponseEntity.status(200).body(score);
    }

    @PostMapping("/{courseId}/saga/enroll/{studentId}")
    public ResponseEntity<?> startEnrollmentSaga(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseSagaService.processEnrollStudentSaga(new EnrollStudentSagaStartedEvent(courseId, studentId));
        SagaInitiationResponse response = SagaInitiationResponse.builder()
                .status("EVENT_PUBLISHED")
                .studentId(studentId)
                .courseId(courseId)
                .message("EnrollStudentSagaStartedEvent published successfully.")
                .timestamp(Instant.now())
                .saga(SagaInitiationResponse.SagaStep.builder()
                        .step("INITIATED")
                        .nextExpectedStep("CourseService -> Validate Student Role")
                        .build())
                .build();

        return ResponseEntity.status(202).body(response);
    }

}
