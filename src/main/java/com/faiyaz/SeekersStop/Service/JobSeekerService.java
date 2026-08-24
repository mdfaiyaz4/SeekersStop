package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.JobSeekerRequestDto;
import com.faiyaz.SeekersStop.Dto.JobSeekerResponseDto;
import com.faiyaz.SeekersStop.Entity.JobSeeker;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.JobSeekerRepository;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    public JobSeekerService(JobSeekerRepository jobSeekerRepository, UserRepository userRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.userRepository = userRepository;

    }
    public JobSeekerResponseDto createJobSeekerProfile(JobSeekerRequestDto jobSeekerRequestDto){
       JobSeeker jobSeeker = new  JobSeeker();
       jobSeeker.setName(jobSeekerRequestDto.getName());
       jobSeeker.setCv(jobSeekerRequestDto.getCv());
       jobSeeker.setExperience(jobSeekerRequestDto.getExperience());
       jobSeeker.setContact(jobSeekerRequestDto.getContact());
       jobSeeker.setSkill(jobSeekerRequestDto.getSkill());

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
       User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));
       jobSeeker.setUser(user);
        JobSeeker saved = jobSeekerRepository.save(jobSeeker);

        JobSeekerResponseDto jobSeekerResponseDto = new JobSeekerResponseDto();
        jobSeekerResponseDto.setJobSeekerId(saved.getId());
        jobSeekerResponseDto.setName(saved.getName());
        jobSeekerResponseDto.setCv(saved.getCv());
        jobSeekerResponseDto.setExperience(saved.getExperience());
        jobSeekerResponseDto.setContact(saved.getContact());
        jobSeekerResponseDto.setSkill(saved.getSkill());
        return jobSeekerResponseDto;

    }
    public JobSeekerResponseDto GetMyJobSeekerProfile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));
        JobSeeker jobSeeker =  jobSeekerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Invalid JobSeeker"));

        JobSeekerResponseDto jobSeekerResponseDto = new JobSeekerResponseDto();
        jobSeekerResponseDto.setJobSeekerId(jobSeeker.getId());
        jobSeekerResponseDto.setName(jobSeeker.getName());
        jobSeekerResponseDto.setCv(jobSeeker.getCv());
        jobSeekerResponseDto.setExperience(jobSeeker.getExperience());
        jobSeekerResponseDto.setContact(jobSeeker.getContact());
        jobSeekerResponseDto.setSkill(jobSeeker.getSkill());
        return jobSeekerResponseDto;
    }

    public JobSeekerResponseDto UpdateMyJobSeekerProfile(JobSeekerRequestDto jobSeekerRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));
        JobSeeker jobSeeker =  jobSeekerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Invalid JobSeeker"));

        jobSeeker.setContact(jobSeekerRequestDto.getContact());
        jobSeeker.setSkill(jobSeekerRequestDto.getSkill());
        jobSeeker.setExperience(jobSeekerRequestDto.getExperience());
        jobSeeker.setCv(jobSeekerRequestDto.getCv());
        jobSeeker.setName(jobSeekerRequestDto.getName());
        jobSeekerRepository.save(jobSeeker);

        JobSeekerResponseDto jobSeekerResponseDto = new JobSeekerResponseDto();
        jobSeekerResponseDto.setJobSeekerId(jobSeeker.getId());
        jobSeekerResponseDto.setName(jobSeeker.getName());
        jobSeekerResponseDto.setCv(jobSeeker.getCv());
        jobSeekerResponseDto.setExperience(jobSeeker.getExperience());
        jobSeekerResponseDto.setContact(jobSeeker.getContact());
        jobSeekerResponseDto.setSkill(jobSeeker.getSkill());
        return jobSeekerResponseDto;
    }
}
