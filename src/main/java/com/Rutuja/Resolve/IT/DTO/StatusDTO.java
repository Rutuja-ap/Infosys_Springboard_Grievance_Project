package com.Rutuja.Resolve.IT.DTO;

import java.time.LocalDateTime;

public class StatusDTO {
    private String status;
    private LocalDateTime date;

    public StatusDTO() {}

    public StatusDTO(String status, LocalDateTime date) {
        this.status = status;
        this.date = date;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
