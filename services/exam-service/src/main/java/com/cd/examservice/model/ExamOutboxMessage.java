package com.cd.examservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "exam_outbox_messages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamOutboxMessage {
    @Id
    private String id;
    private String eventType;
    private String payload;
    private boolean processed;
    private Instant createdAt;
}
