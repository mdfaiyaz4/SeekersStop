package com.faiyaz.SeekersStop.Dto;

import lombok.Data;

@Data
public class CompanyResponseDto {
    private String name;
    private Long id;
    private String description;
    private String location;
    private String contact;
    private String website;
}
