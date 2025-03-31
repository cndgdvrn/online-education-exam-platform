package com.cd.examservice.event;



public interface StudentEnrolledEventConsumer {
    void consumeStudentEnrolledEvent(StudentEnrolledEvent event);

}
