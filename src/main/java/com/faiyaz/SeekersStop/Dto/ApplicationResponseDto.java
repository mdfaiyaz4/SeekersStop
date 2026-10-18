package com.faiyaz.SeekersStop.Dto;

import java.time.LocalDateTime;

import com.faiyaz.SeekersStop.Enums.ApplicationStatus;

import lombok.Data;

@Data
public class ApplicationResponseDto {
    private Long applicationId;
    private LocalDateTime appliedAt;
    private String jobSeeker;
    private ApplicationStatus applicationStatus;
    private String jobName;
    private Long jobId;
}
