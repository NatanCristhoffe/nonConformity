package blessed.notification.service.query;

import blessed.exception.BusinessException;
import blessed.notification.entity.Notification;
import blessed.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationQuery {

    private final NotificationRepository repository;

    NotificationQuery(NotificationRepository repository){
        this.repository = repository;
    }

    public Notification byId(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Erro ao carregar notificações"));
    }
}
