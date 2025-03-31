package com.cd.courseservice.dto;

public record CourseResponseDTO (
        Long id,
        Long instructor,
        String title,
        String description

){
}
