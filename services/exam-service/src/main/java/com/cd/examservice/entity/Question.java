package com.cd.examservice.entity;


import lombok.*;



import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
}





