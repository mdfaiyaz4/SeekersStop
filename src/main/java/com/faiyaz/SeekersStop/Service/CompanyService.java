package com.faiyaz.SeekersStop.Service;


import com.faiyaz.SeekersStop.Dto.CompanyRequestDto;
import com.faiyaz.SeekersStop.Dto.CompanyResponseDto;
import com.faiyaz.SeekersStop.Entity.Company;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.CompanyRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.DuplicateResourceException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyService {
    public CompanyService(CompanyRepository companyRepository
    , RecruiterRepository recruiterRepository,FindByAuthenticationService  findByAuthenticationService
                          ) {
        this.companyRepository = companyRepository;
        this.recruiterRepository = recruiterRepository;
        this.findByAuthenticationService = findByAuthenticationService;
    }
    private final FindByAuthenticationService  findByAuthenticationService;
    private final CompanyRepository companyRepository;
    private final RecruiterRepository recruiterRepository;

    public CompanyResponseDto createCompany(CompanyRequestDto companyRequestDto){

        Company company = new Company();
        company.setName(companyRequestDto.getName());
        company.setDescription(companyRequestDto.getDescription());
        company.setLocation(companyRequestDto.getLocation());
        company.setWebsite(companyRequestDto.getWebsite());
        company.setContactInfo(companyRequestDto.getContact());

        Company saved = companyRepository.save(company);

        CompanyResponseDto companyResponseDto = new CompanyResponseDto();
        companyResponseDto.setName(saved.getName());
        companyResponseDto.setDescription(saved.getDescription());
        companyResponseDto.setLocation(saved.getLocation());
        companyResponseDto.setWebsite(saved.getWebsite());
        companyResponseDto.setContact(saved.getContactInfo());
        companyResponseDto.setId(saved.getId());
        return companyResponseDto;

    }

    public CompanyResponseDto getMyCompany(){

        User user = findByAuthenticationService.findUser();
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Company company = recruiter.getCompany();
        if(company == null){
            throw new ResourceNotFoundException("Company not found");
        }

        CompanyResponseDto companyResponseDto = new CompanyResponseDto();
        companyResponseDto.setName(company.getName());
        companyResponseDto.setDescription(company.getDescription());
        companyResponseDto.setLocation(company.getLocation());
        companyResponseDto.setWebsite(company.getWebsite());
        companyResponseDto.setContact(company.getContactInfo());
        companyResponseDto.setId(company.getId());
        return companyResponseDto;

    }

    public CompanyResponseDto updateMyCompany(CompanyRequestDto companyRequestDto){


        User user = findByAuthenticationService.findUser();
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Company company = recruiter.getCompany();
        if(company == null){
            throw new ResourceNotFoundException("Company not found");
        }

        company.setName(companyRequestDto.getName());
        company.setDescription(companyRequestDto.getDescription());
        company.setLocation(companyRequestDto.getLocation());
        company.setWebsite(companyRequestDto.getWebsite());
        company.setContactInfo(companyRequestDto.getContact());

        Company saved = companyRepository.save(company);

        CompanyResponseDto companyResponseDto = new CompanyResponseDto();
        companyResponseDto.setName(saved.getName());
        companyResponseDto.setDescription(saved.getDescription());
        companyResponseDto.setLocation(saved.getLocation());
        companyResponseDto.setWebsite(saved.getWebsite());
        companyResponseDto.setContact(saved.getContactInfo());
        companyResponseDto.setId(saved.getId());
        return companyResponseDto;

    }

}
