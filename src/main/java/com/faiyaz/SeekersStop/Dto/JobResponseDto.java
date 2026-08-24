package com.faiyaz.SeekersStop.Dto;

import com.faiyaz.SeekersStop.Entity.Job;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobResponseDto {

    private Long id;
    private String title;
    private String description;
    private String experience;
    private String qualification;
    private Double salary;
    private String location;
    private LocalDate deadline;
    private String recruiterName;
    private String companyName;


}
