package com.faiyaz.SeekersStop.Controller;

import com.faiyaz.SeekersStop.Dto.CompanyRequestDto;
import com.faiyaz.SeekersStop.Dto.CompanyResponseDto;
import com.faiyaz.SeekersStop.Service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    private final CompanyService companyService;

    @PostMapping()
    public ResponseEntity<CompanyResponseDto> createCompany(@Valid @RequestBody CompanyRequestDto companyRequestDto){
        CompanyResponseDto company = companyService.createCompany(companyRequestDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
    @PutMapping()
    public CompanyResponseDto updateCompany(@Valid @RequestBody CompanyRequestDto companyRequestDto){
        return companyService.updateMyCompany(companyRequestDto);
    }
    @GetMapping()
    public CompanyResponseDto getCompany(){
        return companyService.getMyCompany();
    }
}
