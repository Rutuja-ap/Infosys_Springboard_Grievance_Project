package com.Rutuja.Resolve.IT.Controller;
import com.Rutuja.Resolve.IT.Model.Feedback;
import com.Rutuja.Resolve.IT.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("http://localhost:3000")
public class FeedbackController {

    @Autowired
    private FeedbackService service;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(service.create(feedback));
    }

    @GetMapping("/all")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }
    @GetMapping("/complaint/{id}")
public ResponseEntity<?> listByComplaint(@PathVariable Long id){
    return ResponseEntity.ok(service.getByComplaint(id));
}
}

