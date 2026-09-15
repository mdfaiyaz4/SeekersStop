package com.faiyaz.SeekersStop.Specification;

import com.faiyaz.SeekersStop.Entity.Job;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> hasLocation(String location){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("location"),location);
    }
    public static Specification<Job> hasTitle(String title){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("title"),"%" + title + "%");
    }
    public static Specification<Job> isActive(){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("active"),true);
    }
    public static Specification<Job> hasExperience(String experience){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("experience"),experience);
    }

}
