package com.revly.Service;

import com.revly.Model.DoubtRequest;

import java.util.List;

public interface DoubtRequestServiceTutor {
    public DoubtRequest solveDoubt(Integer doubtRequestId, String solutionDescription, String email);
    public DoubtRequest acceptDoubtRequest(Integer doubtRequestId, String email);
    public List<DoubtRequest> allPendingDoubtRequest(String email);
    public List<DoubtRequest> checkDoubtRequestBasedOnExpertise(String email);
    public List<DoubtRequest> getAllDoubtRequest(String email);
}
