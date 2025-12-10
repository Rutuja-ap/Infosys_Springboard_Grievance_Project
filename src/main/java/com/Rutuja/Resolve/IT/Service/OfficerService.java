package com.Rutuja.Resolve.IT.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.Rutuja.Resolve.IT.Model.Complaint;
import com.Rutuja.Resolve.IT.Model.Officer;
import com.Rutuja.Resolve.IT.Repositories.ComplaintRepository;
import com.Rutuja.Resolve.IT.Repositories.OfficerRepository;
@Service
public class OfficerService {
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private OfficerRepository officerRepository; 

    public int getComplaintCountForOfficer(Long officerId) {
        return complaintRepository.countByOfficerId(officerId);
    }

    public List<Complaint> getOfficerComplaints(Long officerId) {
        return complaintRepository.findByOfficerId(officerId);
    }
    
    public Officer getOfficerById(Long officerId) {
        return officerRepository.findById(officerId).orElse(null);
    }
}
