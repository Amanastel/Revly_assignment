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

    @Override
    public DoubtRequest findDoubtRequestById(Integer doubtRequestId) {
        return doubtRequestRepository.findById(doubtRequestId).orElseThrow(() -> new UserException("DoubtRequest not found"));
    }

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

    @Override
    public List<DoubtRequest> getAllDoubtRequest(String email) {
        return doubtRequestRepository.findByStudent(userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found")));
    }

    @Override
    public List<DoubtRequest> getAllResolvedDoubtRequest(String email) {
        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByStudent(userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found")));
        return doubtRequests.stream().filter(doubtRequest -> doubtRequest.getDoubtResolved() == DoubtResolved.RESOLVED).toList();
    }

    @Override
    public List<DoubtRequest> getAllPendingDoubtRequest(String email) {
        List<DoubtRequest> doubtRequests = doubtRequestRepository.findByStudent(userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found")));
        return doubtRequests.stream().filter(doubtRequest -> doubtRequest.getDoubtResolved() == DoubtResolved.UNRESOLVED).toList();
    }


}
