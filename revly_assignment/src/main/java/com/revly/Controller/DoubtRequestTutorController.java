package com.revly.Controller;

import com.revly.Model.DoubtRequest;
import com.revly.Service.DoubtRequestService;
import com.revly.Service.DoubtRequestServiceTutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{doubtRequestId}/solve/{tutorId}")
    public ResponseEntity<DoubtRequest> solveDoubtRequestHandler(@PathVariable Integer doubtRequestId, @PathVariable Integer tutorId , @RequestParam("solutionDescription") String solutionDescription) {
        return ResponseEntity.ok(doubtRequestTutor.solveDoubt(doubtRequestId, solutionDescription, tutorId));
    }
}
