package com.faiyaz.SeekersStop.Dto;

import com.faiyaz.SeekersStop.Entity.JobSeeker;
import com.faiyaz.SeekersStop.Enums.ApplicationStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ApplicationResponseDto {
    private Long applicationId;
    private Date appliedAt;
    private String jobSeeker;
    private ApplicationStatus applicationStatus;
    private String jobName;
    private Long jobId;
}
