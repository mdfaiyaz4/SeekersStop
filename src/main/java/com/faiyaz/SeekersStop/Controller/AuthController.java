package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.LoginRequestDto;
import com.faiyaz.SeekersStop.Dto.LoginResponseDto;
import com.faiyaz.SeekersStop.Dto.RegisterRequestDto;
import com.faiyaz.SeekersStop.Dto.RegisterResponseDto;
import com.faiyaz.SeekersStop.Service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

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
