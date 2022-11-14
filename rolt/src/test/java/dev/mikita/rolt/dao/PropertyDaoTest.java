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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
@ComponentScan(basePackageClasses = App.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class PropertyDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private PropertyDao propertyDao;

    @Test
    public void findAllReturnsOnlyPublishedProperties() {
        final City city = Generator.generateCity();
        final Landlord owner = Generator.generateLandlord();
        em.persist(owner);
        em.persist(city);
        em.flush();
        final List<Property> properties = IntStream.range(0, 10).mapToObj(i -> {
                    Property p = Generator.generateProperty();
                    p.setCity(city);
                    p.setOwner(owner);
                    return p;
                })
                .collect(Collectors.toList());
        properties.forEach(em::persist);

        Property deletedProperty = Generator.generateProperty();
        deletedProperty.setOwner(owner);
        deletedProperty.setCity(city);
        deletedProperty.setStatus(PublicationStatus.DELETED);
        em.persist(deletedProperty);

        final List<Property> result = propertyDao.findAll();
        assertEquals(properties.stream().filter(p -> p.getStatus() == PublicationStatus.PUBLISHED).count(), result.size());
        result.forEach(p -> assertSame(p.getStatus(), PublicationStatus.PUBLISHED));
    }
}
