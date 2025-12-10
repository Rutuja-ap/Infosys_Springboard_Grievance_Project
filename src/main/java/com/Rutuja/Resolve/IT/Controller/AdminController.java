package com.Rutuja.Resolve.IT.Controller;

import com.Rutuja.Resolve.IT.Model.Admin;
import com.Rutuja.Resolve.IT.Model.User;
import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Model.Officer;
import com.Rutuja.Resolve.IT.Repositories.AdminRepository;
import com.Rutuja.Resolve.IT.Repositories.UserRepository;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import com.Rutuja.Resolve.IT.Repositories.OfficerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin1")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        Admin existingAdmin = adminRepository.findByEmail(admin.getEmail());
        if (existingAdmin != null && existingAdmin.getPassword().equals(admin.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("adminId", existingAdmin.getId());
            response.put("firstName", existingAdmin.getFirstName());
            response.put("email", existingAdmin.getEmail());
            response.put("role", "admin");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@RequestBody Admin admin) {
        try {
            Admin existingAdmin = adminRepository.findByEmail(admin.getEmail());
            if (existingAdmin != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
            }
            Admin savedAdmin = adminRepository.save(admin);
            return ResponseEntity.ok(savedAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create admin");
        }
    }


    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        try {
            List<Complaint> complaints = complaintRepository.findAll();
            return ResponseEntity.ok(complaints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Autowired
    private OfficerRepository officerRepository;

    private LocalDate dateOnly;


    @PostMapping("/officers")
    public ResponseEntity<?> createOfficer(@RequestBody Officer officer) {
        try {
            Officer exists = officerRepository.findByEmail(officer.getEmail());
            if (exists != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email exists");
            Officer saved = officerRepository.save(officer);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed");
        }
    }

    
    @GetMapping("/officers")
    public ResponseEntity<?> getOfficers() {
       
        try {
        List<Officer> officers = officerRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Officer o : officers) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", o.getId());
            data.put("name", o.getName());
            data.put("email", o.getEmail());
            data.put("department", o.getDepartment());
            //data.put("password", o.getPassword());
            data.put("phone", o.getPhone());

            
            int complaintCount = complaintRepository.countByOfficerId(o.getId());
            o.setComplaintNumber(complaintCount);
            officerRepository.save(o);
            data.put("Complaint_number", complaintCount);

            result.add(data);
        }

        return ResponseEntity.ok(result);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    }


    @PutMapping("/complaints/{id}/assign")
    public ResponseEntity<?> assignOfficerToComplaint(@PathVariable Long id, @RequestParam Long officerId) {
        Optional<Complaint> opt = complaintRepository.findById(id);
        Optional<Officer> offOpt = officerRepository.findById(officerId);
        if (!opt.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Complaint not found");
        if (!offOpt.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Officer not found");

        Complaint c = opt.get();
        c.setOfficerId(officerId);
        complaintRepository.save(c);
        return ResponseEntity.ok("Assigned officer");
    }



    @PutMapping("/complaints/{id}/status")
public ResponseEntity<?> updateComplaintStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> request
) {
    String newStatus = request.get("status");

    Complaint complaint = complaintRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Complaint not found"));

    LocalDateTime now = LocalDateTime.now();

    switch (newStatus) {
        case "Pending":
            if (complaint.getCreatedAt() == null)
                complaint.setCreatedAt(now);
            break;

        case "Ongoing":
            complaint.setOngoingAt(now);
            break;

        case "Resolved":
            complaint.setResolvedAt(now);
            break;
    }

    
    complaint.setStatus(newStatus);

   
    if (complaint.getDueDate() != null) {
        boolean isPastDue = now.isAfter(complaint.getDueDate());
        boolean notResolved = !newStatus.equals("Resolved");

        if (isPastDue && notResolved) {
            complaint.setStatus("Escalated");

            // Set escalated timestamp only once
            if (complaint.getEscalatedAt() == null)
                complaint.setEscalatedAt(now);
        }
    }

    complaintRepository.save(complaint);
    return ResponseEntity.ok("Status Updated");
}


    @PutMapping("/complaints/{id}/dueDate")
    public Complaint updateDueDate(
            @PathVariable Long id,
            @RequestBody DueDateRequest req
    ) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        c.setDueDate(req.getDueDate());

        
        String oldHistory = c.getHistory() == null ? "" : c.getHistory();

        String newEntry = "Due date set to " + req.getDueDate()
                + " on: " + LocalDateTime.now() + "\n";

        c.setHistory(oldHistory + newEntry);

        return complaintRepository.save(c);
    }

    public static class DueDateRequest {
        private LocalDateTime dueDate;

        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
        }
    }
}
