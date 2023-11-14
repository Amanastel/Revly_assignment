package com.revly.Service;

import com.revly.Model.DoubtRequest;

import java.util.List;

public interface DoubtRequestService {
    public DoubtRequest addDoubtRequest(DoubtRequest doubtRequest);
    public DoubtRequest tutorAvailableLiveDoubtRequest(DoubtRequest doubtRequest, String email);
    public DoubtRequest findDoubtRequestById(Integer doubtRequestId);
    public DoubtRequest updateUnresolvedDoubts(Integer doubtRequestId, String doubtDescription);
    public DoubtRequest deleteDoubtRequestById(Integer doubtRequestId);
    public List<DoubtRequest> getAllDoubtRequest(String email);
    public List<DoubtRequest> getAllResolvedDoubtRequest(String email);
    public List<DoubtRequest> getAllPendingDoubtRequest(String email);
}
