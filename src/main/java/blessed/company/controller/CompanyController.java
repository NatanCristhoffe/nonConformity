package blessed.company.controller;


import blessed.application.dto.CompanyWithAdminRequestDTO;
import blessed.application.service.CreateCompanyWithAdminService;
import blessed.company.dto.CompanyRequestDTO;
import blessed.company.dto.UpdatePlanTypeCompanyDTO;
import blessed.company.enums.PlanType;
import blessed.company.service.CompanyService;

import blessed.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private final CreateCompanyWithAdminService serviceAdmin;
    private final CompanyService companyService;

    CompanyController(
            CreateCompanyWithAdminService serviceAdmin,
            CompanyService companyService
    ){
        this.serviceAdmin = serviceAdmin;
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> companyCreate(@RequestBody CompanyWithAdminRequestDTO data){
        serviceAdmin.createCompanyWithAdmin(data);

        return ResponseEntity.ok(Map.of("success", "Empresa cadastrada com sucesso!"));
    }

    @PatchMapping("/update-plan")
    public ResponseEntity<Map<String, String>> updatePlanTypeCompany(
            @AuthenticationPrincipal User user,
            @RequestBody UpdatePlanTypeCompanyDTO updatePlanTypeCompanyDTO
            ){
      companyService.updatePlanCompany(updatePlanTypeCompanyDTO, user.getCompany().getId());

      return ResponseEntity.ok(Map.of("success", "Plano atualizado com sucesso"));
    };
}
