package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Model.AvailabilityStatus;
import com.revly.Model.TutorAvailability;
import com.revly.Model.User;
import com.revly.Model.UserType;
import com.revly.Repository.DoubtRequestRepository;
import com.revly.Repository.TutorAvailabilityRepository;
import com.revly.Repository.UserRepository;
import com.revly.Service.TutorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TutorAvailabilityServiceImpl implements TutorAvailabilityService , ApplicationListener<ContextClosedEvent> {

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
    public TutorAvailability addTutorAvailability(String email) {
        User tutor = userRepository.findByEmail(email)
                .filter(user -> user.getUserType() == UserType.TUTOR)
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


    @Scheduled(cron = "0 * * * * *") // Run every minute, adjust the cron expression as needed
    public void updateLastPingTimeForAvailableTutors() {
        List<TutorAvailability> availableTutors = tutorAvailabilityRepository
                .findByAvailabilityStatus("AVAILABLE");

        LocalDateTime currentTime = LocalDateTime.now();

        for (TutorAvailability tutorAvailability : availableTutors) {
            tutorAvailability.setLastPingTime(currentTime);
            tutorAvailabilityRepository.save(tutorAvailability);
        }
    }


    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        // This method will be called when the application is terminated
//        markTutorAway(String tutorEmail);

    }

    public void markTutorAway(String tutorEmail) {
        Optional<User> tutorOptional = userRepository.findByEmail(tutorEmail)
                .filter(user -> user.getUserType() == UserType.TUTOR);

        if (tutorOptional.isPresent()) {
            User tutor = tutorOptional.get();
            Optional<TutorAvailability> tutorAvailabilityOptional = tutorAvailabilityRepository.findByTutor(tutor);

            if (tutorAvailabilityOptional.isPresent()) {
                TutorAvailability tutorAvailability = tutorAvailabilityOptional.get();
                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.AWAY);
                tutorAvailabilityRepository.save(tutorAvailability);
            }
        }
    }



    /*
    @Scheduled(cron = "0 * * * * *") // Run every minute, adjust the cron expression as needed
public void updateTutorAvailabilityStatus(String email) {
    LocalDateTime currentTime = LocalDateTime.now().minusSeconds(3);

    Optional<User> tutorOptional = userRepository.findByEmail(email)
            .filter(user -> user.getUserType() == UserType.TUTOR);

    if (tutorOptional.isPresent()) {
        User tutor = tutorOptional.get();

        // Check if the tutor is available
        Optional<TutorAvailability> tutorAvailabilityOptional = tutorAvailabilityRepository.findByTutor(tutor);

        tutorAvailabilityOptional.ifPresent(tutorAvailability -> {
            if (tutorAvailability.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                // Tutor is available, update the availability status
                List<TutorAvailability> onlineTutors = tutorAvailabilityRepository.findByLastPingTimeAfter(currentTime);

                for (TutorAvailability onlineTutor : onlineTutors) {
                    if (onlineTutor.getTutor().equals(tutor)) {
                        onlineTutor.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                        tutorAvailabilityRepository.save(onlineTutor);
                    }
                }

                // Set availability status to UNAVAILABLE for tutors not updated in the last 3 seconds
                List<TutorAvailability> offlineTutors = tutorAvailabilityRepository.findByLastPingTimeBefore(currentTime);
                for (TutorAvailability offlineTutor : offlineTutors) {
                    if (offlineTutor.getTutor().equals(tutor)) {
                        offlineTutor.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
                        tutorAvailabilityRepository.save(offlineTutor);
                    }
                }
            }
        });
    }
}
     */

/*



    @Scheduled(cron = "0 * * * * *") // Run every minute, adjust the cron expression as needed
    public void updateTutorAvailabilityStatus(String email) {
        Optional<User> tutorOptional = userRepository.findByEmail(email)
                .filter(user -> user.getUserType() == UserType.TUTOR);

        if (tutorOptional.isPresent()) {
            User tutor = tutorOptional.get();
            TutorAvailability tutorAvailability = tutorAvailabilityRepository.findByTutor(tutor)
                    .orElse(new TutorAvailability());

            LocalDateTime currentTime = LocalDateTime.now();

            // Update lastPingTime
            tutorAvailability.setLastPingTime(currentTime);

            // Check and update availability status
            if (tutorAvailability.getLastPingTime() != null && isWithin3SecondsWindow(tutorAvailability.getLastPingTime(), currentTime)) {
                // Tutor is online
                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            } else {
                // Tutor is offline
                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
            }

            tutorAvailabilityRepository.save(tutorAvailability);
        } else {
            throw new UserException("Tutor not found");
        }
    }

    private boolean isWithin3SecondsWindow(LocalDateTime lastPingTime, LocalDateTime currentTime) {
        return Duration.between(lastPingTime, currentTime).abs().getSeconds() <= 3;
    }


 */




}

