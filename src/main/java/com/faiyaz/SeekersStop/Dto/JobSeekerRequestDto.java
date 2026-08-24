package com.faiyaz.SeekersStop.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JobSeekerRequestDto {

    @NotBlank(message = "Skill is required")
    private String skill;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Cv is required")
    private String cv;
    @NotBlank(message = "Experience is required")
    private String experience;
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit contact number"
    )
    @NotBlank(message = "Contact cannot be empty")
    private String contact;
}
