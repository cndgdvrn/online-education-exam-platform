package com.cd.examservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamSubmittedEvent {
    private String examId;
    private Long studentId;
    private Long courseId;
    private int score;
    private boolean submitted;
}
//com.cd.examservice.dto.ExamSubmittedEvent.java