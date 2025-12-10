package com.Rutuja.Resolve.IT.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_history")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;

    public StatusHistory() {}

    public StatusHistory(String status, LocalDateTime date, Complaint complaint) {
        this.status = status;
        this.date = date;
        this.complaint = complaint;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
}
