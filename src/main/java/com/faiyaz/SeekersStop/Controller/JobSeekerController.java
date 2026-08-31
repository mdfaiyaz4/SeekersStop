package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.JobSeekerRequestDto;
import com.faiyaz.SeekersStop.Dto.JobSeekerResponseDto;
import com.faiyaz.SeekersStop.Service.JobSeekerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobseeker")
@SecurityRequirement(name = "bearerAuth")
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;

    public JobSeekerController(JobSeekerService jobSeekerService) {
        this.jobSeekerService = jobSeekerService;
    }

    @PostMapping("/profile")
    public ResponseEntity<JobSeekerResponseDto> createJobSeekerProfile(@Valid @RequestBody JobSeekerRequestDto jobSeekerRequestDto) {
        JobSeekerResponseDto jobSeekerProfile = jobSeekerService.createJobSeekerProfile(jobSeekerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobSeekerProfile);
    }

    @GetMapping("/profile")
    public JobSeekerResponseDto getJobSeekerProfile() {
        return jobSeekerService.getMyJobSeekerProfile();
    }

    @PutMapping("/profile")
    public JobSeekerResponseDto updateJobSeekerProfile(@Valid @RequestBody JobSeekerRequestDto request) {
        return jobSeekerService.updateMyJobSeekerProfile(request);
    }
}
