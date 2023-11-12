package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Exception.TutorAvailabilityException;
import com.revly.Model.*;
import com.revly.Repository.DoubtRequestRepository;
import com.revly.Repository.TutorAvailabilityRepository;
import com.revly.Repository.UserRepository;
import com.revly.Service.DoubtRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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
    public DoubtRequest addDoubtRequest(DoubtRequest doubtRequest, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        if(user.getUserType().equals(UserType.STUDENT)) {
            doubtRequest.setDoubtDescription("Student doubt description: " + doubtRequest.getDoubtDescription());
            doubtRequest.setStudent(user);
            doubtRequest.setDoubtResolved(DoubtResolved.UNRESOLVED);
            doubtRequest.setTimestamp(LocalDateTime.now());
            return doubtRequestRepository.save(doubtRequest);
        }
        else {
            throw new UserException("Only students can add doubt requests");
        }
    }

//    @Override
//    public DoubtRequest tutorAvailableLiveDoubtRequest(DoubtRequest doubtRequest, Integer userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
//        List<TutorAvailability> tutorAvailabilityList = tutorAvailabilityRepository.findByAvailabilityStatus("AVAILABLE");
//        if(user.getUserType().equals(UserType.TUTOR)) {
//            if(tutorAvailabilityList.size() > 0) {
//                // here i want to check tutor subjectExpertise and students subject same then only i want to assign tutor to student here you can use stream api
//
//
//
//
//                TutorAvailability tutorAvailability = tutorAvailabilityList.get(0);
//
//                tutorAvailability.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
//                tutorAvailabilityRepository.save(tutorAvailability);
//                doubtRequest.setTutor(user);
//                doubtRequest.setTimestamp(LocalDateTime.now());
//                return doubtRequestRepository.save(doubtRequest);
//            }
//            else {
//                throw new UserException("No tutors available");
//            }
//        }
//        else {
//            throw new UserException("Only tutors can accept doubt requests");
//        }
//    }
//

    @Override
    public DoubtRequest tutorAvailableLiveDoubtRequest(DoubtRequest doubtRequest, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));

        if (user.getUserType().equals(UserType.TUTOR)) {
            throw new UserException("Only students can create doubt requests");
        }

        if (user.getUserType().equals(UserType.STUDENT)) {
            List<TutorAvailability> availableTutors = tutorAvailabilityRepository.findByAvailabilityStatus("AVAILABLE");

            if (availableTutors.isEmpty()) {
                throw new TutorAvailabilityException("No tutors available");
            }

            // Use Stream API to find the first tutor with matching subject expertise
            TutorAvailability selectedTutor = availableTutors.stream()
                    .filter(tutor -> tutor.getTutor().getSubjectExpertise().equals(user.getUserLanguage())) // Assuming userLanguage is the subject for the student
                    .findFirst()
                    .orElseThrow(() -> new UserException("No matching tutor available"));

            selectedTutor.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
            tutorAvailabilityRepository.save(selectedTutor);

            doubtRequest.setStudent(user);
            doubtRequest.setTutor(selectedTutor.getTutor());
            doubtRequest.setTimestamp(LocalDateTime.now());

            return doubtRequestRepository.save(doubtRequest);
        }else{
            throw new UserException("Only students can create doubt requests");
        }

    }




}
