package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Model.AvailabilityStatus;
import com.revly.Model.TutorAvailability;
import com.revly.Model.Users;
import com.revly.Model.UserType;
import com.revly.Repository.DoubtRequestRepository;
import com.revly.Repository.TutorAvailabilityRepository;
import com.revly.Repository.UserRepository;
import com.revly.Service.TutorAvailabilityService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TutorAvailabilityServiceImpl implements TutorAvailabilityService  {

    private final TutorAvailabilityRepository tutorAvailabilityRepository;

    private final UserRepository userRepository;
    private final DoubtRequestRepository doubtRequestRepository;

    @Autowired
    public TutorAvailabilityServiceImpl(TutorAvailabilityRepository tutorAvailabilityRepository, UserRepository userRepository, DoubtRequestRepository doubtRequestRepository) {
        this.tutorAvailabilityRepository = tutorAvailabilityRepository;
        this.userRepository = userRepository;
        this.doubtRequestRepository = doubtRequestRepository;
    }

    @Override
    @Transactional
    public TutorAvailability addTutorAvailability(String email) {
        Users tutor = userRepository.findByEmail(email)
                .filter(user -> Objects.equals(user.getUserType().toUpperCase(), "ROLE_TUTOR"))
                .orElseThrow(() -> new UserException("Tutor not found"));

        Optional<TutorAvailability> existingAvailability = tutorAvailabilityRepository.findByTutor(tutor);

        if (existingAvailability.isPresent()) {
            // If tutorAvailability already exists, check if it's already AVAILABLE
            TutorAvailability tutorAvailability = existingAvailability.get();
            if (tutorAvailability.getAvailabilityStatus() == AvailabilityStatus.UNAVAILABLE) {
                // If tutorAvailability is UNAVAILABLE, update its status to AVAILABLE
                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                tutorAvailability.setLastPingTime(LocalDateTime.now());
                return tutorAvailabilityRepository.save(tutorAvailability);
            } else {
                // If tutorAvailability is already AVAILABLE, throw an exception
                throw new UserException("Tutor is already marked as AVAILABLE");
            }
        } else {
            // If tutorAvailability doesn't exist, create a new one
            TutorAvailability newTutorAvailability = new TutorAvailability();
            newTutorAvailability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            newTutorAvailability.setLastPingTime(LocalDateTime.now());
            newTutorAvailability.setTutor(tutor);
            return tutorAvailabilityRepository.save(newTutorAvailability);
        }
    }

    @Override
    public List<TutorAvailability> availableTutors() {
        List<TutorAvailability> availableTutors = tutorAvailabilityRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        if (availableTutors.isEmpty()) {
            throw new UserException("No tutors available");
        } else {
            return availableTutors;
        }
    }

    @Override
    public List<TutorAvailability> unavailableTutors() {
        List<TutorAvailability> unavailableTutors = tutorAvailabilityRepository.findByAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
        if (unavailableTutors.isEmpty()) {
            throw new UserException("No tutors unavailable");
        } else {
            return unavailableTutors;
        }
    }


    @Scheduled(fixedRate = 3000) // Run every minute, adjust the cron expression as needed
    public void updateLastPingTimeForAvailableTutors() {
        List<TutorAvailability> availableTutors = tutorAvailabilityRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        LocalDateTime currentTime = LocalDateTime.now();

        for (TutorAvailability tutorAvailability : availableTutors) {
            tutorAvailability.setLastPingTime(currentTime);
            tutorAvailabilityRepository.save(tutorAvailability);
        }
    }


//    @Override
//    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
//        // This method will be called when the application is terminated
////        markTutorAway(String tutorEmail);
//
//    }

    public void markTutorAway(String tutorEmail) {
        Optional<Users> tutorOptional = userRepository.findByEmail(tutorEmail)
                .filter(user -> Objects.equals(user.getUserType(), "ROLE_TUTOR"));

        if (tutorOptional.isPresent()) {
            Users tutor = tutorOptional.get();
            Optional<TutorAvailability> tutorAvailabilityOptional = tutorAvailabilityRepository.findByTutor(tutor);

            if (tutorAvailabilityOptional.isPresent()) {
                TutorAvailability tutorAvailability = tutorAvailabilityOptional.get();
                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.AWAY);
                tutorAvailabilityRepository.save(tutorAvailability);
            }
        }
    }


    @Scheduled(fixedRate = 1000)
    public int countOnlineTutors() {
        LocalDateTime currentTime = LocalDateTime.now().minusSeconds(3);
        int onlineTutorCount = tutorAvailabilityRepository.countAvailableByLastPingTimeAfter(currentTime, LocalDateTime.now());
        if (onlineTutorCount > 0) {
            System.out.println("Number of online tutors: " + onlineTutorCount);
            return onlineTutorCount;
        }else{
            throw new UserException("No tutors available");
        }



    }





}

