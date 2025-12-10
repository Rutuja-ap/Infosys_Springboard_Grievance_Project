package com.Rutuja.Resolve.IT.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;
    private String location;
    private String status;
    private Long userId;

    @Column(name = "officer_id")
    private Long officerId;
    private LocalDateTime createdAt;
    private LocalDateTime ongoingAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime dueDate; 
    private LocalDateTime escalatedAt;
    private LocalDate dateOnly;
    private String urgency;

    private String history;
    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;
    


    public Complaint() {}

    public Complaint(String title, String description, String category, Long userId) {
        this.title = title;
        this.description = description;
        this.category = category;
        
        this.userId = userId;
        
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long officerId) { this.officerId = officerId; }


    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public String getHistory() {return history;}
    public void setHistory(String history) {this.history = history;}

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getOngoingAt() {return ongoingAt;}
    public void setOngoingAt(LocalDateTime ongoingAt) {this.ongoingAt = ongoingAt;}

    public LocalDateTime getResolvedAt() {return resolvedAt;}
    public void setResolvedAt(LocalDateTime resolvedAt) {this.resolvedAt = resolvedAt;}

    public List<StatusHistory> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<StatusHistory> statusHistory) { this.statusHistory = statusHistory; }

    public LocalDateTime getEscalatedAt() { return escalatedAt; }
    public void setEscalatedAt(LocalDateTime escalatedAt) { this.escalatedAt = escalatedAt; }

    public LocalDate getDateOnly() { return dateOnly; }
    public void setDateOnly(LocalDate dateOnly) { this.dateOnly = dateOnly; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
}
