package com.faiyaz.SeekersStop.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobRequestDto {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Job experience is required")
    private String experience;

    @NotBlank(message = "Job qualification is required")
    private String qualification;

    @NotNull(message = "Job salary is required")
    @Positive(message = "Salary must be greater than 0")
    private Double salary;

    @NotBlank(message = "Job location is required")
    private String location;

    @FutureOrPresent(message = "Job deadline cannot be past")
    @NotNull(message = "Job deadline cannot be empty")
    private LocalDate deadline;
}
