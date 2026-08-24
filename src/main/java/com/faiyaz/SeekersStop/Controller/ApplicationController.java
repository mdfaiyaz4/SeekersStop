package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.ApplicationRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationResponseDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusResponseDto;
import com.faiyaz.SeekersStop.Service.ApplicationService;
import com.faiyaz.SeekersStop.Service.RecruiterApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final RecruiterApplicationService  recruiterApplicationService;

    public ApplicationController(ApplicationService applicationService, RecruiterApplicationService recruiterApplicationService) {
        this.applicationService = applicationService;
        this.recruiterApplicationService = recruiterApplicationService;
    }
    @PostMapping()
    public ResponseEntity<ApplicationResponseDto> createApplication(@Valid @RequestBody ApplicationRequestDto applicationRequestDto) {
        ApplicationResponseDto jobApplication = applicationService.createJobApplication(applicationRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(jobApplication);
    }


    @GetMapping("/recruiter")
    public List<ApplicationResponseDto> getAllApplications() {
        return recruiterApplicationService.getAllApplications();
    }

    @PatchMapping("/{applicationId}/status")
    public ApplicationStatusResponseDto changeStatus ( @PathVariable Long applicationId, @Valid
                                                          @RequestBody ApplicationStatusRequestDto statusRequestDto){
        return applicationService.changeApplicationStatus(statusRequestDto,applicationId);
    }
    @GetMapping("/{applicationId}")
    public ApplicationResponseDto getApplicationById( @PathVariable Long applicationId){
        return applicationService.getApplicationById(applicationId);
    }
    @GetMapping("/my")
    public List<ApplicationResponseDto> getMyApplications(){
        return applicationService.getAllApplications();
    }
}
