package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindByAuthenticationService {
    private UserRepository userRepository;
    public FindByAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username cannot be found"));

    }
}
