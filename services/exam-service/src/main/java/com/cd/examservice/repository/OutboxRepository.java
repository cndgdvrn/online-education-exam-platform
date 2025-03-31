package com.cd.examservice.repository;

import com.cd.examservice.model.ExamOutboxMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OutboxRepository extends MongoRepository<ExamOutboxMessage, String> {

    @Query(value = "{'processed': false}")
    List<ExamOutboxMessage> findAllUnprocessedMessages();

    List<ExamOutboxMessage> findTop20ByProcessedFalseOrderByCreatedAtAsc();


}
