package com.faiyaz.SeekersStop.Repository;

import com.faiyaz.SeekersStop.Entity.Application;
import com.faiyaz.SeekersStop.Entity.Company;
import com.faiyaz.SeekersStop.Entity.Job;
import com.faiyaz.SeekersStop.Entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    public boolean existsByJobAndJobSeeker(Job job,JobSeeker jobSeeker);
    public List<Application> findByJobRecruiterId(Long recruiterId);
    public Optional<Application> findByIdAndJobRecruiterId(Long applicationId,Long recruiterId);
    public Optional<Application> findByIdAndJobSeekerId(Long applicationId,Long jobseekerId);
    public List<Application> findByJobSeekerId(Long jobseekerId);
}
