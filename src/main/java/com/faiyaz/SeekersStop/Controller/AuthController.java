package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.LoginRequestDto;
import com.faiyaz.SeekersStop.Dto.LoginResponseDto;
import com.faiyaz.SeekersStop.Dto.RegisterRequestDto;
import com.faiyaz.SeekersStop.Dto.RegisterResponseDto;
import com.faiyaz.SeekersStop.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public RegisterResponseDto registerNewUser(@Valid @RequestBody RegisterRequestDto requestRegisterDto){

         return authService.registerUser(requestRegisterDto);
    }

    @PostMapping("/login")
    public LoginResponseDto loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto){

        return authService.login(loginRequestDto);
    }



}
