package com.revly.Service;

import com.revly.Model.DoubtRequest;

import java.util.List;

public interface DoubtRequestServiceTutor {
    public DoubtRequest solveDoubt(Integer doubtRequestId, String solutionDescription, String email);
    public List<DoubtRequest> allPendingDoubtRequest(String email);



}
