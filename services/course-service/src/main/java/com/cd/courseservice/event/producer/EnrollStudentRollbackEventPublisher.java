package com.cd.courseservice.event.producer;

import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;

public interface EnrollStudentRollbackEventPublisher {
    void publishEnrollStudentRollbackEvent(EnrollStudentRollbackEvent event);
}
