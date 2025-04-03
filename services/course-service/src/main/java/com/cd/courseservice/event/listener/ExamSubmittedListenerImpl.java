package com.cd.courseservice.event.listener;

import com.cd.courseservice.dto.ExamSubmittedEvent;
import com.cd.courseservice.entity.Course;
import com.cd.courseservice.repository.CourseRepository;
import com.cd.courseservice.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ExamSubmittedListenerImpl implements ExamSubmittedListener{

    private final ObjectMapper mapper;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Override
    @RabbitListener(queues = "${rabbitmq.exam-submitted.queue}")
    public void handleExamSubmitted(String payload) {
        try {
            ExamSubmittedEvent event = mapper.readValue(payload, ExamSubmittedEvent.class);
            Course course = courseRepository.findById(event.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
            courseService.updateScore(event.getStudentId(), event.getCourseId(), event.getScore());
            log.info("Updated score for student {} in course {}", event.getStudentId(), event.getCourseId());
        }catch (Exception e){
            log.error("Error parsing exam submitted event", e);
        }
    }
}
