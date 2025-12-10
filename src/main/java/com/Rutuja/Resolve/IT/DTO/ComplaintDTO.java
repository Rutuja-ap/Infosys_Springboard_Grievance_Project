package com.Rutuja.Resolve.IT.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class ComplaintDTO {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime ongoingAt;
    private LocalDateTime resolvedAt;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long officerId;
    private String officerName;
    private List<StatusDTO> statusHistory;
    private List<String> timeline;

    
private String userFirstName;
private String userLastName;
private String userLocation;

private String officerEmail;
private String officerPhone;
private Integer complaintNumber;


 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getOngoingAt() { return ongoingAt; }
    public void setOngoingAt(LocalDateTime ongoingAt) { this.ongoingAt = ongoingAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long officerId) { this.officerId = officerId; }

    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }

    public List<StatusDTO> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<StatusDTO> statusHistory) { this.statusHistory = statusHistory; }

    public List<String> getTimeline() { return timeline; }
    public void setTimeline(List<String> timeline) { this.timeline = timeline; }

    public String getUserFirstName() {
        return userFirstName;
    }
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
    public String getUserLastName() {
        return userLastName;
    }   
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
    public String getUserLocation() {
        return userLocation;
    }
    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }
    public String getOfficerEmail() {
        return officerEmail;
    }
    public void setOfficerEmail(String officerEmail) {
        this.officerEmail = officerEmail;
    }
    public String getOfficerPhone() {
        return officerPhone;
    }
    public void setOfficerPhone(String officerPhone) {
        this.officerPhone = officerPhone;
    }
    public Integer getComplaintNumber() {
        return complaintNumber;
    }
    public void setComplaintNumber(Integer complaintNumber) {
        this.complaintNumber = complaintNumber;
    }
    
}
