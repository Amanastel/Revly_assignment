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

    /**
     * Handles the endpoint for solving a doubt request by a tutor.
     *
     * @param doubtRequestId     The ID of the doubt request.
     * @param auth               The authentication object.
     * @param solutionDescription The solution description provided by the tutor.
     * @return ResponseEntity with the solved doubt request.
     */
    @GetMapping("/{doubtRequestId}/solve")
    public ResponseEntity<DoubtRequest> solveDoubtRequestHandler(@PathVariable Integer doubtRequestId, Authentication auth, @RequestParam("solutionDescription") String solutionDescription) {
        return ResponseEntity.ok(doubtRequestTutor.solveDoubt(doubtRequestId, solutionDescription, auth.getName()));
    }

    /**
     * Handles the endpoint for accepting a doubt request by a tutor.
     *
     * @param doubtRequestId The ID of the doubt request.
     * @param auth           The authentication object.
     * @return ResponseEntity with the accepted doubt request.
     */
    @GetMapping("/{doubtRequestId}/accept")
    public ResponseEntity<DoubtRequest> acceptDoubtRequestHandler(@PathVariable Integer doubtRequestId, Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.acceptDoubtRequest(doubtRequestId, auth.getName()));
    }

    /**
     * Handles the endpoint for retrieving all pending doubt requests for a tutor.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of all pending doubt requests for the tutor.
     */
    @GetMapping("/pending")
    public ResponseEntity<List<DoubtRequest>> allPendingDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.allPendingDoubtRequest(auth.getName()));
    }

    /**
     * Handles the endpoint for checking doubt requests based on the tutor's expertise.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of doubt requests matching the tutor's expertise.
     */
    @GetMapping("/doubtRequestBasedOnSubject")
    public ResponseEntity<List<DoubtRequest>> checkDoubtRequestBasedOnExpertiseHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.checkDoubtRequestBasedOnExpertise(auth.getName()));
    }

    /**
     * Handles the endpoint for retrieving all doubt requests for a tutor.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of all doubt requests for the tutor.
     */
    @GetMapping("/all")
    public ResponseEntity<List<DoubtRequest>> getAllDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestTutor.getAllDoubtRequest(auth.getName()));
    }
}
