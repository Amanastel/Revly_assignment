package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Exception.TutorAvailabilityException;
import com.revly.Model.*;
import com.revly.Repository.DoubtRequestRepository;
import com.revly.Repository.TutorAvailabilityRepository;
import com.revly.Repository.UserRepository;
import com.revly.Service.DoubtRequestService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DoubtRequestServiceImpl implements DoubtRequestService {

    private final DoubtRequestRepository doubtRequestRepository;
    private final UserRepository userRepository;

    private final TutorAvailabilityRepository tutorAvailabilityRepository;

    @Autowired
    public DoubtRequestServiceImpl(DoubtRequestRepository doubtRequestRepository, UserRepository userRepository, TutorAvailabilityRepository tutorAvailabilityRepository) {
        this.doubtRequestRepository = doubtRequestRepository;
        this.userRepository = userRepository;
        this.tutorAvailabilityRepository = tutorAvailabilityRepository;
    }



    /**
     * Adds a doubt request for a student.
     *
     * @param doubtRequest The DoubtRequest object containing the doubt details.
     * @return The saved DoubtRequest.
     * @throws UserException if the user is not a student.
     */
    @Override
    @Transactional
    public DoubtRequest addDoubtRequest(DoubtRequest doubtRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Email: " + email);
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found"));
        if(user.getUserType().equals("ROLE_STUDENT")) {
            doubtRequest.setDoubtDescription("Student doubt description: " + doubtRequest.getDoubtDescription());
            doubtRequest.setStudent(user);
            doubtRequest.setDoubtSubject(user.getUserLanguage());
            doubtRequest.setDoubtResolved(DoubtResolved.UNRESOLVED);
            doubtRequest.setTimestamp(LocalDateTime.now());
            return doubtRequestRepository.save(doubtRequest);
        }
        else {
            throw new UserException("Only students can add doubt requests");
        }
    }


    /**
     * Creates a live doubt request for a student, assigning an available tutor.
     *
     * @param doubtRequest The DoubtRequest object containing the doubt details.
     * @param email        The email of the student making the request.
     * @return The saved DoubtRequest.
     * @throws UserException          if the user is a tutor.
     * @throws TutorAvailabilityException if no available tutors are found.
     * @throws UserException          if no matching tutor is available.
     */
    @Override
    @Transactional
    public DoubtRequest tutorAvailableLiveDoubtRequest(DoubtRequest doubtRequest, String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found"));

        if (user.getUserType().equals("ROLE_TUTOR")) {
            throw new UserException("Only students can create doubt requests");
        }

        if (user.getUserType().equals("ROLE_STUDENT")) {
            List<TutorAvailability> availableTutors = tutorAvailabilityRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE);

            if (availableTutors.isEmpty()) {
                throw new TutorAvailabilityException("No tutors available");
            }

            // Use Stream API to find the first tutor with matching subject expertise
            TutorAvailability selectedTutor = availableTutors.stream()
                    .filter(tutor -> tutor.getTutor().getSubjectExpertise().equals(user.getUserLanguage())) // Assuming userLanguage is the subject for the student
                    .findFirst()
                    .orElseThrow(() -> new UserException("No matching tutor available"));

            selectedTutor.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            tutorAvailabilityRepository.save(selectedTutor);

            doubtRequest.setDoubtDescription("Student doubt description: " + doubtRequest.getDoubtDescription());
            doubtRequest.setDoubtResolved(DoubtResolved.UNRESOLVED);
            doubtRequest.setStudent(user);
            doubtRequest.setDoubtSubject(user.getUserLanguage());
            doubtRequest.setTutor(selectedTutor.getTutor());
            doubtRequest.setTimestamp(LocalDateTime.now());

            return doubtRequestRepository.save(doubtRequest);
        }else{
            throw new UserException("Only students can create doubt requests");
        }

    }

    /**
     * Retrieves a doubt request by its ID.
     *
     * @param doubtRequestId The ID of the doubt request to retrieve.
     * @return The DoubtRequest object.
     * @throws UserException if the doubt request is not found.
     */
    @Override
    public DoubtRequest findDoubtRequestById(Integer doubtRequestId) {
        return doubtRequestRepository.findById(doubtRequestId).orElseThrow(() -> new UserException("DoubtRequest not found"));
    }


    /**
     * Updates the description of an unresolved doubt request.
     *
     * @param doubtRequestId    The ID of the doubt request to update.
     * @param doubtDescription The new description to set.
     * @return The updated DoubtRequest.
     * @throws UserException if the doubt request is not found or is already resolved.
     */
    @Override
    public DoubtRequest updateUnresolvedDoubts(Integer doubtRequestId, String doubtDescription) {
        DoubtRequest doubtRequest = doubtRequestRepository.findById(doubtRequestId).orElseThrow(() -> new UserException("DoubtRequest not found"));
        if (doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED) {
//            doubtRequest.setDoubtDescription(doubtRequest.getDoubtDescription() + "\n" + "Student doubt description: " + doubtDescription);
            doubtRequest.setDoubtDescription("Student doubt description: " + doubtDescription);
            doubtRequest.setTimestamp(LocalDateTime.now());
            return doubtRequestRepository.save(doubtRequest);
        } else {
            throw new UserException("Doubt is already resolved");
        }
    }


    /**
     * Deletes a doubt request by its ID.
     *
     * @param doubtRequestId The ID of the doubt request to delete.
     * @return The deleted DoubtRequest.
     * @throws UserException if the doubt request is not found.
     */
    @Override
    public DoubtRequest deleteDoubtRequestById(Integer doubtRequestId) {
        DoubtRequest doubtRequest = doubtRequestRepository.findById(doubtRequestId).orElseThrow(() -> new UserException("DoubtRequest not found"));
        if (doubtRequest.getTutor()!=null){
            TutorAvailability tutorAvailability = tutorAvailabilityRepository.findByTutor(doubtRequest.getTutor()).orElseThrow(() -> new UserException("Tutor not found"));
            tutorAvailability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            tutorAvailabilityRepository.save(tutorAvailability);
        }
        doubtRequestRepository.delete(doubtRequest);
        return doubtRequest;
    }

    /**
     * Retrieves all doubt requests for a given student.
     *
     * @param email The email of the student.
     * @return List of DoubtRequest objects for the student.
     * @throws UserException if the user is not found.
     */
    @Override
    public List<DoubtRequest> getAllDoubtRequest(String email) {
        return doubtRequestRepository.findByStudent(userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found")));
    }

    /**
     * Retrieves all resolved doubt requests for a given student.
     *
     * @param email The email of the student.
     * @return List of resolved DoubtRequest objects for the student.
     * @throws UserException if the user is not found.
     */
    @Override
    public List<DoubtRequest> getAllResolvedDoubtRequest(String email) {
        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByStudent(userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found")));
        return doubtRequests.stream().filter(doubtRequest -> doubtRequest.getDoubtResolved() == DoubtResolved.RESOLVED).toList();
    }

    /**
     * Retrieves all unresolved doubt requests for a given student.
     *
     * @param email The email of the student.
     * @return List of unresolved DoubtRequest objects for the student.
     * @throws UserException if the user is not found.
     */
    @Override
    public List<DoubtRequest> getAllPendingDoubtRequest(String email) {
        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByStudent(userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found")));
        return doubtRequests.stream().filter(doubtRequest -> doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED).toList();
    }


}
