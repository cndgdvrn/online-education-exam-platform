package com.cd.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "course_outbox_messages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseOutboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;
    private boolean processed;
    private Instant createdAt;


}


