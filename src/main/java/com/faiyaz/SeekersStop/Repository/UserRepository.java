package com.faiyaz.SeekersStop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.faiyaz.SeekersStop.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>  findByUsername (String username);
    boolean existsByUsername(String username);
}
