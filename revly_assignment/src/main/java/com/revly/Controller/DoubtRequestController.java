package com.revly.Controller;

import com.revly.Model.DoubtRequest;
import com.revly.Service.DoubtRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/doubtRequest")
public class DoubtRequestController {

    private final DoubtRequestService doubtRequestService;


    @Autowired
    public DoubtRequestController(DoubtRequestService doubtRequestService) {
        this.doubtRequestService = doubtRequestService;
    }



    @PostMapping("/{userId}")
    public ResponseEntity<DoubtRequest> addDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest, @PathVariable Integer userId) {
        return ResponseEntity.ok(doubtRequestService.addDoubtRequest(doubtRequest, userId));
    }

    @PostMapping("/{userId}/live")
    public ResponseEntity<DoubtRequest> tutorAvailableLiveDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest, @PathVariable Integer userId) {
        return ResponseEntity.ok(doubtRequestService.tutorAvailableLiveDoubtRequest(doubtRequest, userId));
    }
}
