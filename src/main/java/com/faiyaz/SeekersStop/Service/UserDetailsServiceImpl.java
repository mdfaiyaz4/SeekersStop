package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if(!byUsername.isPresent()){
            throw new UsernameNotFoundException("Username not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(byUsername.get().getUsername())
                .password(byUsername.get().getPassword())
                .roles(byUsername.get().getRole().name())
                .build();
    }
}
