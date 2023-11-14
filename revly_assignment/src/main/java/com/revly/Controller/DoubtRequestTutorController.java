package com.revly.Controller;

import com.revly.Model.DoubtRequest;
import com.revly.Service.DoubtRequestService;
import com.revly.Service.DoubtRequestServiceTutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/doubtRequest/tutor")
public class DoubtRequestTutorController {

    private final DoubtRequestService doubtRequestService;
    private final DoubtRequestServiceTutor doubtRequestTutor;

    @Autowired
    public DoubtRequestTutorController(DoubtRequestService doubtRequestService, DoubtRequestServiceTutor doubtRequestTutor) {
        this.doubtRequestService = doubtRequestService;
        this.doubtRequestTutor = doubtRequestTutor;
    }

    @GetMapping("/{doubtRequestId}/solve")
    public ResponseEntity<DoubtRequest> solveDoubtRequestHandler(@PathVariable Integer doubtRequestId, Authentication auth, @RequestParam("solutionDescription") String solutionDescription) {
        return ResponseEntity.ok(doubtRequestTutor.solveDoubt(doubtRequestId, solutionDescription, auth.getName()));
    }

    @GetMapping("/{doubtRequestId}/accept")
    public ResponseEntity<DoubtRequest> acceptDoubtRequestHandler(@PathVariable Integer doubtRequestId, Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.acceptDoubtRequest(doubtRequestId, auth.getName()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DoubtRequest>> allPendingDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.allPendingDoubtRequest(auth.getName()));
    }

    @GetMapping("/doubtRequestBasedOnSubject")
    public ResponseEntity<List<DoubtRequest>> checkDoubtRequestBasedOnExpertiseHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.checkDoubtRequestBasedOnExpertise(auth.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoubtRequest>> getAllDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.getAllDoubtRequest(auth.getName()));
    }
}
