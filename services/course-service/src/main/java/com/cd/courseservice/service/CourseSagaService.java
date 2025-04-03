package com.cd.courseservice.service;

import com.cd.courseservice.client.UserClient;
import com.cd.courseservice.dto.UserResponseDTO;
import com.cd.courseservice.entity.Course;
import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;
import com.cd.courseservice.event.common.EnrollStudentSagaStartedEvent;
import com.cd.courseservice.event.common.StudentEnrolledEvent;
import com.cd.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
@Slf4j
public class CourseSagaService {

    private final UserClient userClient;
    private final CourseRepository courseRepository;
    private final OutboxService outboxService;
    private final CourseRollbackService courseRollbackService;

    @Transactional
    public void processEnrollStudentSaga(EnrollStudentSagaStartedEvent event) {
        try {
            validateStudent(event.getStudentId());
            Course course = getCourseOrThrow(event.getCourseId());
            checkStudentAlreadyEnrolled(course, event.getStudentId());

            enrollStudent(course, event.getStudentId());

            StudentEnrolledEvent enrolledEvent = StudentEnrolledEvent.builder()
                    .studentId(event.getStudentId())
                    .courseId(event.getCourseId())
                    .build();

            outboxService.saveOutbox("ENROLL_SUCCESS", enrolledEvent);

            log.info(" Student enrolled successfully | courseId={}, studentId={}",
                    event.getCourseId(), event.getStudentId());

        } catch (Exception e) {
            log.error(" Enrollment failed: {}", e.getMessage(), e);
            courseRollbackService.saveRollBackEvent(event, e.getMessage());
            throw new RuntimeException("Enrollment failed, rollback event created", e);
        }
    }

    /**
     * Validates if the user is a student by checking their role.
     *
     * @param studentId the ID of the student to validate
     * @throws IllegalArgumentException if the user is not a student or not found
     */
    private void validateStudent(Long studentId) {
        ResponseEntity<UserResponseDTO> response = userClient.getUser(studentId);
        UserResponseDTO user = response.getBody();

        if (user == null || !"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException("User is not a student or user not found (id=" + studentId + ")");
        }
    }

    /**
     * Retrieves a course by its ID or throws an exception if not found.
     *
     * @param courseId the ID of the course to retrieve
     * @return the Course object
     * @throws RuntimeException if the course is not found
     */
    private Course getCourseOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }

    /**
     * Checks if the student is already enrolled in the course.
     *
     * @param course    the Course object
     * @param studentId the ID of the student to check
     * @throws IllegalArgumentException if the student is already enrolled
     */
    private void checkStudentAlreadyEnrolled(Course course, Long studentId) {
        boolean alreadyEnrolled = courseRepository.countStudentEnrollment(course.getId(), studentId) > 0;
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Student already enrolled | courseId=" + course.getId() + ", studentId=" + studentId);
        }
    }

    /**
     * Enrolls a student in the course.
     *
     * @param course    the Course object
     * @param studentId the ID of the student to enroll
     */
    private void enrollStudent(Course course, Long studentId) {
        course.getEnrolledStudentIds().add(studentId);
        courseRepository.save(course);
    }





}