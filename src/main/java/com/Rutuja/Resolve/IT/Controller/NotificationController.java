package com.Rutuja.Resolve.IT.Controller;

import com.Rutuja.Resolve.IT.Model.Notification;
import com.Rutuja.Resolve.IT.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/admin1/notifications")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/unseen")
    public List<Notification> unseen() {
        return service.getUnseen();
    }

    @GetMapping("/last24hrs")
    public List<Notification> last24hrs() {
        return service.getLast24Hours();
    }

    @PutMapping("/mark-seen")
    public String markSeen() {
        service.markAllSeen();
        return "All notifications marked as seen";
    }
    @PutMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {
    service.markRead(id);
    return "Marked as read";
}
}
