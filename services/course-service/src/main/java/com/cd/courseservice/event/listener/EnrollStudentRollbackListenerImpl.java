package com.cd.courseservice.event.listener;

import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnrollStudentRollbackListenerImpl implements EnrollStudentRollbackListener {

    @Override
    @RabbitListener(queues = "${rabbitmq.rollback-queue}")
    public void handleEnrollStudentRollbackEvent(EnrollStudentRollbackEvent event) {
        log.warn("Received EnrollStudentRollbackEvent: {}", event);
        //TODO Implement the rollback logic here
    }
}
