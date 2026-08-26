package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.JobSeekerRequestDto;
import com.faiyaz.SeekersStop.Dto.JobSeekerResponseDto;
import com.faiyaz.SeekersStop.Entity.JobSeeker;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.JobSeekerRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.DuplicateResourceException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerService {
    private final JobSeekerRepository jobSeekerRepository;

    private final FindByAuthenticationService findByAuthentication;

    public JobSeekerService(JobSeekerRepository jobSeekerRepository,
                            FindByAuthenticationService findByAuthentication) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.findByAuthentication = findByAuthentication;

    }
    public JobSeekerResponseDto createJobSeekerProfile(JobSeekerRequestDto jobSeekerRequestDto){
        User user = findByAuthentication.findUser();
        if(jobSeekerRepository.existsByUser(user)){
            throw new DuplicateResourceException("Jobseeker already exists");
        }

       JobSeeker jobSeeker = new  JobSeeker();
        jobSeeker.setUser(user);
       jobSeeker.setName(jobSeekerRequestDto.getName());
       jobSeeker.setCv(jobSeekerRequestDto.getCv());
       jobSeeker.setExperience(jobSeekerRequestDto.getExperience());
       jobSeeker.setContact(jobSeekerRequestDto.getContact());
       jobSeeker.setSkill(jobSeekerRequestDto.getSkill());


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
    public JobSeekerResponseDto getMyJobSeekerProfile(){

        User  user = findByAuthentication.findUser();
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

    public JobSeekerResponseDto updateMyJobSeekerProfile(JobSeekerRequestDto jobSeekerRequestDto) {
        User user = findByAuthentication.findUser();
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
