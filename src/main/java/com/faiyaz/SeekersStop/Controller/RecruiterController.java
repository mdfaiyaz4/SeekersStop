package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.RecruiterProfileUpdateRequestDto;
import com.faiyaz.SeekersStop.Dto.RecruiterRequestDto;
import com.faiyaz.SeekersStop.Dto.RecruiterResponseDto;
import com.faiyaz.SeekersStop.Service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    private final RecruiterService recruiterService;
    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }
    @PostMapping("/profile")
public ResponseEntity<RecruiterResponseDto> CreateRecruiterProfile
        (@Valid @RequestBody RecruiterRequestDto recruiterRequestDto){
        RecruiterResponseDto recruiterProfile = recruiterService.createRecruiterProfile(recruiterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recruiterProfile);
    }
@GetMapping("/profile")
public RecruiterResponseDto GetMyRecruiterProfile(){
        return recruiterService.GetMyRecruiterProfile();
}

@PutMapping("/profile")
    public RecruiterResponseDto updateRecruiterProfile(
           @Valid @RequestBody RecruiterProfileUpdateRequestDto recruiterProfileUpdateRequestDto){
        return recruiterService.UpdateMyRecruiterProfile(recruiterProfileUpdateRequestDto);
}

}
