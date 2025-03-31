package com.cd.courseservice.event.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrolledEvent {
    private Long courseId;
    private Long studentId;
}
