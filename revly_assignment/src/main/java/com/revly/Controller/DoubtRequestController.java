package com.revly.Controller;

import com.revly.Model.DoubtRequest;
import com.revly.Service.DoubtRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/doubtRequest")
public class DoubtRequestController {

    private final DoubtRequestService doubtRequestService;


    @Autowired
    public DoubtRequestController(DoubtRequestService doubtRequestService) {
        this.doubtRequestService = doubtRequestService;
    }



    @PostMapping
    public ResponseEntity<DoubtRequest> addDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest) {
        return ResponseEntity.ok(doubtRequestService.addDoubtRequest(doubtRequest));
    }

    @PostMapping("/live")
    public ResponseEntity<DoubtRequest> tutorAvailableLiveDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest, Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(doubtRequestService.tutorAvailableLiveDoubtRequest(doubtRequest, auth.getName()));
    }
}
