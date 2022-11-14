package dev.mikita.rolt.dao;

import dev.mikita.rolt.App;
import dev.mikita.rolt.entity.ConsumerStatus;
import dev.mikita.rolt.entity.Tenant;
import dev.mikita.rolt.environment.Generator;
import dev.mikita.rolt.environment.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackageClasses = App.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class TenantDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private TenantDao tenantDao;

    @Test
    public void findAllReturnsOnlyActiveTenants() {
        final List<Tenant> tenants = IntStream.range(0, 10).mapToObj(i -> Generator.generateTenant())
                .collect(Collectors.toList());

        Tenant bannedTenant = Generator.generateTenant();
        bannedTenant.getDetails().setStatus(ConsumerStatus.BANNED);
        tenants.add(bannedTenant);

        tenants.forEach(em::persist);

        final List<Tenant> result = tenantDao.findAll();
        assertEquals(tenants.stream().filter(t -> t.getDetails().getStatus() == ConsumerStatus.ACTIVE).count(), result.size());
        result.forEach(t -> assertSame(t.getDetails().getStatus(), ConsumerStatus.ACTIVE));
    }

    @Test
    public void findAllInSearchReturnsOnlyActiveAndInSearchTenants() {
        final List<Tenant> tenants = IntStream.range(0, 10).mapToObj(i -> {
                    Tenant t = Generator.generateTenant();
                    t.setInSearch(true);
                    return t;
                })
                .collect(Collectors.toList());

        Tenant bannedTenant = Generator.generateTenant();
        bannedTenant.getDetails().setStatus(ConsumerStatus.BANNED);
        tenants.add(bannedTenant);

        Tenant notInSearchTenant = Generator.generateTenant();
        tenants.add(notInSearchTenant);

        tenants.forEach(em::persist);

        final List<Tenant> result = tenantDao.findAllInSearch();
        assertEquals(tenants.stream().filter(t -> t.getDetails().getStatus() == ConsumerStatus.ACTIVE && t.getInSearch()).count(), result.size());
        result.forEach(t -> assertTrue(t.getDetails().getStatus() == ConsumerStatus.ACTIVE && t.getInSearch()));
    }
}
