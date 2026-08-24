package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.faiyaz.SeekersStop.Entity.User;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }








}
