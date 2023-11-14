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


    /**
     * Handles the endpoint for adding a doubt request.
     *
     * @param doubtRequest The doubt request to be added.
     * @return ResponseEntity with the added doubt request.
     */
    @PostMapping
    public ResponseEntity<DoubtRequest> addDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest) {
        return ResponseEntity.ok(doubtRequestService.addDoubtRequest(doubtRequest));
    }

    /**
     * Handles the endpoint for creating a live doubt request when a tutor is available.
     *
     * @param doubtRequest The doubt request to be created.
     * @param auth         The authentication object.
     * @return ResponseEntity with the created live doubt request.
     */
    @PostMapping("/live")
    public ResponseEntity<DoubtRequest> tutorAvailableLiveDoubtRequestHandler(@RequestBody DoubtRequest doubtRequest, Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.tutorAvailableLiveDoubtRequest(doubtRequest, auth.getName()));
    }


    /**
     * Handles the endpoint for retrieving all doubt requests for a user.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of all doubt requests for the user.
     */
    @GetMapping("/all")
    public ResponseEntity<List<DoubtRequest>> getAllDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.getAllDoubtRequest(auth.getName()));
    }


    /**
     * Handles the endpoint for retrieving a doubt request by its ID.
     *
     * @param doubtRequestId The ID of the doubt request.
     * @return ResponseEntity with the doubt request.
     */
    @GetMapping("/{doubtRequestId}")
    public ResponseEntity<DoubtRequest> getDoubtRequestByIdHandler(@PathVariable Integer doubtRequestId) {
        return ResponseEntity.ok(doubtRequestService.findDoubtRequestById(doubtRequestId));
    }


    /**
     * Handles the endpoint for retrieving all resolved doubt requests for a user.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of all resolved doubt requests for the user.
     */
    @GetMapping("/resolved")
    public ResponseEntity<List<DoubtRequest>> getAllResolvedDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.getAllResolvedDoubtRequest(auth.getName()));
    }


    /**
     * Handles the endpoint for retrieving all pending doubt requests for a user.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the list of all pending doubt requests for the user.
     */
    @GetMapping("/pending")
    public ResponseEntity<List<DoubtRequest>> getAllPendingDoubtRequestHandler(Authentication auth) {
        return ResponseEntity.ok(doubtRequestService.getAllPendingDoubtRequest(auth.getName()));
    }


    /**
     * Handles the endpoint for deleting a doubt request by its ID.
     *
     * @param doubtRequestId The ID of the doubt request.
     * @return ResponseEntity with the deleted doubt request.
     */
    @DeleteMapping("/{doubtRequestId}")
    public ResponseEntity<DoubtRequest> deleteDoubtRequestByIdHandler(@PathVariable Integer doubtRequestId) {
        return ResponseEntity.ok(doubtRequestService.deleteDoubtRequestById(doubtRequestId));
    }

    /**
     * Handles the endpoint for updating unresolved doubts for a doubt request.
     *
     * @param doubtRequestId   The ID of the doubt request.
     * @param doubtDescription The updated doubt description.
     * @return ResponseEntity with the updated doubt request.
     */
    @GetMapping("/{doubtRequestId}/{doubtDescription}")
    public ResponseEntity<DoubtRequest> updateUnresolvedDoubtsHandler(@PathVariable Integer doubtRequestId, @PathVariable String doubtDescription) {
        return ResponseEntity.ok(doubtRequestService.updateUnresolvedDoubts(doubtRequestId, doubtDescription));
    }
}
