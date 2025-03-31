package com.cd.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Long instructorId;

    @ElementCollection
    @CollectionTable(name="course_students",joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "student_id")
    private List<Long> enrolledStudentIds;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="course_scores",joinColumns = @JoinColumn(name = "course_id"))
    @MapKeyColumn(name = "student_id")
    @Column(name = "score")
    private Map<Long,Integer> studentScores;

    
}
