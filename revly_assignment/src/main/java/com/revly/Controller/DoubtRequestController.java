package com.revly.Controller;

import com.revly.Model.DoubtRequest;
import com.revly.Service.DoubtRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(doubtRequestService.tutorAvailableLiveDoubtRequest(doubtRequest, auth.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoubtRequest>> getAllDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.getAllDoubtRequest(auth.getName()));
    }

    @GetMapping("/{doubtRequestId}")
    public ResponseEntity<DoubtRequest> getDoubtRequestByIdHandler(@PathVariable Integer doubtRequestId) {
        return ResponseEntity.ok(doubtRequestService.findDoubtRequestById(doubtRequestId));
    }

    @GetMapping("/resolved")
    public ResponseEntity<List<DoubtRequest>> getAllResolvedDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.getAllResolvedDoubtRequest(auth.getName()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DoubtRequest>> getAllPendingDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.getAllPendingDoubtRequest(auth.getName()));
    }

    @DeleteMapping("/{doubtRequestId}")
    public ResponseEntity<DoubtRequest> deleteDoubtRequestByIdHandler(@PathVariable Integer doubtRequestId) {
        return ResponseEntity.ok(doubtRequestService.deleteDoubtRequestById(doubtRequestId));
    }

    @GetMapping("/{doubtRequestId}/{doubtDescription}")
    public ResponseEntity<DoubtRequest> updateUnresolvedDoubtsHandler(@PathVariable Integer doubtRequestId, @PathVariable String doubtDescription) {
        return ResponseEntity.ok(doubtRequestService.updateUnresolvedDoubts(doubtRequestId, doubtDescription));
    }
}
