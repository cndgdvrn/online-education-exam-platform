package com.cd.examservice.repository;

import com.cd.examservice.entity.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ExamRepository extends MongoRepository<Exam,String> {
    Optional<Exam> findByCourseIdAndStudentId(Long courseId, Long studentId);
}
