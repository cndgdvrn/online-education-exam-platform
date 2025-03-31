package com.cd.courseservice.event.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollStudentRollbackEvent {
    public Long courseId;
    public Long studentId;
    public String reason;
}
