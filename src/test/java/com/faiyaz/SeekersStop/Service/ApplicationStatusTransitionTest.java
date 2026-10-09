package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Dto.ApplicationStatusRequestDto;
import com.faiyaz.SeekersStop.Dto.ApplicationStatusResponseDto;
import com.faiyaz.SeekersStop.Dto.ApplicationRequestDto;
import com.faiyaz.SeekersStop.Dto.ExceptionResponseDto;
import com.faiyaz.SeekersStop.Entity.Application;
import com.faiyaz.SeekersStop.Entity.Job;
import com.faiyaz.SeekersStop.Entity.JobSeeker;
import com.faiyaz.SeekersStop.Entity.Recruiter;
import com.faiyaz.SeekersStop.Entity.User;
import com.faiyaz.SeekersStop.Enums.ApplicationStatus;
import com.faiyaz.SeekersStop.Repository.ApplicationRepository;
import com.faiyaz.SeekersStop.Repository.JobRepository;
import com.faiyaz.SeekersStop.Repository.JobSeekerRepository;
import com.faiyaz.SeekersStop.Repository.RecruiterRepository;
import com.faiyaz.SeekersStop.UserDefinedExceptions.GlobalExceptionHandler;
import com.faiyaz.SeekersStop.UserDefinedExceptions.DuplicateResourceException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.InvalidStatusTransitionException;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationStatusTransitionTest {
    private static final long APPLICATION_ID = 100L;
    private static final long RECRUITER_ID = 25L;

    @Mock private ApplicationRepository applicationRepository;
    @Mock private JobSeekerRepository jobSeekerRepository;
    @Mock private JobRepository jobRepository;
    @Mock private RecruiterRepository recruiterRepository;
    @Mock private FindByAuthenticationService findByAuthenticationService;
    @Mock private FileStorageService fileStorageService;

    private ApplicationService applicationService;
    private User recruiterUser;
    private Recruiter recruiter;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(
                applicationRepository,
                jobSeekerRepository,
                jobRepository,
                recruiterRepository,
                findByAuthenticationService,
                fileStorageService
        );
        recruiterUser = new User();
        recruiter = new Recruiter();
        recruiter.setId(RECRUITER_ID);
        lenient().when(findByAuthenticationService.findUser()).thenReturn(recruiterUser);
        lenient().when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(recruiter));
    }

    @Test
    void allowsOnlyTheFourForwardTransitions() {
        assertTransition(ApplicationStatus.PENDING, ApplicationStatus.SHORTLISTED, true);
        assertTransition(ApplicationStatus.PENDING, ApplicationStatus.REJECTED, true);
        assertTransition(ApplicationStatus.SHORTLISTED, ApplicationStatus.ACCEPTED, true);
        assertTransition(ApplicationStatus.SHORTLISTED, ApplicationStatus.REJECTED, true);
    }

    @Test
    void rejectsAllOtherTransitionsWithoutSavingOrChangingTheCurrentStatus() {
        for (ApplicationStatus current : ApplicationStatus.values()) {
            for (ApplicationStatus requested : ApplicationStatus.values()) {
                if (isAllowed(current, requested)) {
                    continue;
                }

                Application application = prepareApplication(current);
                ApplicationStatusRequestDto request = request(requested);

                InvalidStatusTransitionException exception = assertThrows(
                        InvalidStatusTransitionException.class,
                        () -> applicationService.changeApplicationStatus(request, APPLICATION_ID),
                        current + " -> " + requested
                );

                assertEquals("Invalid application status transition from " + current + " to " + requested,
                        exception.getMessage());
                assertEquals(current, application.getStatus(), "Rejected request must leave status unchanged");
                verify(applicationRepository, never()).save(application);
                verify(applicationRepository).findByIdAndJobRecruiterId(APPLICATION_ID, RECRUITER_ID);
            }
        }
    }

    @Test
    void onlyLooksUpApplicationsOwnedByTheAuthenticatedRecruiter() {
        reset(applicationRepository);
        when(applicationRepository.findByIdAndJobRecruiterId(APPLICATION_ID, RECRUITER_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.changeApplicationStatus(
                        request(ApplicationStatus.SHORTLISTED), APPLICATION_ID));
        verify(applicationRepository, never()).save(org.mockito.ArgumentMatchers.any(Application.class));
    }

    @Test
    void seekerCanWithdrawOnlyTheirPendingApplicationAndSnapshotPathIsPreserved() {
        JobSeeker seeker = seeker();
        Application application = prepareSeekerApplication(seeker, ApplicationStatus.PENDING);
        application.setSubmittedCvPath("uploads/application-cv/original.pdf");

        var response = applicationService.withdrawApplication(APPLICATION_ID);

        assertEquals(ApplicationStatus.WITHDRAWN, application.getStatus());
        assertEquals(ApplicationStatus.WITHDRAWN, response.getApplicationStatus());
        assertEquals(APPLICATION_ID, response.getApplicationId());
        assertEquals("uploads/application-cv/original.pdf", application.getSubmittedCvPath());
        verify(applicationRepository).findByIdAndJobSeekerId(APPLICATION_ID, seeker.getId());
        verify(applicationRepository).save(application);
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void seekerCannotWithdrawApplicationsOutsidePending() {
        for (ApplicationStatus current : new ApplicationStatus[]{
                ApplicationStatus.SHORTLISTED,
                ApplicationStatus.ACCEPTED,
                ApplicationStatus.REJECTED,
                ApplicationStatus.WITHDRAWN
        }) {
            Application application = prepareSeekerApplication(seeker(), current);
            application.setSubmittedCvPath("uploads/application-cv/original.pdf");

            assertThrows(InvalidStatusTransitionException.class,
                    () -> applicationService.withdrawApplication(APPLICATION_ID), current.toString());
            assertEquals(current, application.getStatus());
            assertEquals("uploads/application-cv/original.pdf", application.getSubmittedCvPath());
            verify(applicationRepository, never()).save(application);
            verify(fileStorageService, never()).deleteApplicationCv(org.mockito.ArgumentMatchers.anyString());
        }
    }

    @Test
    void seekerCannotWithdrawAnotherSeekersApplication() {
        JobSeeker seeker = seeker();
        reset(applicationRepository);
        when(jobSeekerRepository.findByUser(recruiterUser)).thenReturn(Optional.of(seeker));
        when(applicationRepository.findByIdAndJobSeekerId(APPLICATION_ID, seeker.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.withdrawApplication(APPLICATION_ID));
        verify(applicationRepository, never()).save(org.mockito.ArgumentMatchers.any(Application.class));
    }

    @Test
    void withdrawnApplicationStillBlocksReapplyingToTheSameJob() {
        JobSeeker seeker = seeker();
        Job job = new Job();
        job.setId(72L);
        job.setDeadline(LocalDate.now().plusDays(1));
        when(jobSeekerRepository.findByUser(recruiterUser)).thenReturn(Optional.of(seeker));
        when(jobRepository.findByIdAndActiveTrue(job.getId())).thenReturn(Optional.of(job));
        when(applicationRepository.existsByJobAndJobSeeker(job, seeker)).thenReturn(true);
        ApplicationRequestDto request = new ApplicationRequestDto();
        request.setJobId(job.getId());

        assertThrows(DuplicateResourceException.class,
                () -> applicationService.createJobApplication(request));
        verify(applicationRepository, never()).save(org.mockito.ArgumentMatchers.any(Application.class));
        verify(fileStorageService, never()).copyCvForApplication(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void withdrawnApplicationRemainsInSeekerApplicationHistory() {
        reset(applicationRepository);
        reset(jobSeekerRepository);
        JobSeeker seeker = seeker();
        when(jobSeekerRepository.findByUser(recruiterUser)).thenReturn(Optional.of(seeker));
        Application application = new Application();
        application.setId(APPLICATION_ID);
        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setJobSeeker(seeker);
        Job job = new Job();
        job.setId(72L);
        job.setTitle("Java Developer");
        application.setJob(job);
        when(applicationRepository.findByJobSeekerId(seeker.getId())).thenReturn(java.util.List.of(application));

        var applications = applicationService.getAllApplications();

        assertEquals(1, applications.size());
        assertEquals(APPLICATION_ID, applications.get(0).getApplicationId());
        assertEquals(ApplicationStatus.WITHDRAWN, applications.get(0).getApplicationStatus());
    }

    @Test
    void invalidTransitionUsesConflictResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        InvalidStatusTransitionException exception = new InvalidStatusTransitionException(
                ApplicationStatus.REJECTED,
                ApplicationStatus.PENDING
        );

        ResponseEntity<ExceptionResponseDto> response = handler.handleInvalidStatusTransitionException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getStatus());
        assertEquals(exception.getMessage(), response.getBody().getMessage());
    }

    private void assertTransition(ApplicationStatus current, ApplicationStatus requested, boolean allowed) {
        Application application = prepareApplication(current);
        ApplicationStatusResponseDto response = applicationService.changeApplicationStatus(
                request(requested), APPLICATION_ID);

        assertTrue(allowed);
        assertEquals(requested, application.getStatus());
        assertEquals(requested, response.getApplicationStatus());
        verify(applicationRepository).save(application);
        verify(applicationRepository).findByIdAndJobRecruiterId(APPLICATION_ID, RECRUITER_ID);
    }

    private Application prepareApplication(ApplicationStatus current) {
        reset(applicationRepository);
        Application application = new Application();
        application.setStatus(current);
        when(applicationRepository.findByIdAndJobRecruiterId(APPLICATION_ID, RECRUITER_ID))
                .thenReturn(Optional.of(application));
        lenient().when(applicationRepository.save(application)).thenReturn(application);
        return application;
    }

    private Application prepareSeekerApplication(JobSeeker seeker, ApplicationStatus current) {
        reset(applicationRepository);
        reset(jobSeekerRepository);
        when(jobSeekerRepository.findByUser(recruiterUser)).thenReturn(Optional.of(seeker));
        Application application = new Application();
        application.setId(APPLICATION_ID);
        application.setStatus(current);
        Job job = new Job();
        job.setId(72L);
        job.setTitle("Java Developer");
        application.setJob(job);
        application.setJobSeeker(seeker);
        when(applicationRepository.findByIdAndJobSeekerId(APPLICATION_ID, seeker.getId()))
                .thenReturn(Optional.of(application));
        lenient().when(applicationRepository.save(application)).thenReturn(application);
        return application;
    }

    private JobSeeker seeker() {
        JobSeeker seeker = new JobSeeker();
        seeker.setId(35L);
        seeker.setName("Test Seeker");
        seeker.setCv("uploads/cv/current.pdf");
        return seeker;
    }

    private ApplicationStatusRequestDto request(ApplicationStatus requested) {
        ApplicationStatusRequestDto request = new ApplicationStatusRequestDto();
        request.setApplicationStatus(requested);
        return request;
    }

    private boolean isAllowed(ApplicationStatus current, ApplicationStatus requested) {
        return switch (current) {
            case PENDING -> requested == ApplicationStatus.SHORTLISTED || requested == ApplicationStatus.REJECTED;
            case SHORTLISTED -> requested == ApplicationStatus.ACCEPTED || requested == ApplicationStatus.REJECTED;
            case ACCEPTED, REJECTED, WITHDRAWN -> false;
        };
    }
}
