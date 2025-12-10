package com.Rutuja.Resolve.IT.Service;
import com.Rutuja.Resolve.IT.Model.Feedback;
import com.Rutuja.Resolve.IT.Repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository repo;

    public Feedback create(Feedback feedback) {
        return repo.save(feedback);
    }

    public List<Feedback> getAll() { return repo.findAll(); }
    public List<Feedback> getByComplaint(Long id){
    return repo.findByComplaintId(id);
}
}

