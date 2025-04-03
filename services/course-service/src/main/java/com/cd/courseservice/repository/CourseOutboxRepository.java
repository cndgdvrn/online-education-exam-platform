package com.cd.courseservice.repository;

import com.cd.courseservice.entity.CourseOutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseOutboxRepository extends JpaRepository<CourseOutboxMessage, Long> {
    List<CourseOutboxMessage> findTop20ByProcessedFalseOrderByCreatedAtAsc();
}
