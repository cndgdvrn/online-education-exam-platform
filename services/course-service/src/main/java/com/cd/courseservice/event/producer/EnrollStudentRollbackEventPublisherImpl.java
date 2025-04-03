package com.cd.courseservice.event.producer;

import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollStudentRollbackEventPublisherImpl implements EnrollStudentRollbackEventPublisher{

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.rollback-routing-key}")
    private String routingKey;

    @Override
    public void publishEnrollStudentRollbackEvent(EnrollStudentRollbackEvent event) {
      log.warn("Publishing EnrollStudentRollbackEvent to exchange: {}, routingKey: {}, payload: {}", exchange, routingKey, event);
      try {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
      }catch (Exception e){
          log.error("Failed to publish event to RabbitMQ", e);
          throw new RuntimeException("RabbitMQ publish failed", e);
      }


    }
}
