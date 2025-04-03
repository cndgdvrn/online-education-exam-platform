package com.cd.courseservice.saga;


import com.cd.courseservice.event.common.EnrollStudentSagaStartedEvent;
import com.cd.courseservice.service.CourseSagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;




@Component
@RequiredArgsConstructor
@Slf4j
public class CourseSagaCoordinator {

    private final CourseSagaService courseSagaService;

    @RabbitListener(queues = "${rabbitmq.enroll-started.queue}")
    public void handleStudentEnrolledEvent(EnrollStudentSagaStartedEvent sagaStartedEvent) {
        log.info("📥 Starting Saga for courseId={}, studentId={}", sagaStartedEvent.getCourseId(), sagaStartedEvent.getStudentId());
        courseSagaService.processEnrollStudentSaga(sagaStartedEvent);
    }
}
