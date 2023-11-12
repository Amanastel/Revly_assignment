package com.revly.Service;

import com.revly.Model.DoubtRequest;

public interface DoubtRequestServiceTutor {
    public DoubtRequest solveDoubt(Integer doubtRequestId, String solutionDescription, Integer tutorId);

}
