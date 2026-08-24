package com.faiyaz.SeekersStop.Service;


import com.faiyaz.SeekersStop.Dto.CompanyRequestDto;
import com.faiyaz.SeekersStop.Dto.CompanyResponseDto;
import com.faiyaz.SeekersStop.Entity.Company;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.CompanyRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.Repository.UserRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    public CompanyService(CompanyRepository companyRepository,  UserRepository userRepository
    , RecruiterRepository recruiterRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
    }
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private RecruiterRepository recruiterRepository;

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

        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Company company = recruiter.getCompany();

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


        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Recruiter recruiter = recruiterRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        Company company = recruiter.getCompany();

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
