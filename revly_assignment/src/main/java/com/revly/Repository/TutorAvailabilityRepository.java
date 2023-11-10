package com.revly.Repository;

import com.revly.Model.TutorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorAvailabilityRepository extends JpaRepository<TutorAvailability, Integer> {
    public List<TutorAvailability> findByAvailabilityStatus(String availabilityStatus);
}
