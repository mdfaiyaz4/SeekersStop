package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.*;
import com.faiyaz.SeekersStop.Entity.*;
import com.faiyaz.SeekersStop.Enums.ApplicationStatus;
import com.faiyaz.SeekersStop.Repository.*;
import com.faiyaz.SeekersStop.UserDefinedExceptions.DuplicateResourceException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ForbiddenException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobRepository jobRepository;
    private final RecruiterRepository  recruiterRepository;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository
    , JobSeekerRepository jobSeekerRepository, JobRepository jobRepository
    , RecruiterRepository recruiterRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public ApplicationResponseDto createJobApplication(ApplicationRequestDto applicationRequestDto){

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        JobSeeker jobseeker = jobSeekerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        Job job = jobRepository.findByIdAndActiveTrue(applicationRequestDto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("Job not found or Job is not Active"));
        if(job.getDeadline().isBefore(LocalDate.now())) throw new ForbiddenException("Deadline has Passed");

        Application application = new  Application();
        application.setJob(job);
        application.setJobSeeker(jobseeker);
        application.setAppliedAt(new Date());
        application.setStatus(ApplicationStatus.PENDING);

        if(!applicationRepository.existsByJobAndJobSeeker(job,jobseeker)) {
            Application saved = applicationRepository.save(application);
            ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
            applicationResponseDto.setApplicationId(saved.getId());
            applicationResponseDto.setApplicationStatus(saved.getStatus());
            applicationResponseDto.setJobName(saved.getJob().getTitle());
            applicationResponseDto.setJobSeeker(saved.getJobSeeker().getName());
            applicationResponseDto.setAppliedAt(saved.getAppliedAt());
            applicationResponseDto.setJobId(saved.getJob().getId());
            return applicationResponseDto;
        }
        else{
            throw new DuplicateResourceException("Application Already Exists");
        }
    }
    public ApplicationStatusResponseDto changeApplicationStatus(ApplicationStatusRequestDto applicationStatusRequestDto,long applicationId){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Application application =  applicationRepository.findByIdAndJobRecruiterId(applicationId,recruiter.getId()).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        application.setStatus(applicationStatusRequestDto.getApplicationStatus());
        Application saved = applicationRepository.save(application);
        ApplicationStatusResponseDto applicationStatusResponseDto = new ApplicationStatusResponseDto();
        applicationStatusResponseDto.setApplicationStatus(saved.getStatus());
        return applicationStatusResponseDto;
    }

    public ApplicationResponseDto getApplicationById(Long applicationId){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        JobSeeker jobSeeker = jobSeekerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        Application application = applicationRepository.findByIdAndJobSeekerId(applicationId, jobSeeker.getId()).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
        applicationResponseDto.setApplicationId(application.getId());
        applicationResponseDto.setApplicationStatus(application.getStatus());
        applicationResponseDto.setJobName(application.getJob().getTitle());
        applicationResponseDto.setJobSeeker(application.getJobSeeker().getName());
        applicationResponseDto.setAppliedAt(application.getAppliedAt());
        applicationResponseDto.setJobId(application.getJob().getId());
        return applicationResponseDto;
    }

    public List<ApplicationResponseDto> getAllApplications(){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        JobSeeker jobSeeker = jobSeekerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        List<Application> applications = applicationRepository.findByJobSeekerId( jobSeeker.getId());

        List<ApplicationResponseDto> applicationResponseDtos = new ArrayList<>();

        for(Application application : applications){
        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
        applicationResponseDto.setApplicationId(application.getId());
        applicationResponseDto.setApplicationStatus(application.getStatus());
        applicationResponseDto.setJobName(application.getJob().getTitle());
        applicationResponseDto.setJobSeeker(application.getJobSeeker().getName());
        applicationResponseDto.setAppliedAt(application.getAppliedAt());
        applicationResponseDto.setJobId(application.getJob().getId());
        applicationResponseDtos.add(applicationResponseDto);
    }
        return applicationResponseDtos;
    }
}
