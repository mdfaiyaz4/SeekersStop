package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.ApplicationResponseDto;
import com.faiyaz.SeekersStop.Entity.Application;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.ApplicationRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecruiterApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;

    public List<ApplicationResponseDto> getAllApplications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        List<Application> applications = applicationRepository.findByJobRecruiterId(recruiter.getId());
        List<ApplicationResponseDto> responseDtos = new ArrayList<>();
        for (Application application : applications) {
            ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
            applicationResponseDto.setApplicationStatus(application.getStatus());
            applicationResponseDto.setApplicationId(application.getId());
            applicationResponseDto.setAppliedAt(application.getAppliedAt());
            applicationResponseDto.setJobId(application.getJob().getId());
            applicationResponseDto.setJobName(application.getJob().getTitle());
            applicationResponseDto.setJobSeeker(application.getJobSeeker().getName());
            responseDtos.add(applicationResponseDto);
        }
        return responseDtos;
    }
}
