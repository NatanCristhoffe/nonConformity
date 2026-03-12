package blessed.nonconformity.service;

import blessed.auth.utils.CurrentUser;
import blessed.company.entity.Company;
import blessed.nonconformity.dto.DashboardIndicatorsResponse;
import blessed.nonconformity.dto.SummaryDTO;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.query.DashboardQuery;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class DashboardService {

    private final DashboardQuery dashboardQuery;
    private final CurrentUser currentUser;

    DashboardService(DashboardQuery dashboardQuery, CurrentUser currentUser){
        this.dashboardQuery = dashboardQuery;
        this.currentUser = currentUser;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DashboardIndicatorsResponse getIndicators(){
        UUID companyId = currentUser.getCompanyId();
        DashboardIndicatorsResponse response = new DashboardIndicatorsResponse();


        response.setSummary(buildSummary());
        response.setByPriority(dashboardQuery.countByPriority(companyId));
        response.setByDepartment(dashboardQuery.countByDepartment(companyId));
        response.setTrend(dashboardQuery.trend(companyId));
        response.setAverageResolutionDays(dashboardQuery.averageResolutionDays(companyId));

        return response;
    }

    private SummaryDTO buildSummary() {

        Map<NonConformityStatus, Long> byStatus =
                dashboardQuery.countByStatus(currentUser.getCompanyId());

        long total = byStatus.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();

        SummaryDTO summary = new SummaryDTO();
        summary.setByStatus(byStatus);
        summary.setTotal(total);

        return summary;
    }


}
