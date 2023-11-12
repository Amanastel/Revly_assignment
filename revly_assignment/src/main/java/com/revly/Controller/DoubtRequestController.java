package com.revly.Controller;

import com.revly.Model.DoubtRequest;
import com.revly.Service.DoubtRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoubtRequestController {

    private final DoubtRequestService doubtRequestService;


    @Autowired
    public DoubtRequestController(DoubtRequestService doubtRequestService) {
        this.doubtRequestService = doubtRequestService;
    }



    @PostMapping("/user/{userId}/doubtRequest")
    public ResponseEntity<DoubtRequest> addDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest, @PathVariable Integer userId) {
        return ResponseEntity.ok(doubtRequestService.addDoubtRequest(doubtRequest, userId));
    }

    @PostMapping("/user/{userId}/doubtRequest/live")
    public ResponseEntity<DoubtRequest> tutorAvailableLiveDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest, @PathVariable Integer userId) {
        return ResponseEntity.ok(doubtRequestService.tutorAvailableLiveDoubtRequest(doubtRequest, userId));
    }
}
