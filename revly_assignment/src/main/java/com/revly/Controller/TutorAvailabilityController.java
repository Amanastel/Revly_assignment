package com.revly.Controller;

import com.revly.Model.TutorAvailability;
import com.revly.Service.TutorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/addTutorAvailability/{email}")
    public ResponseEntity<TutorAvailability> addTutorAvailabilityHandler(@PathVariable String email) {
        return ResponseEntity.ok(tutorAvailabilityService.addTutorAvailability(email));
    }


    @GetMapping("/availableTutors")
    public ResponseEntity<List<TutorAvailability>> availableTutorsHandler() {
        return ResponseEntity.ok(tutorAvailabilityService.availableTutors());
    }
}
