package com.cd.examservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "exams")
@CompoundIndex(name = "unique_exam_per_student_course", def = "{'studentId': 1, 'courseId': 1}", unique = true)
public class Exam {

    @MongoId
    private String id;

    private Long courseId;

    private Long studentId;

    @JsonIgnore
    private List<Question> questions;

    private boolean submitted;

    private int score;
}
