package com.revly.Service;

import com.revly.Model.DoubtRequest;

public interface DoubtRequestService {
    public DoubtRequest addDoubtRequest(DoubtRequest doubtRequest);
    public DoubtRequest tutorAvailableLiveDoubtRequest(DoubtRequest doubtRequest, String email);
//    public void deleteDoubtRequest();
//    public void updateDoubtRequest();
//    public void getDoubtRequest();
//    public void getAllDoubtRequest();
}
