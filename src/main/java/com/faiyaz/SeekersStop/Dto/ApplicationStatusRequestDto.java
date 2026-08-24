package com.faiyaz.SeekersStop.Dto;

import com.faiyaz.SeekersStop.Enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationStatusRequestDto {

    @NotNull(message = "Status is required")
    private ApplicationStatus applicationStatus;

}
