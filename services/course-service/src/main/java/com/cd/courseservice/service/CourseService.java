package com.cd.courseservice.service;

import com.cd.courseservice.client.UserClient;
import com.cd.courseservice.dto.CourseRequestDTO;
import com.cd.courseservice.dto.UserResponseDTO;
import com.cd.courseservice.entity.Course;
import com.cd.courseservice.event.common.StudentEnrolledEvent;
import com.cd.courseservice.event.producer.StudentEnrolledEventPublisher;
import com.cd.courseservice.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor

public class CourseService {

    private final CourseRepository repository;
    private final UserClient userClient;
    private final StudentEnrolledEventPublisher eventPublisher;


    public Course createCourse(CourseRequestDTO courseRequestDTO) {

        ResponseEntity<UserResponseDTO> user = userClient.getUser(courseRequestDTO.getInstructorId());

        if(!Objects.requireNonNull(user.getBody()).getRole().equals("INSTRUCTOR")){
            throw new IllegalArgumentException("User with id : " + courseRequestDTO.getInstructorId() + " is not an instructor");
        }

        Course course = Course.builder()
                .description(courseRequestDTO.getDescription())
                .title(courseRequestDTO.getTitle())
                .instructorId(courseRequestDTO.getInstructorId())
                .build();

        return repository.save(course);
    }

    public Course getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }


    public List<Course> getAll() {
        return repository.findAll();
    }

    public Course enrollStudent(Long courseId, Long studentId) {
        Course course = repository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        int countStudentEnrollment = repository.countStudentEnrollment(courseId, studentId);
        if(countStudentEnrollment > 0){
            throw new IllegalArgumentException("Student with id : " + studentId + " is already enrolled in the course");
        }

        if(course.getEnrolledStudentIds().contains(studentId)){
            throw new IllegalArgumentException("Student with id : " + studentId + " is already enrolled in the course");
        }

        ResponseEntity<UserResponseDTO> user = userClient.getUser(studentId);
        if(!Objects.requireNonNull(user.getBody()).getRole().equals("STUDENT")){
            throw new IllegalArgumentException("User with id : " + studentId + " is not a student");
        }
        course.getEnrolledStudentIds().add(studentId);

        eventPublisher.publishStudentEnrolledEvent(new StudentEnrolledEvent(courseId, studentId));
        return repository.save(course);
    }


    @Transactional
    public void updateScore(Long studentId, Long courseId, int score) {
        Course course = repository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        if(course.getStudentScores() == null){
                course.setStudentScores(new HashMap<>());
            }
        course.getStudentScores().put(studentId,score);
        repository.save(course);
    }

    public Map<?, ?> getStudentScore(Long courseId, Long studentId) {
        int score = repository.getStudentScore(courseId, studentId).orElseThrow(() -> new RuntimeException("Student or course not found with id: " + courseId + " " + studentId + " \n CourseService -> getStudentScore"));
        Map<String, ? extends Number> map = Map.of("id", studentId, "score", score);
        return Map.of("data" ,map );
    }
}
