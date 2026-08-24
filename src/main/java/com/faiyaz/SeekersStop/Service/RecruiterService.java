package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.RecruiterProfileUpdateRequestDto;
import com.faiyaz.SeekersStop.Dto.RecruiterRequestDto;
import com.faiyaz.SeekersStop.Dto.RecruiterResponseDto;
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
public class RecruiterService {
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public RecruiterService(RecruiterRepository recruiterRepository,
                            UserRepository userRepository,
                            CompanyRepository companyRepository) {
        this.recruiterRepository = recruiterRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public RecruiterResponseDto createRecruiterProfile(RecruiterRequestDto recruiterRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

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
