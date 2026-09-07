package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.JobSeekerRequestDto;
import com.faiyaz.SeekersStop.Dto.JobSeekerResponseDto;
import com.faiyaz.SeekersStop.Service.JobSeekerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/jobseeker")
@SecurityRequirement(name = "bearerAuth")
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;

    public JobSeekerController(JobSeekerService jobSeekerService) {
        this.jobSeekerService = jobSeekerService;
    }

    @PostMapping(value = "/profile",consumes = "multipart/form-data")
    public ResponseEntity<JobSeekerResponseDto> createJobSeekerProfile( @RequestPart("cv") MultipartFile cv,
                                                                       @Valid @RequestPart("profile") JobSeekerRequestDto jobSeekerRequestDto) {


        JobSeekerResponseDto jobSeekerProfile = jobSeekerService.createJobSeekerProfile(jobSeekerRequestDto,cv);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobSeekerProfile);
    }

    @GetMapping("/cv")
    public ResponseEntity<Resource> getMyCv(){
        Resource cv =  jobSeekerService.getMyCv();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(cv);
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
