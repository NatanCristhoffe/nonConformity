package blessed.notification.repository;

import blessed.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdAndReadFalse(Long userId);
    Optional<Notification> findById(UUID id);
}
