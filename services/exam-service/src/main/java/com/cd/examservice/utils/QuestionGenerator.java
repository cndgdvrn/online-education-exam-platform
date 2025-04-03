package com.cd.examservice.utils;

import com.cd.examservice.entity.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class QuestionGenerator {
    private static ObjectMapper objectMapper;

    public QuestionGenerator(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    public static List<Question> generateQuestions() {
        try {
            return objectMapper.readValue(
                    QuestionGenerator.class.getClassLoader().getResourceAsStream("questions.json"),
                    new TypeReference<List<Question>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("questions.json okunamadı: " + e.getMessage(), e);
        }
    }
}