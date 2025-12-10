package com.Rutuja.Resolve.IT.Controller;
import com.Rutuja.Resolve.IT.Model.Notification;
import com.Rutuja.Resolve.IT.DTO.ComplaintDTO;
import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Model.User;
import com.Rutuja.Resolve.IT.Service.ComplaintService;
import com.Rutuja.Resolve.IT.Service.NotificationService;
import com.Rutuja.Resolve.IT.Service.PDFExportService;
import com.Rutuja.Resolve.IT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import com.Rutuja.Resolve.IT.DTO.NotificationDTO;
import com.Rutuja.Resolve.IT.Service.CSVExportService;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:3000")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private UserService userService;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private CSVExportService csvExportService;
    @Autowired
    private PDFExportService pdfExportService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/add")
    
    public ResponseEntity<?> createComplaint(
            @RequestParam("userId") Long userId, 
            @RequestParam("category") String category,
            @RequestParam("urgency") String urgency,
            @RequestParam("complaint") String complaintText,
            @RequestParam("location") String location,
            @RequestParam(value = "image", required = false) MultipartFile image
        ) {
        try {
            Complaint complaint = new Complaint();
          
            complaint.setUserId(userId);   
            complaint.setCategory(category);
            complaint.setDescription(complaintText);
            complaint.setTitle(urgency); 
            complaint.setLocation(location);

            if (image != null && !image.isEmpty()) {
                complaint.setImage(image.getBytes()); 
            }

            Complaint savedComplaint = complaintService.saveComplaint(complaint, userId);
            Notification notification = complaintService.notifyAdminForNewComplaint(savedComplaint); 

            return ResponseEntity.ok(notification);  
            
        } catch (Exception e) {
             e.printStackTrace();
            return ResponseEntity.status(500).body("Error while saving complaint: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/user/{userId}")
    public List<Complaint> getComplaintsByUserId(@PathVariable Long userId) {
        return complaintService.getComplaintsByUserId(userId);
    }


    @GetMapping("/complaints")
    public List<ComplaintDTO> getAllComplaintsWithTimeline() {
        //return complaintService.getAllComplaintsWithTimeline();
        List<Complaint> complaint = complaintRepository.findAllByOrderByIdDesc();
         return complaintRepository.findAll().stream()
            .map(complaintService::mapToDTO)  // Use mapToDTO for proper mapping
            .collect(Collectors.toList());
   
    }
    @GetMapping("/{id}")
    public ComplaintDTO getComplaintById(@PathVariable Long id) {
    Complaint complaint = complaintService.getAllComplaints()
        .stream().filter(c -> c.getId().equals(id)).findFirst()
        .orElseThrow(() -> new RuntimeException("Complaint not found"));
    return complaintService.mapToDTO(complaint);
    }
    @GetMapping("/report/export")
    public ResponseEntity<?> exportCSV(
        @RequestParam("start") String startDate,
        @RequestParam("end") String endDate,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "format", defaultValue = "csv") String format
    ) {

    LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
    LocalDateTime end = LocalDate.parse(endDate).atTime(23,59,59);

    
    List<Complaint> complaints = complaintRepository.findAll().stream()
        .filter(c -> c.getCreatedAt() != null
                     && !c.getCreatedAt().isBefore(start)
                     && !c.getCreatedAt().isAfter(end))
        .collect(Collectors.toList());
    if (category != null && !category.isEmpty()) {
        complaints = complaints.stream()
                .filter(c -> category.equalsIgnoreCase(c.getCategory()))
                .collect(Collectors.toList());
    }

    if (complaints.isEmpty()) {
        return ResponseEntity.badRequest().body("No complaints found for the selected parameters.");
    }

     if ("csv".equalsIgnoreCase(format)) {
        ByteArrayResource csv = csvExportService.exportToCSV(complaints);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=complaints-report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    } else if ("pdf".equalsIgnoreCase(format)) {
        ByteArrayResource pdf = pdfExportService.exportToPDF(complaints);
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=complaints-report.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }

    return ResponseEntity.badRequest().body("Invalid format");
}
}

