package com.Rutuja.Resolve.IT.Model;

import jakarta.persistence.*;
import lombok.Data;



import java.time.LocalDateTime;

@Entity
@Data
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long complaintId;        
    private String message;         
    private String noteType;         
    private String createdBy;        
    private LocalDateTime createdAt; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId ; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNoteType() { return noteType; }    
    public void setNoteType(String noteType) { this.noteType = noteType; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    
    
}
