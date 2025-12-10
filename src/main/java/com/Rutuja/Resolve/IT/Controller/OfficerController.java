package com.Rutuja.Resolve.IT.Controller;

import com.Rutuja.Resolve.IT.Model.Officer;
import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Repositories.OfficerRepository;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/officers")
@CrossOrigin(origins = "http://localhost:3000")
public class OfficerController {

    @Autowired
    private OfficerRepository officerRepository;

    @Autowired
    private ComplaintRepository complaintRepository;


    @PostMapping("/login")
    public ResponseEntity<?> loginOfficer(@RequestBody Officer officer) {
        Officer existing = officerRepository.findByEmail(officer.getEmail());

        if (existing != null && existing.getPassword().equals(officer.getPassword())) {

            Map<String, Object> res = new HashMap<>();
            res.put("id", existing.getId());
            res.put("name", existing.getName());
            res.put("email", existing.getEmail());
            res.put("role", "officer");

            return ResponseEntity.ok(res);

        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }



    @GetMapping("/{id}/complaints")
    public ResponseEntity<List<Complaint>> getAssignedComplaints(@PathVariable Long id) {
        List<Complaint> list = complaintRepository.findByOfficerId(id);
        return ResponseEntity.ok(list);
    }


  
    @GetMapping("/{id}/complaints/count")
    public ResponseEntity<Map<String, Integer>> getComplaintCount(@PathVariable Long id) {

        int count = complaintRepository.countByOfficerId(id);

        Map<String, Integer> res = new HashMap<>();
        res.put("count", count);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/complaints/{id}/assign")
    public ResponseEntity<?> assignOfficerToComplaint(@PathVariable Long id, @RequestParam Long officerId) {

    Complaint c = complaintRepository.findById(id).orElse(null);
    Officer o = officerRepository.findById(officerId).orElse(null);

    if (c == null) return ResponseEntity.status(404).body("Complaint not found");
    if (o == null) return ResponseEntity.status(404).body("Officer not found");

    c.setOfficerId(officerId);
    complaintRepository.save(c);

    
    int count = complaintRepository.countByOfficerId(officerId);
    o.setComplaintNumber(count);
    officerRepository.save(o);

    return ResponseEntity.ok("Assigned officer and updated count");
}

}
