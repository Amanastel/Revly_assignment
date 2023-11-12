package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Model.DoubtRequest;
import com.revly.Model.DoubtResolved;
import com.revly.Model.Users;
import com.revly.Model.UserType;
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
            doubtRequest.setTutor(tutor);
            doubtRequest.setTimestamp(LocalDateTime.now());

            // Save the updated DoubtRequest
            return doubtRequestRepository.save(doubtRequest);
        } else {
            throw new UserException("Doubt is already resolved");
        }
    }


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

}
