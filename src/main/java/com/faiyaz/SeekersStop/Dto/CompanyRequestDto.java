package com.faiyaz.SeekersStop.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompanyRequestDto {
    @NotBlank(message = "Company name is required")
    private String name;

    @NotBlank(message = "Company description is required")
    private String description;

    @NotBlank(message = "Company location is required")
    private String location;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit contact number"
    )
    @NotBlank(message = "Contact cannot be empty")
    private String contact;

    @NotBlank(message = "Company website is required")
    private String website;
}
