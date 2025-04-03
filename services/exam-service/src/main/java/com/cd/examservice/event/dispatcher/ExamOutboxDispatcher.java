package com.cd.examservice.event.dispatcher;

import com.cd.examservice.entity.ExamOutboxMessage;
import com.cd.examservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExamOutboxDispatcher {

    private final RabbitTemplate rabbitTemplate;
    private final OutboxRepository outboxRepository;

    @Value("${rabbitmq.exam-submitted.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    // fixedRate = 15000 means that the method will be executed every 15 seconds
    @Scheduled(fixedRate = 15000 , initialDelay = 5000)
    public void dispatchPendingEvents(){
        List<ExamOutboxMessage> examOutboxMessages = outboxRepository.findTop20ByProcessedFalseOrderByCreatedAtAsc();
        examOutboxMessages.forEach(examOutboxMessage -> {

            try {
                String stringPayload = examOutboxMessage.getPayload();
                rabbitTemplate.convertAndSend(this.exchange, this.routingKey, stringPayload);
                examOutboxMessage.setProcessed(true);
                outboxRepository.save(examOutboxMessage);
                log.info("Sent outbox event: {}", stringPayload);
            } catch (Exception e) {
                log.error("Error while processing outbox message", e);
            }
        });
    }
}
