package com.faiyaz.SeekersStop.Service;

import org.springframework.stereotype.Service;

import com.faiyaz.SeekersStop.Repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }








}
