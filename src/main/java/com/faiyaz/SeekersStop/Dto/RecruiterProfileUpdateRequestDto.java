package com.faiyaz.SeekersStop.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import javax.management.remote.JMXServerErrorException;

@Data
public class RecruiterProfileUpdateRequestDto {
    @NotNull(message = "Name is required")
    private String name;
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit contact number"
    )
    @NotBlank(message = "Contact cannot be empty")
    private String contact;
}
