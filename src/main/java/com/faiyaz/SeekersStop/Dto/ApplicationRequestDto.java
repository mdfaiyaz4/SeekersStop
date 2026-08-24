package com.faiyaz.SeekersStop.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ApplicationRequestDto {
    @NotNull(message = "Job ID is required")
    @Positive(message = "Job ID must be greater than 0")
    private Long jobId;
}
