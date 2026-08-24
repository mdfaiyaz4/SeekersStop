package com.faiyaz.SeekersStop.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
public class Job {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
        private String description;
        private String experience;
        private String qualification;
        private Double salary;
        private String location;
        private LocalDate deadline;
        private Boolean active;

        @ManyToOne
        @JoinColumn(name = "recruiter_id")
        private Recruiter recruiter;

        @ManyToOne
        @JoinColumn(name = "company_id")
        private Company company;

    }

