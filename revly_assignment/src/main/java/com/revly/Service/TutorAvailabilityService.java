package com.revly.Service;

import com.revly.Model.TutorAvailability;

import java.util.List;

public interface TutorAvailabilityService {
    public TutorAvailability addTutorAvailability(String email);
    public List<TutorAvailability> availableTutors();
    public List<TutorAvailability> unavailableTutors();
    public int countOnlineTutors();
    public List<TutorAvailability> getAllTutorAvailabilityByStudentEmail(String email);
}
