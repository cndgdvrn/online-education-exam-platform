package com.cd.examservice.service;
import com.cd.examservice.dto.ExamSubmittedEvent;
import com.cd.examservice.entity.Exam;
import com.cd.examservice.entity.ExamOutboxMessage;
import com.cd.examservice.entity.Question;
import com.cd.examservice.repository.ExamRepository;
import com.cd.examservice.repository.OutboxRepository;
import com.cd.examservice.utils.QuestionGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService {

    private final ObjectMapper objectMapper;
    private final ExamRepository examRepository;
    private final OutboxRepository outboxRepository;

    public void createExamForStudent(Long courseId, Long studentId){
        log.info("Exam Service is called");
        log.info("Exam Service is called");
        log.info("Exam Service is called");
        log.info("Exam Service is called");
        log.info("Creating exam for student {} for course {}", studentId, courseId);
        log.info("Exam Service is called");
        log.info("Exam Service is called");
        log.info("Exam Service is called");
        log.info("Exam Service is called");

        boolean b = examRepository.findByCourseIdAndStudentId(courseId, studentId).isPresent();
        if(b){
            return;
        }

        Exam exam = Exam.builder()
                .courseId(courseId)
                .studentId(studentId)
                .submitted(false)
                .score(0)
                .questions(QuestionGenerator.generateQuestions())
                .build();

        examRepository.save(exam);
    }

    @Transactional
    public int submitExam(String examId, List<Integer> answers) {
        String examPayloadJSON = "";
        Exam exam = getExamOrThrow(examId);
        validateExamSubmission(exam);
        validateAnswers(exam, answers);
        int score = calculateScore(exam.getQuestions(), answers);
        exam.setScore(score);
        exam.setSubmitted(true);
        examRepository.save(exam);

        ExamSubmittedEvent examSubmittedEvent = ExamSubmittedEvent.builder()
                .examId(exam.getId())
                .courseId(exam.getCourseId())
                .studentId(exam.getStudentId())
                .score(score)
                .submitted(exam.isSubmitted())
                .build();

        try {
             examPayloadJSON= objectMapper.writeValueAsString(examSubmittedEvent);
        }catch (Exception e){
            log.error("Error while sending exam submitted event", e);
        }

        ExamOutboxMessage examOutboxMessage = ExamOutboxMessage.builder()
                .eventType("EXAM_SUBMITTED")
                .payload(examPayloadJSON.isEmpty() ? null : examPayloadJSON)
                .processed(false)
                .createdAt(Instant.now())
                .build();
        outboxRepository.save(examOutboxMessage);
        return score;
    }

    private Exam getExamOrThrow(String examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam with ID " + examId + " not found"));
    }

    private void validateExamSubmission(Exam exam) {
        if (exam.isSubmitted()) {
            throw new IllegalStateException("Exam has already been submitted");
        }
    }

    private void validateAnswers(Exam exam, List<Integer> answers) {
        if (answers == null || exam.getQuestions().size() < answers.size()) {
            throw new IllegalArgumentException("Invalid number of answers provided");
        }
    }

    private int calculateScore(List<Question> questions, List<Integer> answers) {
        final int POINT_PER_CORRECT_ANSWER = 5;
        int score = 0;
        for (int i = 0; i < answers.size(); i++) {
            if (questions.get(i).getCorrectOptionIndex() == answers.get(i)) {
                score += POINT_PER_CORRECT_ANSWER;
            }
        }
        return score;
    }

    public List<Exam> getExams() {
        return examRepository.findAll();
    }
}
