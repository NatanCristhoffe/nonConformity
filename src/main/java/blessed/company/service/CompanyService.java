package blessed.company.service;


import blessed.company.dto.CompanyRequestDTO;
import blessed.company.dto.UpdatePlanTypeCompanyDTO;
import blessed.company.entity.Company;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import blessed.company.service.query.CompanyQuery;
import blessed.exception.BusinessException;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyService {
    private final CompanyQuery companyQuery;
    private final UserQuery userQuery;

    CompanyService(
            CompanyQuery companyQuery,
            UserQuery userQuery
    ){
        this.companyQuery = companyQuery;
        this.userQuery = userQuery;
    }

    public Company create(CompanyRequestDTO data){
        existsByEmailOrPhone(data);

        Company company = new Company(data, TypeDocument.CPF);
        companyQuery.save(company);

        return company;
    }

    @Transactional
    public void updatePlanCompany(UpdatePlanTypeCompanyDTO data, UUID companyId){
        Company company = companyQuery.byId(companyId);
        PlanType newPlan = data.planType();

        if(company.getPlanType().equals(newPlan)){
            throw new BusinessException("A empresa já possui este plano.");
        }

        Long totalUsers = userQuery.countByCompany(companyId);

        if (totalUsers > newPlan.getMaxUsers()){
            throw new BusinessException(
                    "Não é possível alterar para o plano " + newPlan +
                    ". Existem " + totalUsers +
                    " usuários ativos e o plano permite apenas " +
                    newPlan.getMaxUsers()
            );
        }

        company.setPlanType(data.planType());
    }


    private void existsByEmailOrPhone(CompanyRequestDTO data){
        if (this.companyQuery.existsByEmailOrPhoneOrDocument(data.document(), data.email(), data.phone())){
            throw new BusinessException("Empresa já cadastrada com este documento, e-mail ou telefone.");
        }
    }
}
