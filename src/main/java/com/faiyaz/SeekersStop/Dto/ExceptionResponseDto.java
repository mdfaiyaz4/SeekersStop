package com.faiyaz.SeekersStop.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponseDto {
    private int status;

    public ExceptionResponseDto(int status,String message) {
        this.status = status;
        this.message = message;
        this.date = new Date();
    }

    private String message;
    private Date date;
}
