package com.cd.courseservice.event.producer;


import com.cd.courseservice.event.common.StudentEnrolledEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentEnrolledEventPublisherImpl implements StudentEnrolledEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingKey;


    @Override
    public void publishStudentEnrolledEvent(StudentEnrolledEvent event) {
        log.info("Publishing StudentEnrolledEvent to exchange: {}, routingKey: {}, payload: {}", exchange, routingKey, event);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.info(" publishStudentEnrolledEvent published successfully");
        } catch (Exception e) {
            log.error("Failed to publish event to RabbitMQ", e);
            throw new RuntimeException("RabbitMQ publish failed", e);
        }
    }
}
