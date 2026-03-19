package blessed.nonconformity.repository;

import blessed.company.entity.Company;
import blessed.company.repository.CompanyRepository;
import blessed.nonconformity.entity.NonConformity;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
public class NonConformityRepositoryFindByIdAndCompanyIdTest {

    private final NonconformityRepository nonconformityRepository;
    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    NonConformityRepositoryFindByIdAndCompanyIdTest(
            NonconformityRepository nonconformityRepository,
            CompanyRepository companyRepository,
            SectorRepository sectorRepository,
            UserRepository userRepository
    ){
        this.nonconformityRepository = nonconformityRepository;
        this.companyRepository = companyRepository;
        this.sectorRepository = sectorRepository;
        this.userRepository = userRepository;
    }

    @Test
    void shouldReturnTrueWhenFindByIdAndCompanyId(){
        Company company = companyRepository.save(
                TestDataFactory.createCompany()
        );
        Sector sector = sectorRepository.save(
                TestDataFactory.createSector(company)
        );
        User user = userRepository.save(
                TestDataFactory.createUser(company, sector)
        );

        NonConformity nonConformity = TestDataFactory.createNonConformity(
                company,
                sector,
                user
        );
        nonconformityRepository.save(nonConformity);

        Optional<NonConformity> found = nonconformityRepository.findByIdAndCompanyId(
                nonConformity.getId(),
                nonConformity.getCompany().getId()
        );

        assertTrue(found.isPresent());
        assertEquals(nonConformity.getId(), found.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenCompanyDoesNotMatch(){
        Company company = companyRepository.save(
                TestDataFactory.createCompany()
        );
        Sector sector = sectorRepository.save(
                TestDataFactory.createSector(company)
        );
        User user = userRepository.save(
                TestDataFactory.createUser(company, sector)
        );

        NonConformity nc = TestDataFactory.createNonConformity(
                company,
                sector,
                user
        );

        Optional<NonConformity> found = nonconformityRepository.findByIdAndCompanyId(
                nc.getId(),
                UUID.randomUUID()
        );

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenNotFindNonConformity(){
        Optional<NonConformity> found = nonconformityRepository.findByIdAndCompanyId(
                198L,
                UUID.randomUUID()
        );

        assertFalse(found.isPresent());
    }


}
