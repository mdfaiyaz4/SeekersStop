package com.faiyaz.SeekersStop.Dto;

import lombok.Data;

@Data
public class JobSeekerResponseDto {
    private Long jobSeekerId;
    private String skill;
    private String name;
    private String cv;
    private String experience;
    private String contact;
}
