package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Model.*;
import com.revly.Repository.DoubtRequestRepository;
import com.revly.Repository.TutorAvailabilityRepository;
import com.revly.Repository.UserRepository;
import com.revly.Service.DoubtRequestServiceTutor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoubtRequestServiceTutorImpl implements DoubtRequestServiceTutor {

    private final DoubtRequestRepository doubtRequestRepository;
    private final UserRepository userRepository;

    private final TutorAvailabilityRepository tutorAvailabilityRepository;

    @Autowired
    public DoubtRequestServiceTutorImpl(DoubtRequestRepository doubtRequestRepository, UserRepository userRepository, TutorAvailabilityRepository tutorAvailabilityRepository) {
        this.doubtRequestRepository = doubtRequestRepository;
        this.userRepository = userRepository;
        this.tutorAvailabilityRepository = tutorAvailabilityRepository;
    }


    /**
     * Solves a doubt request, marking it as resolved and adding the tutor's solution.
     *
     * @param doubtRequestId     The ID of the doubt request to solve.
     * @param solutionDescription The solution provided by the tutor.
     * @param email              The email of the tutor solving the doubt.
     * @return The updated DoubtRequest.
     * @throws UserException if the doubt request is not found, already resolved, or tutor expertise does not match the doubt subject.
     */
    @Override
    @Transactional
    public DoubtRequest solveDoubt(Integer doubtRequestId, String solutionDescription, String email) {
        DoubtRequest doubtRequest = doubtRequestRepository.findById(doubtRequestId)
                .orElseThrow(() -> new UserException("DoubtRequest not found"));

        Users tutor = userRepository.findByEmail(email)
                .filter(user -> Objects.equals(user.getUserType(), "ROLE_TUTOR"))
                .orElseThrow(() -> new UserException("Tutor not found"));

        // Additional check: Ensure the tutor's expertise matches the doubt subject
        if (!doubtRequest.getDoubtSubject().equals(tutor.getSubjectExpertise())) {
            throw new UserException("Tutor expertise does not match the doubt subject");
        }


        if (doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED) {
            doubtRequest.setDoubtResolved(DoubtResolved.RESOLVED);
            doubtRequest.setDoubtDescription(doubtRequest.getDoubtDescription() + "\nTutor solution: " + solutionDescription);
            tutorAvailabilityRepository.findByTutor(tutor).ifPresent(tutorAvailability -> {
                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                tutorAvailability.setLastPingTime(LocalDateTime.now());
                tutorAvailabilityRepository.save(tutorAvailability);
            });
            doubtRequest.setTutor(tutor);
            doubtRequest.setTimestamp(LocalDateTime.now());

            // Save the updated DoubtRequest
            return doubtRequestRepository.save(doubtRequest);
        } else {
            throw new UserException("Doubt is already resolved");
        }
    }

    /**
     * Accepts a pending doubt request, marking it as unresolved and assigning the tutor.
     *
     * @param doubtRequestId The ID of the doubt request to accept.
     * @param email          The email of the tutor accepting the doubt request.
     * @return The updated DoubtRequest.
     * @throws UserException if the doubt request is not found, already resolved, or tutor expertise does not match the doubt subject.
     */
    @Override
    public DoubtRequest acceptDoubtRequest(Integer doubtRequestId, String email) {
        DoubtRequest doubtRequest = doubtRequestRepository.findById(doubtRequestId)
                .orElseThrow(() -> new UserException("DoubtRequest not found"));

        Users tutor = userRepository.findByEmail(email)
                .filter(user -> Objects.equals(user.getUserType(), "ROLE_TUTOR"))
                .orElseThrow(() -> new UserException("Tutor not found"));

        // Additional check: Ensure the tutor's expertise matches the doubt subject
        if (!doubtRequest.getDoubtSubject().equals(tutor.getSubjectExpertise())) {
            throw new UserException("Tutor expertise does not match the doubt subject");
        }

        if (doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED) {
            doubtRequest.setDoubtResolved(DoubtResolved.UNRESOLVED);
            doubtRequest.setTutor(tutor);
            doubtRequest.setTimestamp(LocalDateTime.now());

            // Save the updated DoubtRequest
            return doubtRequestRepository.save(doubtRequest);
        } else {
            throw new UserException("Doubt is already resolved");
        }
    }


    /**
     * Retrieves all pending doubt requests for a tutor.
     *
     * @param email The email of the tutor.
     * @return List of pending DoubtRequest objects for the tutor.
     * @throws UserException if the user is not a tutor or no pending doubt requests are found.
     */
    @Override
    public List<DoubtRequest> allPendingDoubtRequest(String email) {
        Users tutor = userRepository.findByEmail(email)
                .filter(user -> Objects.equals(user.getUserType(), "ROLE_TUTOR"))
                .orElseThrow(() -> new UserException("Tutor not found"));

        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByTutor(tutor).stream()
                .filter(doubtRequest -> doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED)
                .toList();
        if (doubtRequests.isEmpty()) {
            throw new UserException("No pending doubt requests for this tutor");
        } else {
            return doubtRequests;
        }
    }

    /**
     * Checks and retrieves all pending doubt requests for a tutor based on expertise.
     *
     * @param email The email of the tutor.
     * @return List of pending DoubtRequest objects for the tutor based on expertise.
     * @throws UserException if the user is not a tutor or no pending doubt requests are found.
     */
    @Override
    public List<DoubtRequest> checkDoubtRequestBasedOnExpertise(String email) {
        Users tutor = userRepository.findByEmail(email)
                .filter(user -> Objects.equals(user.getUserType(), "ROLE_TUTOR"))
                .orElseThrow(() -> new UserException("Tutor not found"));

        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByDoubtSubject(tutor.getSubjectExpertise()).stream()
                .filter(doubtRequest -> doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED)
                .filter(doubtRequest -> {
                    Users tutorFromDoubt = doubtRequest.getTutor();
                    // Both tutors are null
                    return tutorFromDoubt == null || tutorFromDoubt.getName().equals(tutor.getName()); // Check if tutor names match
                })
                .toList();
        if (doubtRequests.isEmpty()) {
            throw new UserException("No pending doubt requests for this tutor");
        } else {
            return doubtRequests;
        }
    }

    /**
     * Retrieves all doubt requests assigned to a tutor.
     *
     * @param email The email of the tutor.
     * @return List of DoubtRequest objects assigned to the tutor.
     * @throws UserException if the user is not a tutor or no doubt requests are found.
     */
    @Override
    public List<DoubtRequest> getAllDoubtRequest(String email) {
        Users tutor = userRepository.findByEmail(email)
                .filter(user -> Objects.equals(user.getUserType(), "ROLE_TUTOR"))
                .orElseThrow(() -> new UserException("Tutor not found"));

        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByTutor(tutor).stream()
                .toList();
        if (doubtRequests.isEmpty()) {
            throw new UserException("No pending doubt requests for this tutor");
        } else {
            return doubtRequests;
        }
    }

}
