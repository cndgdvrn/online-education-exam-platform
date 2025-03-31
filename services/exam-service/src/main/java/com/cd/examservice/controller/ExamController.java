package com.cd.examservice.controller;

import com.cd.examservice.service.ExamService;
import com.cd.examservice.dto.SubmitExamRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/exams")
@RestController
@AllArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/{examId}/submit")
    public ResponseEntity<Map<Object,Object>> submitExam(@PathVariable String examId, @RequestBody SubmitExamRequestDTO submitExamRequestDTO){
        int score = examService.submitExam(examId, submitExamRequestDTO.getAnswers());
        return ResponseEntity.status(200).body(Map.of("score", score));
    }

    @GetMapping
    public ResponseEntity<Map<Object,Object>> getExams(){
        return ResponseEntity.status(200).body(Map.of("exams", examService.getExams()));
    }
}
