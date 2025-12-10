package com.Rutuja.Resolve.IT.Repositories;
import com.Rutuja.Resolve.IT.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {


   @Query("SELECT n FROM Notification n WHERE n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findUnseenNotifications();

    // Last 24 hours notifications (can be all, doesn't matter)
    @Query("SELECT n FROM Notification n WHERE n.createdAt >= :last24Hours ORDER BY n.createdAt DESC")
    List<Notification> findLast24Hours(LocalDateTime last24Hours);

    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();


}
