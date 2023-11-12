package com.revly.Repository;

import com.revly.Model.AvailabilityStatus;
import com.revly.Model.TutorAvailability;
import com.revly.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TutorAvailabilityRepository extends JpaRepository<TutorAvailability, Integer> {
    public Optional<TutorAvailability> findByTutor(User tutor);
    public List<TutorAvailability> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);
    public List<TutorAvailability> findByLastPingTimeAfter(LocalDateTime lastPingTime);

}
