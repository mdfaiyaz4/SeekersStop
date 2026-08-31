package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.ApplicationRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationResponseDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusResponseDto;
import com.faiyaz.SeekersStop.Service.ApplicationService;
import com.faiyaz.SeekersStop.Service.RecruiterApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Applications",description = "Applications Control Api")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final RecruiterApplicationService  recruiterApplicationService;

    public ApplicationController(ApplicationService applicationService, RecruiterApplicationService recruiterApplicationService) {
        this.applicationService = applicationService;
        this.recruiterApplicationService = recruiterApplicationService;
    }
    @Operation(summary = "create application")
    @PostMapping()
    public ResponseEntity<ApplicationResponseDto> createApplication(@Valid @RequestBody ApplicationRequestDto applicationRequestDto) {
        ApplicationResponseDto jobApplication = applicationService.createJobApplication(applicationRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(jobApplication);
    }

    @Operation(summary = "get all applications")
    @GetMapping("/recruiter")
    public List<ApplicationResponseDto> getAllApplications() {
        return recruiterApplicationService.getAllApplications();
    }

    @Operation(summary = "update application status")
    @PatchMapping("/{applicationId}/status")
    public ApplicationStatusResponseDto changeStatus ( @PathVariable Long applicationId, @Valid
                                                          @RequestBody ApplicationStatusRequestDto statusRequestDto){
        return applicationService.changeApplicationStatus(statusRequestDto,applicationId);
    }

    @Operation(summary = "get application by id")
    @GetMapping("/{applicationId}")
    public ApplicationResponseDto getApplicationById( @PathVariable Long applicationId){
        return applicationService.getApplicationById(applicationId);
    }

    @Operation(summary = "get all application for particular jobseeker")
    @GetMapping("/my")
    public List<ApplicationResponseDto> getMyApplications(){
        return applicationService.getAllApplications();
    }
}
