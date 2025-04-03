package com.cd.courseservice.service;

import com.cd.courseservice.entity.CourseOutboxMessage;
import com.cd.courseservice.repository.CourseOutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j

public class OutboxService {
    private final CourseOutboxRepository courseOutboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Saves an outbox message to the database.
     *
     * @param eventType    the type of the event
     * @param eventPayload the payload of the event
     * @throws RuntimeException if saving the outbox message fails
     */
    public void saveOutbox(String eventType, Object eventPayload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(eventPayload);
            CourseOutboxMessage message = CourseOutboxMessage.builder()
                    .eventType(eventType)
                    .payload(payloadJson)
                    .processed(false)
                    .createdAt(Instant.now())
                    .build();
            courseOutboxRepository.save(message);
        } catch (Exception e) {
            log.error("Failed to write to outbox: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write to outbox", e);
        }
    }
}
