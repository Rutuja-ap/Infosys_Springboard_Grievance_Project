package com.Rutuja.Resolve.IT.Service;
import com.Rutuja.Resolve.IT.DTO.NotificationDTO;
import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Model.Notification;
import com.Rutuja.Resolve.IT.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import java.util.stream.Collectors;



@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;
    @Autowired
    private ComplaintRepository complaintRepo;

    
    public Notification createNotification(Long complaintId, String message, String category, String urgency, String location) {
    Notification n = new Notification();
    n.setComplaintId(complaintId);
    n.setMessage(message);
     n.setSeen(false);
    n.setRead(false);
    n.setCreatedAt(LocalDateTime.now());
    Complaint c = complaintRepo.findById(complaintId).orElse(null);
    
        n.setCategory(c.getCategory());
        n.setUrgency(c.getUrgency());
        n.setLocation(c.getLocation());
    
    return repo.save(n); // ✅ return the saved Notification
}

    public List<Notification> getUnseen() {
        return repo.findUnseenNotifications();
    }

    public List<Notification> getLast24Hours() {
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        return repo.findLast24Hours(last24Hours);
        
        
 }

    public void markAllSeen() {
        List<Notification> list = repo.findUnseenNotifications();
        list.forEach(n -> n.setSeen(true));
        repo.saveAll(list);
    }
    public void markRead(Long id) {
    Notification n = repo.findById(id)
        .orElseThrow(() -> new RuntimeException("Notification not found"));
    n.setRead(true);
    repo.save(n);
}

}
