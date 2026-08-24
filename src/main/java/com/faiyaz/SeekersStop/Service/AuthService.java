package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.LoginRequestDto;
import com.faiyaz.SeekersStop.Dto.LoginResponseDto;
import com.faiyaz.SeekersStop.Dto.RegisterRequestDto;
import com.faiyaz.SeekersStop.Dto.RegisterResponseDto;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.UsernameAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }
        String encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(registerRequestDto.getRole());
        User savedUser = userRepository.save(user);

        RegisterResponseDto registerResponseDto = new RegisterResponseDto();
        registerResponseDto.setUsername(savedUser.getUsername());
        registerResponseDto.setRole(savedUser.getRole());
        registerResponseDto.setId(savedUser.getId());
        return registerResponseDto;
    }

    public LoginResponseDto login(LoginRequestDto request){
        String username = request.getUsername();
        String password = request.getPassword();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            boolean matches = passwordEncoder.matches(password, user.get().getPassword());
            if(!matches){
                throw new BadCredentialsException("Invalid username or password");
            }
            else{
                String token = jwtService.generateToken(user.get());
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setToken(token);
                return loginResponseDto;

            }
        }
        else {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
