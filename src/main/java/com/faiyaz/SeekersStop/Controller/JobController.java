package com.faiyaz.SeekersStop.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.faiyaz.SeekersStop.Dto.JobRequestDto;
import com.faiyaz.SeekersStop.Dto.JobResponseDto;
import com.faiyaz.SeekersStop.Service.JobService;
import com.faiyaz.SeekersStop.Service.RecruiterApplicationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/jobs")
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    JobController(JobService jobService,
                  RecruiterApplicationService recruiterApplicationService) {
        this.jobService = jobService;

        this.recruiterApplicationService = recruiterApplicationService;
    }

    private JobService jobService;

    private final RecruiterApplicationService recruiterApplicationService;

    @PostMapping()
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobRequestDto jobRequestDto) {
        JobResponseDto job = jobService.createJob(jobRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping()
    public Page<JobResponseDto> getJobs(@PageableDefault(size = 2,
            sort = "salary",
            direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String experience)

    {
        return jobService.getAllJobs(title,pageable,location,experience);
    }

    @GetMapping("/{id}")
    public JobResponseDto getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @PutMapping("/{id}")
    public JobResponseDto updateJob(@PathVariable Long id, @Valid @RequestBody JobRequestDto jobRequestDto) {
        return jobService.updateJobById(id, jobRequestDto);
    }

    @DeleteMapping("/deactive/{id}")
    public ResponseEntity<Void> deactivateJobProfileById(@PathVariable Long id) {
        jobService.deactivateJobById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<Void> activateJobProfileById(@PathVariable Long id) {
        jobService.activateJobById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    public ResponseEntity<Page<JobResponseDto>> myJobs(Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(jobService.getMyJobs(pageable));
    }
}
