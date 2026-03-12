package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.EffectivenessAnalysisRequestDTO;
import blessed.nonconformity.entity.EffectivenessAnalysis;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.EffectivenessRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.service.query.EffectivenessAnalysisQuery;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.notification.enums.NotificationType;
import blessed.notification.service.NotificationService;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class EffectivenessAnalysisService {

    private final EffectivenessAnalysisQuery effectivenessAnalysisQuery;
    private final NonConformityQuery nonConformityQuery;
    private final UserQuery userQuery;
    private final NotificationService notificationService;

    public EffectivenessAnalysisService(
            EffectivenessAnalysisQuery effectivenessAnalysisQuery,
            NonConformityQuery nonConformityQuery,
            UserQuery userQuery,
            NotificationService notificationService
    ) {
        this.effectivenessAnalysisQuery = effectivenessAnalysisQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.userQuery = userQuery;
        this.notificationService = notificationService;
    }

    @PreAuthorize("@ncAuth.isEffectivenessAnalystOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public void addEffectivenessAnalysis(Long nonconformityId,EffectivenessAnalysisRequestDTO data, User userRequest) {
        UUID companyId = userRequest.getCompany().getId();
        NonConformity nc = nonConformityQuery.byId(nonconformityId, companyId);
        User user = userQuery.byId(userRequest.getCompany().getId(), userRequest.getId());

        EffectivenessAnalysis effectiveness = new EffectivenessAnalysis(data, nc, user);
        nc.addEffectivenessAnalysis(effectiveness, userRequest);

        effectivenessAnalysisQuery.save(effectiveness);

        Set<UUID> notifyUsers =  new HashSet<UUID>();

        notifyUsers.add(nc.getCreatedBy().getId());
        notifyUsers.add(nc.getDispositionOwner().getId());
        notifyUsers.add(nc.getEffectivenessAnalyst().getId());

        if (effectiveness.getEffective()){
            notificationService.notifyIfNotSameUser(
                    notifyUsers,
                    userRequest.getId(),
                    companyId,
                    NotificationType.EFFECTIVENESS_APPROVED,
                    nc.getTitle()
            );
        } else {
            notificationService.notifyIfNotSameUser(
                    notifyUsers,
                    userRequest.getId(),
                    companyId,
                    NotificationType.EFFECTIVENESS_REJECTED,
                    nc.getTitle()
            );
        }

    }
}
