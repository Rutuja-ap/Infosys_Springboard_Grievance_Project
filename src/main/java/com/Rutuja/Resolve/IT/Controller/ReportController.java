package com.Rutuja.Resolve.IT.Controller;

import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    @Autowired
    private ComplaintRepository complaintRepository;

   
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportReport(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "csv") String format
    ) {
        
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

       
        List<Complaint> filtered = complaintRepository.findAll().stream()
                .filter(c -> {
                    if (c.getDateOnly() == null) return false;
                    return !c.getDateOnly().isBefore(startDate) && !c.getDateOnly().isAfter(endDate);
                })
                .filter(c -> {
                    if (category == null || category.trim().isEmpty()) return true;
                    return Objects.equals(category.trim(), c.getCategory());
                })
                .collect(Collectors.toList());

        if ("pdf".equalsIgnoreCase(format)) {
            return exportAsPdf(filtered);
        } else {
            return exportAsCsv(filtered);
        }
    }


    private ResponseEntity<InputStreamResource> exportAsCsv(List<Complaint> complaints) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(out, true, java.nio.charset.StandardCharsets.UTF_8);

            
            pw.println("ID,Title,Category,Status,CreatedDate,UserId,OfficerId,Location,Description");

            for (Complaint c : complaints) {
               
                String title = safeCsv(c.getTitle());
                String cat = safeCsv(c.getCategory());
                String status = safeCsv(c.getStatus());
                String date = c.getDateOnly() != null ? c.getDateOnly().toString() : "";
                String userId = c.getUserId() != null ? c.getUserId().toString() : "";
                String officerId = c.getOfficerId() != null ? c.getOfficerId().toString() : "";
                String loc = safeCsv(c.getLocation());
                String desc = safeCsv(c.getDescription());

                pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        c.getId() == null ? "" : c.getId(),
                        title, cat, status, date, userId, officerId, loc, desc);
            }
            pw.flush();

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=complaints_report.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(out.size())
                    .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                    .body(new InputStreamResource(in));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    private ResponseEntity<InputStreamResource> exportAsPdf(List<Complaint> complaints) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(out, true, java.nio.charset.StandardCharsets.UTF_8);

            pw.println("Complaints Report");
            pw.println("=================");
            pw.println();

            for (Complaint c : complaints) {
                pw.println("ID: " + (c.getId() != null ? c.getId() : ""));
                pw.println("Title: " + safe(c.getTitle()));
                pw.println("Category: " + safe(c.getCategory()));
                pw.println("Status: " + safe(c.getStatus()));
                pw.println("Created Date: " + (c.getDateOnly() != null ? c.getDateOnly().toString() : ""));
                pw.println("UserId: " + (c.getUserId() != null ? c.getUserId() : ""));
                pw.println("OfficerId: " + (c.getOfficerId() != null ? c.getOfficerId() : ""));
                pw.println("Location: " + safe(c.getLocation()));
                pw.println("Description: " + safe(c.getDescription()));
                pw.println("------------------------------------------------------");
            }
            pw.flush();

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=complaints_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(out.size())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(in));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    
    private String safeCsv(String s) {
        if (s == null) return "";
        
        String v = s.replace("\"", "\"\"");
       
        if (v.contains(",") || v.contains("\n") || v.contains("\r")) {
            return "\"" + v + "\"";
        }
        return v;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
