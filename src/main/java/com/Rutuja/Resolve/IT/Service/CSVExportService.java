package com.Rutuja.Resolve.IT.Service;

import com.Rutuja.Resolve.IT.Model.Complaint;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CSVExportService {

    public ByteArrayResource exportToCSV(List<Complaint> complaints) {
        StringBuilder csv = new StringBuilder();

        
        csv.append("ID,Title,Category,Status,CreatedDate,UserId,OfficerId,Location,Description\n");

        
        for (Complaint c : complaints) {
            csv.append(c.getId()).append(",");
            csv.append(safe(c.getTitle())).append(",");
            csv.append(safe(c.getCategory())).append(",");
            csv.append(safe(c.getStatus())).append(",");
            //csv.append(c.getDateOnly() != null ? c.getDateOnly() : "").append(",");
            csv.append(c.getCreatedAt() != null ? c.getCreatedAt() : "").append(",");
            csv.append(c.getUserId()).append(",");
            csv.append(c.getOfficerId()).append(",");
            csv.append(safe(c.getLocation())).append(",");
            csv.append(safe(c.getDescription())).append("\n");
        }

        return new ByteArrayResource(csv.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
