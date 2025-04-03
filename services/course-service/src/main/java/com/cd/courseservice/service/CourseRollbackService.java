package com.cd.courseservice.service;

import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;
import com.cd.courseservice.event.common.EnrollStudentSagaStartedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseRollbackService {

    private final OutboxService outboxService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRollBackEvent(EnrollStudentSagaStartedEvent originalEvent, String reason) {
        EnrollStudentRollbackEvent rollbackEvent = EnrollStudentRollbackEvent.builder()
                .studentId(originalEvent.getStudentId())
                .courseId(originalEvent.getCourseId())
                .reason(reason)
                .build();

        outboxService.saveOutbox("ENROLL_FAIL", rollbackEvent);
        log.warn("Rollback event saved to outbox | courseId={}, studentId={}",
                originalEvent.getCourseId(), originalEvent.getStudentId());
    }
}
