package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.JobRequestDto;
import com.faiyaz.SeekersStop.Dto.JobResponseDto;
import com.faiyaz.SeekersStop.Entity.Job;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.JobRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ForbiddenException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;


    public JobService(JobRepository jobRepository,
                      RecruiterRepository recruiterRepository,
                      UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.userRepository = userRepository;
    }

    public JobResponseDto createJob(JobRequestDto jobRequestDto) {
        Authentication authentication = SecurityContextHolder
                .getContext().
                getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("Username Not Found"));
        Recruiter recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Recruiter Profile Not Found"));

        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setCompany(recruiter.getCompany());
        job.setSalary(jobRequestDto.getSalary());
        job.setLocation(jobRequestDto.getLocation());
        job.setDeadline(jobRequestDto.getDeadline());
        job.setExperience(jobRequestDto.getExperience());
        job.setQualification(jobRequestDto.getQualification());
        job.setTitle(jobRequestDto.getTitle());
        job.setDescription(jobRequestDto.getDescription());
        job.setActive(true);

        Job savedJob = jobRepository.save(job);

        JobResponseDto jobResponseDto = new JobResponseDto();

        jobResponseDto.setTitle(savedJob.getTitle());
        jobResponseDto.setDescription(savedJob.getDescription());
        jobResponseDto.setExperience(savedJob.getExperience());
        jobResponseDto.setQualification(savedJob.getQualification());
        jobResponseDto.setSalary(savedJob.getSalary());
        jobResponseDto.setLocation(savedJob.getLocation());
        jobResponseDto.setId(savedJob.getId());
        jobResponseDto.setDeadline(savedJob.getDeadline());
        jobResponseDto.setCompanyName(savedJob.getCompany().getName());
        jobResponseDto.setRecruiterName(savedJob.getRecruiter().getName());

        return jobResponseDto;

    }

    public List<JobResponseDto> getAllJobs() {
        List<Job> jobs = jobRepository.findByActiveTrue();
        List<JobResponseDto> jobResponseDtos = new ArrayList<>();
        for (Job job : jobs) {
            JobResponseDto jobResponseDto = new JobResponseDto();
            jobResponseDto.setId(job.getId());
            jobResponseDto.setDescription(job.getDescription());
            jobResponseDto.setLocation(job.getLocation());
            jobResponseDto.setExperience(job.getExperience());
            jobResponseDto.setCompanyName(job.getCompany().getName());
            jobResponseDto.setDeadline(job.getDeadline());
            jobResponseDto.setQualification(job.getQualification());
            jobResponseDto.setSalary(job.getSalary());
            jobResponseDto.setRecruiterName(job.getRecruiter().getName());
            jobResponseDto.setTitle(job.getTitle());
            jobResponseDtos.add(jobResponseDto);
        }
        return jobResponseDtos;
    }

    public JobResponseDto getJobById(Long id) {
        Job job = jobRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));
        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setId(job.getId());
        jobResponseDto.setDescription(job.getDescription());
        jobResponseDto.setLocation(job.getLocation());
        jobResponseDto.setExperience(job.getExperience());
        jobResponseDto.setCompanyName(job.getCompany().getName());
        jobResponseDto.setDeadline(job.getDeadline());
        jobResponseDto.setQualification(job.getQualification());
        jobResponseDto.setSalary(job.getSalary());
        jobResponseDto.setRecruiterName(job.getRecruiter().getName());
        jobResponseDto.setTitle(job.getTitle());
        return jobResponseDto;
    }

    public JobResponseDto UpdateJobById(Long id, JobRequestDto jobRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter Profile Not Found"));
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new ForbiddenException("You Cannot Modify This Job");

        }
        job.setTitle(jobRequestDto.getTitle());
        job.setQualification(jobRequestDto.getQualification());
        job.setDescription(jobRequestDto.getDescription());
        job.setExperience(jobRequestDto.getExperience());
        job.setSalary(jobRequestDto.getSalary());
        job.setLocation(jobRequestDto.getLocation());
        job.setDeadline(jobRequestDto.getDeadline());

        Job saved = jobRepository.save(job);

        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setId(saved.getId());
        jobResponseDto.setQualification(saved.getQualification());
        jobResponseDto.setDescription(saved.getDescription());
        jobResponseDto.setLocation(saved.getLocation());
        jobResponseDto.setExperience(saved.getExperience());
        jobResponseDto.setSalary(saved.getSalary());
        jobResponseDto.setDeadline(saved.getDeadline());
        jobResponseDto.setRecruiterName(saved.getRecruiter().getName());
        jobResponseDto.setTitle(saved.getTitle());
        jobResponseDto.setCompanyName(saved.getCompany().getName());
        return jobResponseDto;
    }

    public void deactivateJobById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter Profile Not Found"));
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new ForbiddenException("You cannot deactivate this Job");

        }
        job.setActive(false);
        jobRepository.save(job);

    }

    public void activateJobById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter Profile Not Found"));
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job Not Found"));
        if (!job.getRecruiter().getId().equals(recruiter.getId()) || job.getActive() == true) {
            throw new ForbiddenException("You cannot activate this job or the job is already activated");

        }
        job.setActive(true);
        jobRepository.save(job);
    }
}
