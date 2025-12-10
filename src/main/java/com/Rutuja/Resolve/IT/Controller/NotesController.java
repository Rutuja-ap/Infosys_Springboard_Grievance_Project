package com.Rutuja.Resolve.IT.Controller;

import com.Rutuja.Resolve.IT.Model.Notes;
import com.Rutuja.Resolve.IT.Service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin("http://localhost:3000")
public class NotesController {

    @Autowired
    private NotesService service;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Notes note) {
        return ResponseEntity.ok(service.addNote(note));
    }

    @GetMapping("/complaint/{id}")
    public ResponseEntity<?> list(@PathVariable Long id) {
        return ResponseEntity.ok(service.getNotesByComplaint(id));
    }
}
