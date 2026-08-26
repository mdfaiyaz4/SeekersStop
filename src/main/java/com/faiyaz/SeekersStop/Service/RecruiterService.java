package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.RecruiterProfileUpdateRequestDto;
import com.faiyaz.SeekersStop.Dto.RecruiterRequestDto;
import com.faiyaz.SeekersStop.Dto.RecruiterResponseDto;
import com.faiyaz.SeekersStop.Entity.Company;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.CompanyRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.DuplicateResourceException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {
    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;
    private final FindByAuthenticationService findByAuthenticationService;


    public RecruiterService(RecruiterRepository recruiterRepository,
                            FindByAuthenticationService findByAuthenticationService,
                            CompanyRepository companyRepository) {
        this.recruiterRepository = recruiterRepository;
        this.findByAuthenticationService = findByAuthenticationService;
        this.companyRepository = companyRepository;
    }

    public RecruiterResponseDto createRecruiterProfile(RecruiterRequestDto recruiterRequestDto) {
        User user = findByAuthenticationService.findUser();

        if (recruiterRepository.existsByUser(user)){
            throw new DuplicateResourceException("Recruiter profile already exists");
        }
        Company company = companyRepository.findById(recruiterRequestDto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        Recruiter recruiter = new Recruiter();
        recruiter.setName(recruiterRequestDto.getName());
        recruiter.setUser(user);

        recruiter.setCompany(company);
        recruiter.setContactInfo(recruiterRequestDto.getContact());
        Recruiter saved = recruiterRepository.save(recruiter);

        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();

        recruiterResponseDto.setName(saved.getName());
        recruiterResponseDto.setCompanyId(saved.getCompany().getId());
        recruiterResponseDto.setCompanyName(saved.getCompany().getName());
        recruiterResponseDto.setContact(saved.getContactInfo());
        recruiterResponseDto.setId(saved.getId());
        return recruiterResponseDto;

    }

    public RecruiterResponseDto GetMyRecruiterProfile() {
        User user = findByAuthenticationService.findUser();
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();

        recruiterResponseDto.setName(recruiter.getName());
        recruiterResponseDto.setCompanyId(recruiter.getCompany().getId());
        recruiterResponseDto.setCompanyName(recruiter.getCompany().getName());
        recruiterResponseDto.setContact(recruiter.getContactInfo());
        recruiterResponseDto.setId(recruiter.getId());
        return recruiterResponseDto;
    }

    public RecruiterResponseDto UpdateMyRecruiterProfile(RecruiterProfileUpdateRequestDto recruiterProfileUpdateRequestDto) {
        User user = findByAuthenticationService.findUser();

        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        recruiter.setName(recruiterProfileUpdateRequestDto.getName());

        recruiter.setContactInfo(recruiterProfileUpdateRequestDto.getContact());
        Recruiter saved = recruiterRepository.save(recruiter);

        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();

        recruiterResponseDto.setName(saved.getName());
        recruiterResponseDto.setCompanyId(saved.getCompany().getId());
        recruiterResponseDto.setCompanyName(saved.getCompany().getName());
        recruiterResponseDto.setContact(saved.getContactInfo());
        recruiterResponseDto.setId(saved.getId());
        return recruiterResponseDto;
    }
}
