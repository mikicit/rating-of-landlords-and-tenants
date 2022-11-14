package dev.mikita.rolt.dao;

import dev.mikita.rolt.App;
import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.environment.Generator;
import dev.mikita.rolt.environment.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackageClasses = App.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class ContractDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ContractDao contractDao;

    @Test
    public void findAllByPropertyReturnsContractsOnlySpecificProperty() {
        final List<Landlord> landlords = IntStream.range(0, 10).mapToObj(i -> Generator.generateLandlord())
                .collect(Collectors.toList());
        landlords.forEach(em::persist);

        final List<Tenant> tenants = IntStream.range(0, 10).mapToObj(i -> Generator.generateTenant())
                .collect(Collectors.toList());
        tenants.forEach(em::persist);

        final List<City> cities = IntStream.range(0, 10).mapToObj(i -> Generator.generateCity()).collect(Collectors.toList());
        cities.forEach(em::persist);

        final List<Property> properties = IntStream.range(0, 10).mapToObj(i -> {
            Property p = Generator.generateProperty();
            p.setOwner(landlords.get(Generator.randomInt(0, landlords.size() - 1)));
            p.setCity(cities.get(Generator.randomInt(0, cities.size() - 1)));

            return p;
        }).collect(Collectors.toList());
        properties.forEach(em::persist);

        final List<Contract> contracts = IntStream.range(0, 10).mapToObj(i -> {
            final Property p = properties.get(Generator.randomInt(0, properties.size() - 1));
            Contract contract = new Contract();

            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(new Date());
            startDate.add(Calendar.DATE, i);
            endDate.setTime(new Date());
            endDate.add(Calendar.DATE, i + 1);

            contract.setStartDate(startDate.getTime());
            contract.setEndDate(endDate.getTime());
            contract.setLandlord(p.getOwner());
            contract.setTenant(tenants.get(Generator.randomInt(0, tenants.size() - 1)));
            contract.setProperty(p);

            return contract;
        }).collect(Collectors.toList());
        contracts.forEach(em::persist);

        final Property randomProperty = properties.get(Generator.randomInt(0, properties.size() - 1));
        final List<Contract> result = contractDao.findByProperty(randomProperty);
        result.forEach(c -> assertEquals(randomProperty, c.getProperty()));
    }
}
