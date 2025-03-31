package com.cd.examservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitExamRequestDTO {
    private List<Integer> answers;
}
