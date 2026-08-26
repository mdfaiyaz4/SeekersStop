package com.faiyaz.SeekersStop.Repository;

import com.faiyaz.SeekersStop.Entity.JobSeeker;
import com.faiyaz.SeekersStop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker,Long> {
    public Optional<JobSeeker> findByUser(User user);
    boolean existsByUser(User user);
}
