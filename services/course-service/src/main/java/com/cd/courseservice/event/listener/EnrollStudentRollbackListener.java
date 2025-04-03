package com.cd.courseservice.event.listener;

import com.cd.courseservice.event.common.EnrollStudentRollbackEvent;

public interface EnrollStudentRollbackListener {
    void handleEnrollStudentRollbackEvent(EnrollStudentRollbackEvent event);
}
