package com.Rutuja.Resolve.IT.Service;

import com.Rutuja.Resolve.IT.DTO.ComplaintDTO;
import com.Rutuja.Resolve.IT.DTO.NotificationDTO;
import com.Rutuja.Resolve.IT.DTO.StatusDTO;
import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Model.Officer;
import com.Rutuja.Resolve.IT.Model.StatusHistory;
import com.Rutuja.Resolve.IT.Model.User;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 
import com.Rutuja.Resolve.IT.Model.Notification;



@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OfficerService officerService;

    @Autowired
    private NotificationService notificationService;


    public Complaint saveComplaint(Complaint complaint, Long userId) {
        complaint.setUserId(userId);
        if (complaint.getCreatedAt() == null) {
            complaint.setCreatedAt(LocalDateTime.now());
            complaint.setDateOnly(LocalDate.now());
        }
        return complaintRepository.save(complaint);
    }

 
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    
    public List<Complaint> getComplaintsByUserId(Long userId) {
        return complaintRepository.findByUserId(userId);
    }


    public List<ComplaintDTO> getAllComplaintsWithTimeline() {
        List<Complaint> complaints = complaintRepository.findAll();
        return complaints.stream()
                .map(this::mapToDTO) // Use mapToDTO to map all fields including user/officer info
                .collect(Collectors.toList());
    }

  
    public void addStatusHistory(Complaint complaint, String newStatus) {
        StatusHistory sh = new StatusHistory();
        sh.setStatus(newStatus);
        sh.setDate(LocalDateTime.now());
        sh.setComplaint(complaint);

        List<StatusHistory> history = complaint.getStatusHistory();
        if (history != null) {
            history.add(sh);
        } else {
            history = new ArrayList<>();
            history.add(sh);
        }

        complaint.setStatusHistory(history);
        complaint.setStatus(newStatus);

        
        switch (newStatus) {
            case "Pending":
                complaint.setCreatedAt(LocalDateTime.now());
                break;
            case "Ongoing":
                complaint.setOngoingAt(LocalDateTime.now());
                break;
            case "Resolved":
                complaint.setResolvedAt(LocalDateTime.now());
                break;
        }

        complaintRepository.save(complaint);
    }

    
    public ComplaintDTO mapToDTO(Complaint c) {
        ComplaintDTO dto = new ComplaintDTO();

        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setCategory(c.getCategory());
        dto.setDescription(c.getDescription());
        dto.setStatus(c.getStatus());
        dto.setUserId(c.getUserId());
        dto.setOfficerId(c.getOfficerId());

  
        if (c.getUserId() != null) {
            User user = userService.getUserById(c.getUserId());
            if (user != null) {
                dto.setUserFirstName(user.getFirstName());
                dto.setUserLastName(user.getLastName());
                dto.setUserEmail(user.getEmail());
                dto.setUserLocation(user.getLocation());
            }
        }

        
        if (c.getOfficerId() != null) {
            Officer officer = officerService.getOfficerById(c.getOfficerId());
            if (officer != null) {
                dto.setOfficerName(officer.getName());
                dto.setOfficerEmail(officer.getEmail());
                dto.setOfficerPhone(officer.getPhone());
                dto.setComplaintNumber(officer.getComplaintNumber());
            }
        }

        
        dto.setCreatedAt(c.getCreatedAt());
        dto.setOngoingAt(c.getOngoingAt());
        dto.setResolvedAt(c.getResolvedAt());

        
        List<String> timeline = new ArrayList<>();
        if (c.getCreatedAt() != null)
            timeline.add("Created at: " + c.getCreatedAt());
        if (c.getOngoingAt() != null)
            timeline.add("Marked Ongoing at: " + c.getOngoingAt());
        if (c.getResolvedAt() != null)
            timeline.add("Resolved at: " + c.getResolvedAt());
        dto.setTimeline(timeline);

        List<StatusDTO> statusHistory = c.getStatusHistory() != null
                ? c.getStatusHistory().stream()
                        .map(sh -> new StatusDTO(sh.getStatus(), sh.getDate()))
                        .collect(Collectors.toList())
                : new ArrayList<>();
        dto.setStatusHistory(statusHistory);

        return dto;
    }



public Notification notifyAdminForNewComplaint(Complaint complaint) {

    
    Notification n = notificationService.createNotification(
            complaint.getId(),
            "New complaint submitted by user ID: " + complaint.getUserId(),
            "Location: " + complaint.getLocation() ,
             "Category: " + complaint.getCategory() ,
              " Urgency: " + complaint.getUrgency()  

    );

   
    User user = userService.getUserById(complaint.getUserId());
    String username = (user != null)
            ? user.getFirstName() + " " + user.getLastName()
            : "Unknown User";

   
    return n;
}


public List<Complaint> getFilteredComplaints(LocalDate start, LocalDate end) {
    return complaintRepository.findByDateOnlyBetween(start, end);
}

}