package com.revly.Repository;

import com.revly.Model.AvailabilityStatus;
import com.revly.Model.TutorAvailability;
import com.revly.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TutorAvailabilityRepository extends JpaRepository<TutorAvailability, Integer> {
    public Optional<TutorAvailability> findByTutor(Users tutor);
    public List<TutorAvailability> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);
    @Query("SELECT COUNT(ta) FROM TutorAvailability ta WHERE ta.lastPingTime BETWEEN :startTime AND :endTime")
    public int countAvailableByLastPingTimeAfter(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


}
