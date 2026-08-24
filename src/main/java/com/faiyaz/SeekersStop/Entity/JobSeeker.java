package com.faiyaz.SeekersStop.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skill;
    private String name;
    private String contact;
    private String cv;
    private String experience;

    @OneToOne
    @JoinColumn(name = "user_id",unique = true)
    private User user;
}
