package com.faiyaz.SeekersStop.Dto;

import java.time.LocalDate;

import lombok.Data;

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
    private Boolean active;


}
