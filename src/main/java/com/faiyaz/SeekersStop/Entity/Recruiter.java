package com.faiyaz.SeekersStop.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contactInfo;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true,nullable = false)
    private User user;
}
