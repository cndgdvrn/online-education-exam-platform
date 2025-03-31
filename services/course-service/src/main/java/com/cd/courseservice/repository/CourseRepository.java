package com.cd.courseservice.repository;

import com.cd.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {


    @Query(value = """
        SELECT COUNT(*)
        FROM course_students
        WHERE course_id = :courseId
        AND student_id = :studentId
        """, nativeQuery = true)
    int countStudentEnrollment(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Query(value = """
        SELECT score
        FROM course_scores
        WHERE course_id = :courseId
        AND student_id = :studentId
""", nativeQuery = true)
    Optional<Integer> getStudentScore(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
}
