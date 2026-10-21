package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.ApplicationRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationResponseDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusResponseDto;
import com.faiyaz.SeekersStop.Entity.*;
import com.faiyaz.SeekersStop.Enums.ApplicationStatus;
import com.faiyaz.SeekersStop.Repository.ApplicationRepository;
import com.faiyaz.SeekersStop.Repository.JobRepository;
import com.faiyaz.SeekersStop.Repository.JobSeekerRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.DuplicateResourceException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ForbiddenException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.InvalidStatusTransitionException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobRepository jobRepository;
    private final RecruiterRepository  recruiterRepository;
    private final FindByAuthenticationService findByAuthenticationService;
    private final FileStorageService fileStorageService;


    public ApplicationService(ApplicationRepository applicationRepository
    , JobSeekerRepository jobSeekerRepository, JobRepository jobRepository
    , RecruiterRepository recruiterRepository, FindByAuthenticationService findByAuthenticationService,
      FileStorageService fileStorageService) {
        this.applicationRepository = applicationRepository;
        this.findByAuthenticationService = findByAuthenticationService;
        this.jobRepository = jobRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.recruiterRepository = recruiterRepository;
        this.fileStorageService = fileStorageService;
    }

    public ApplicationResponseDto createJobApplication(ApplicationRequestDto applicationRequestDto){

        User user = findByAuthenticationService.findUser();
        JobSeeker jobseeker = jobSeekerRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        fileStorageService.ensureCurrentCvExists(jobseeker.getCv());
        Job job = jobRepository.findByIdAndActiveTrue(applicationRequestDto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("Job not found or Job is not Active"));
        if(job.getDeadline().isBefore(LocalDate.now())) throw new ForbiddenException("Deadline has Passed");

        if (applicationRepository.existsByJobAndJobSeeker(job, jobseeker)) {
            throw new DuplicateResourceException("Application Already Exists");
        }

        Application application = new  Application();
        application.setJob(job);
        application.setJobSeeker(jobseeker);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.PENDING);
        String submittedCvPath = fileStorageService.copyCvForApplication(jobseeker.getCv());
        application.setSubmittedCvPath(submittedCvPath);

        Application saved;
        try {
            saved = applicationRepository.save(application);
        } catch (RuntimeException saveFailure) {
            try {
                fileStorageService.deleteApplicationCv(submittedCvPath);
            } catch (RuntimeException cleanupFailure) {
                saveFailure.addSuppressed(cleanupFailure);
            }
            throw saveFailure;
        }

        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
        applicationResponseDto.setApplicationId(saved.getId());
        applicationResponseDto.setApplicationStatus(saved.getStatus());
        applicationResponseDto.setJobName(saved.getJob().getTitle());
        applicationResponseDto.setJobSeeker(saved.getJobSeeker().getName());
        applicationResponseDto.setAppliedAt(saved.getAppliedAt());
        applicationResponseDto.setJobId(saved.getJob().getId());
        return applicationResponseDto;
    }
    public ApplicationStatusResponseDto changeApplicationStatus(ApplicationStatusRequestDto applicationStatusRequestDto,long applicationId){
        User user = findByAuthenticationService.findUser();
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Application application =  applicationRepository.findByIdAndJobRecruiterId(applicationId,recruiter.getId()).orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        ApplicationStatus currentStatus = application.getStatus();
        ApplicationStatus requestedStatus = applicationStatusRequestDto.getApplicationStatus();
        if (!isValidStatusTransition(currentStatus, requestedStatus)) {
            throw new InvalidStatusTransitionException(currentStatus, requestedStatus);
        }

        application.setStatus(requestedStatus);
        Application saved = applicationRepository.save(application);
        ApplicationStatusResponseDto applicationStatusResponseDto = new ApplicationStatusResponseDto();
        applicationStatusResponseDto.setApplicationStatus(saved.getStatus());
        return applicationStatusResponseDto;
    }

    static boolean isValidStatusTransition(ApplicationStatus currentStatus, ApplicationStatus requestedStatus) {
        if (currentStatus == null || requestedStatus == null) {
            return false;
        }

        return switch (currentStatus) {
            case PENDING -> requestedStatus == ApplicationStatus.SHORTLISTED
                    || requestedStatus == ApplicationStatus.REJECTED;
            case SHORTLISTED -> requestedStatus == ApplicationStatus.ACCEPTED
                    || requestedStatus == ApplicationStatus.REJECTED;
            case ACCEPTED, REJECTED, WITHDRAWN -> false;
        };
    }

    public ApplicationResponseDto withdrawApplication(long applicationId) {
        User user = findByAuthenticationService.findUser();
        JobSeeker jobSeeker = jobSeekerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        Application application = applicationRepository.findByIdAndJobSeekerId(applicationId, jobSeeker.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new InvalidStatusTransitionException(application.getStatus(), ApplicationStatus.WITHDRAWN);
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        Application saved = applicationRepository.save(application);

        ApplicationResponseDto response = new ApplicationResponseDto();
        response.setApplicationId(saved.getId());
        response.setApplicationStatus(saved.getStatus());
        response.setJobName(saved.getJob().getTitle());
        response.setJobSeeker(saved.getJobSeeker().getName());
        response.setAppliedAt(saved.getAppliedAt());
        response.setJobId(saved.getJob().getId());
        return response;
    }

    public ApplicationResponseDto getApplicationById(Long applicationId){
        User user = findByAuthenticationService.findUser();
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
        User user = findByAuthenticationService.findUser();
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
