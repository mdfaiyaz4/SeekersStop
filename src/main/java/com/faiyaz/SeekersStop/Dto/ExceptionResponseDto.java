package com.faiyaz.SeekersStop.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExceptionResponseDto {
    private int status;

    public ExceptionResponseDto(int status,String message) {
        this.status = status;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    private String message;
    private LocalDateTime date;
}
