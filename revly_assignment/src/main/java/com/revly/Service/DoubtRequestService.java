package com.revly.Service;

import com.revly.Model.DoubtRequest;

public interface DoubtRequestService {
    public DoubtRequest addDoubtRequest(DoubtRequest doubtRequest, Integer userId);
    public DoubtRequest tutorAvailableLiveDoubtRequest(DoubtRequest doubtRequest, Integer userId);
//    public void deleteDoubtRequest();
//    public void updateDoubtRequest();
//    public void getDoubtRequest();
//    public void getAllDoubtRequest();
}
