package com.cd.courseservice.event.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentExamCreatedEvent {
    private Long courseId;
    private Long studentId;
    private String examId;
}
