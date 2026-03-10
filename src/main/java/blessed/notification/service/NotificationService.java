package blessed.notification.service;

import blessed.notification.dto.NotificationResponseDTO;
import blessed.notification.entity.Notification;
import blessed.notification.enums.NotificationType;
import blessed.notification.repository.NotificationRepository;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserQuery userQuery;
    @Autowired
    private SimpUserRegistry simpUserRegistry;

    public NotificationService(
            NotificationRepository repository,
            SimpMessagingTemplate messagingTemplate,
            UserQuery userQuery
    ){
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
        this.userQuery = userQuery;
    }

    @Transactional
    public void notifyByUser(UUID userId, UUID companyId,String message){
        User user = userQuery.byId(companyId, userId);
        Notification notification = new Notification(message, user, NotificationType.NON_CONFORMITY_CREATED);

        System.out.println("Connected users: " + simpUserRegistry.getUsers());

        messagingTemplate.convertAndSendToUser(
                user.getEmail(),
                "/queue/notifications",
                new NotificationResponseDTO(notification)
        );
    }
}
