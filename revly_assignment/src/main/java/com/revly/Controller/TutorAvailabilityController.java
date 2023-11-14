package com.revly.Controller;

import com.revly.Model.TutorAvailability;
import com.revly.Service.TutorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/tutorAvailability")
public class TutorAvailabilityController {

    public final TutorAvailabilityService tutorAvailabilityService;

    @Autowired
    public TutorAvailabilityController(TutorAvailabilityService tutorAvailabilityService) {
        this.tutorAvailabilityService = tutorAvailabilityService;
    }

    /**
     * Handles the endpoint for adding tutor availability.
     *
     * @param email The email of the tutor.
     * @return ResponseEntity with the added tutor availability.
     */
    @PostMapping("/addTutorAvailability/{email}")
    public ResponseEntity<TutorAvailability> addTutorAvailabilityHandler(@PathVariable String email) {
        return ResponseEntity.ok(tutorAvailabilityService.addTutorAvailability(email));
    }

    /**
     * Handles the endpoint for retrieving available tutors.
     *
     * @return ResponseEntity with the list of available tutors.
     */
    @GetMapping("/availableTutors")
    public ResponseEntity<List<TutorAvailability>> availableTutorsHandler() {
        return ResponseEntity.ok(tutorAvailabilityService.availableTutors());
    }

    /**
     * Handles the endpoint for retrieving unavailable tutors.
     *
     * @return ResponseEntity with the list of unavailable tutors.
     */
    @GetMapping("/unavailableTutors")
    public ResponseEntity<List<TutorAvailability>> unavailableTutorsHandler() {
        return ResponseEntity.ok(tutorAvailabilityService.unavailableTutors());
    }

    /**
     * Handles the endpoint for counting online tutors.
     *
     * @return The count of online tutors.
     */
    @GetMapping("/countOnlineTutors")
    public int countOnlineTutorsHandler() {
        return tutorAvailabilityService.countOnlineTutors();
    }

    /**
     * Handles the endpoint for retrieving all tutor availabilities based on the student's email.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of tutor availabilities for the student.
     */
    @GetMapping("/getAllTutorAvailabilityByStudent")
    public ResponseEntity<List<TutorAvailability>> getAllTutorAvailabilityByStudentEmailHandler(Authentication auth) {
        return ResponseEntity.ok(tutorAvailabilityService.getAllTutorAvailabilityByStudentEmail(auth.getName()));
    }
}
