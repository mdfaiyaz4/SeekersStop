package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.JobRequestDto;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Repository.JobRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.IllegalStateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceCompanyRequirementTest {
    @Mock private JobRepository jobRepository;
    @Mock private RecruiterRepository recruiterRepository;
    @Mock private FindByAuthenticationService findByAuthenticationService;
    @InjectMocks private JobService jobService;

    @Test
    void doesNotSaveJobWhenRecruiterHasNoCompany() {
        User user = new User();
        Recruiter recruiter = new Recruiter();
        recruiter.setUser(user);
        when(findByAuthenticationService.findUser()).thenReturn(user);
        when(recruiterRepository.findByUser(user)).thenReturn(Optional.of(recruiter));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> jobService.createJob(new JobRequestDto())
        );

        assertEquals("Company profile is required before creating a job", exception.getMessage());
        verify(jobRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
