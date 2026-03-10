package blessed.notification.controller;

import blessed.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService service;

    NotificationController(NotificationService service){
        this.service = service;
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable UUID id){
        service.markAsRead(id);
        return ResponseEntity.ok(Map.of("success", "notificação lida com sucesso!"));
    }
}
