package com.cd.courseservice.event.producer;

import com.cd.courseservice.event.common.StudentEnrolledEvent;

public interface StudentEnrolledEventPublisher {
    void publishStudentEnrolledEvent(StudentEnrolledEvent event);
}
