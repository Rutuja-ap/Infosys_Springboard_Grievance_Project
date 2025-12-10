package com.Rutuja.Resolve.IT.DTO;

import java.time.LocalDateTime;

import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Model.Notification;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import java.util.stream.Collectors;


public class NotificationDTO {

    private Long id;
    private String message;
    private Long complaintId;
    private boolean isSeen;
    private LocalDateTime createdAt;
    private boolean isRead;
    private String location;
    private String category;
    private String urgency;



    public NotificationDTO() { }

    
    public NotificationDTO(Notification n, Complaint c) {
    
        this.id = n.getId();
        this.message = n.getMessage();
        this.complaintId = n.getComplaintId();
        this.isSeen = n.isSeen();
        this.isRead = n.isRead();
        this.createdAt = n.getCreatedAt();

        if(c != null){
            this.location = c.getLocation();
            this.category = c.getCategory();
            this.urgency = c.getUrgency();
        }

    }


    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId; }

    public boolean isSeen() { return isSeen; }
    public void setSeen(boolean seen) { this.isSeen = seen; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }



    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }


}

