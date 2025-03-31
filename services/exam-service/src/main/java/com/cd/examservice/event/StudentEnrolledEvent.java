package com.cd.examservice.event;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrolledEvent {
    private Long courseId;
    private Long studentId;
}

// __typeId__

