package com.cd.examservice.event.consumer;

import com.cd.examservice.service.ExamService;
import com.cd.examservice.event.StudentEnrolledEvent;
import com.cd.examservice.event.StudentEnrolledEventConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StudentEnrolledEventConsumerImpl implements StudentEnrolledEventConsumer {

    private final ExamService examService;

    @Override
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consumeStudentEnrolledEvent(StudentEnrolledEvent event) {
        log.info("Received student enrolled event: studentid ->  {} -- courseid -> {} ", event.getStudentId(), event.getCourseId());
        //TODO: create exam for student
        examService.createExamForStudent(event.getCourseId(), event.getStudentId());
    }
}
