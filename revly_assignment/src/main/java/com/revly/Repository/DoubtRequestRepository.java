package com.revly.Repository;

import com.revly.Model.DoubtRequest;
import com.revly.Model.Subjects;
import com.revly.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoubtRequestRepository extends JpaRepository<DoubtRequest, Integer> {

    public List<DoubtRequest> findByTutor(Users tutor);
    public List<DoubtRequest> findByStudent(Users student);
    public List<DoubtRequest> findByDoubtSubject(Subjects doubtSubject);
}
