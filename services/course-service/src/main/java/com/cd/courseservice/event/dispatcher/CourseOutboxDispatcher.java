package com.cd.courseservice.event.dispatcher;

import com.cd.courseservice.entity.CourseOutboxMessage;
import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;
import com.cd.courseservice.event.common.StudentEnrolledEvent;
import com.cd.courseservice.event.producer.EnrollStudentRollbackEventPublisher;
import com.cd.courseservice.event.producer.StudentEnrolledEventPublisher;
import com.cd.courseservice.repository.CourseOutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseOutboxDispatcher {

    private final CourseOutboxRepository courseOutboxRepository;
    private final ObjectMapper objectMapper;
    private final StudentEnrolledEventPublisher studentEnrolledEventPublisher;
    private final EnrollStudentRollbackEventPublisher enrollStudentRollbackEventPublisher;

    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    public void dispatchPendingEvents() {
        List<CourseOutboxMessage> messages = courseOutboxRepository.findTop20ByProcessedFalseOrderByCreatedAtAsc();
        messages.forEach(this::processMessageSafely);
    }

    /**
     * Processes a CourseOutboxMessage safely, handling any exceptions that may occur.
     *
     * @param message The CourseOutboxMessage to process.
     */
    private void processMessageSafely(CourseOutboxMessage message) {
        try {
            if (message.getPayload() == null || message.getPayload().trim().isEmpty()) {
                log.warn("Skipping message with empty payload. ID={}", message.getId());
                return;
            }
            processEvent(message);
            markAsProcessed(message);
        } catch (Exception e) {
            log.error(" Failed to process message ID={} | Error: {}", message.getId(), e.getMessage(), e);
        }
    }

    /**
     * Processes the event based on the event type in the CourseOutboxMessage.
     *
     * @param message The CourseOutboxMessage containing the event data.
     * @throws Exception if an error occurs during processing.
     */
    private void processEvent(CourseOutboxMessage message) throws Exception {
        String payload = message.getPayload();

        switch (message.getEventType()) {
            case "ENROLL_SUCCESS" -> {
                StudentEnrolledEvent event = objectMapper.readValue(payload, StudentEnrolledEvent.class);
                studentEnrolledEventPublisher.publishStudentEnrolledEvent(event);
                log.info("ENROLL_SUCCESS event published for studentId={}, courseId={}", event.getStudentId(), event.getCourseId());
            }
            case "ENROLL_FAIL" -> {
                EnrollStudentRollbackEvent rollbackEvent = objectMapper.readValue(payload, EnrollStudentRollbackEvent.class);
                enrollStudentRollbackEventPublisher.publishEnrollStudentRollbackEvent(rollbackEvent);
                log.info(" ENROLL_FAIL rollback event published for studentId={}, courseId={}", rollbackEvent.getStudentId(), rollbackEvent.getCourseId());
            }
            default -> log.warn(" Unknown event type: {} for message ID={}", message.getEventType(), message.getId());
        }
    }

    /**
     * Marks the CourseOutboxMessage as processed.
     *
     * @param message The CourseOutboxMessage to mark as processed.
     */
    private void markAsProcessed(CourseOutboxMessage message) {
        message.setProcessed(true);
        courseOutboxRepository.save(message);
    }
}
