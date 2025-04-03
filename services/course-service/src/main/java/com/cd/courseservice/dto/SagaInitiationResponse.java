package com.cd.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SagaInitiationResponse {
    private String status;
    private String message;
    private Long studentId;
    private Long courseId;
    private SagaStep saga;
    private Instant timestamp;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class SagaStep {
        private String step;
        private String nextExpectedStep;
    }
}
