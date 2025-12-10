package com.Rutuja.Resolve.IT.Service;

import com.Rutuja.Resolve.IT.Model.Notes;
import com.Rutuja.Resolve.IT.Repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotesService {

    @Autowired
    private NotesRepository repo;

    public Notes addNote(Notes note) {
        note.setCreatedAt(LocalDateTime.now());
        return repo.save(note);
    }

    public List<Notes> getNotesByComplaint(Long complaintId) {
        return repo.findByComplaintIdOrderByCreatedAtDesc(complaintId);
    }
}
