package com.faiyaz.SeekersStop.Dto;

import com.faiyaz.SeekersStop.Enums.Role;
import lombok.Data;

@Data
public class RegisterResponseDto {
    private String username;
    private Long id;
    private Role role;
}
