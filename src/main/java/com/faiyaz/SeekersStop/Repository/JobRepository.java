package com.faiyaz.SeekersStop.Repository;

import com.faiyaz.SeekersStop.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job,Long> {
    public List<Job> findByActiveTrue();
    public Optional<Job> findByIdAndActiveTrue(Long id);
}
