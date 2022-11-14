package dev.mikita.rolt.dao;

import dev.mikita.rolt.App;
import dev.mikita.rolt.entity.*;
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
public class LandlordDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private LandlordDao landlordDao;

    @Autowired
    private PropertyDao propertyDao;

    @Test
    public void findAllReturnsOnlyActiveLandlords() {
        final List<Landlord> landlords = IntStream.range(0, 10).mapToObj(i -> Generator.generateLandlord())
                .collect(Collectors.toList());

        Landlord bannedLandlord = Generator.generateLandlord();
        bannedLandlord.getDetails().setStatus(ConsumerStatus.BANNED);
        landlords.add(bannedLandlord);

        landlords.forEach(em::persist);

        final List<Landlord> result = landlordDao.findAll();
        assertEquals(landlords.stream().filter(t -> t.getDetails().getStatus() == ConsumerStatus.ACTIVE).count(), result.size());
        result.forEach(t -> assertSame(t.getDetails().getStatus(), ConsumerStatus.ACTIVE));
    }

    @Test
    public void afterAddingPropertyPropertyExists() {
        final Landlord owner = Generator.generateLandlord();
        final Property property = Generator.generateProperty();

        owner.addProperty(property);
        landlordDao.persist(owner);

        assertTrue(propertyDao.exists(property.getId()));
    }

    @Test
    public void afterRemovingPropertyPropertyHasStatusRemoved() {
        final Landlord owner = Generator.generateLandlord();
        final Property property = Generator.generateProperty();
        final City city = Generator.generateCity();
        em.persist(city);
        property.setCity(city);

        owner.addProperty(property);
        landlordDao.persist(owner);
        owner.removeProperty(property);
        landlordDao.update(owner);
        em.flush();

        Property result = propertyDao.find(property.getId());

        assertNotNull(result);
        assertSame(result.getStatus(), PublicationStatus.DELETED);
    }
}
