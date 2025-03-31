package com.cd.examservice.model;


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





