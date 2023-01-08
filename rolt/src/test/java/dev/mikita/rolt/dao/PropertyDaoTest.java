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
public class PropertyDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private PropertyDao propertyDao;

//    @Test
//    public void findAllReturnsOnlyPublishedProperties() {
//        final City city = Generator.generateCity();
//        final Landlord owner = Generator.generateLandlord();
//        em.persist(owner);
//        em.persist(city);
//        em.flush();
//        final List<Property> properties = IntStream.range(0, 10).mapToObj(i -> {
//                    Property p = Generator.generateProperty();
//                    p.setCity(city);
//                    p.setOwner(owner);
//                    return p;
//                })
//                .collect(Collectors.toList());
//
//        Property deletedProperty = Generator.generateProperty();
//        deletedProperty.setOwner(owner);
//        deletedProperty.setCity(city);
//        deletedProperty.setStatus(PublicationStatus.DELETED);
//        properties.add(deletedProperty);
//
//        properties.forEach(em::persist);
//
//        final List<Property> result = propertyDao.findAll();
//        assertEquals(properties.stream().filter(p -> p.getStatus() == PublicationStatus.PUBLISHED).count(), result.size());
//        result.forEach(p -> assertSame(p.getStatus(), PublicationStatus.PUBLISHED));
//    }
//
//    @Test
//    public void findAllAvailableReturnsOnlyPublishedAndAvailableProperties() {
//        final City city = Generator.generateCity();
//        final Landlord owner = Generator.generateLandlord();
//        em.persist(owner);
//        em.persist(city);
//        em.flush();
//        final List<Property> properties = IntStream.range(0, 10).mapToObj(i -> {
//                    Property p = Generator.generateProperty();
//                    p.setCity(city);
//                    p.setOwner(owner);
//                    return p;
//                })
//                .collect(Collectors.toList());
//
//        Property deletedProperty = Generator.generateProperty();
//        deletedProperty.setOwner(owner);
//        deletedProperty.setCity(city);
//        deletedProperty.setStatus(PublicationStatus.DELETED);
//        properties.add(deletedProperty);
//
//        Property unavailableProperty = Generator.generateProperty();
//        unavailableProperty.setOwner(owner);
//        unavailableProperty.setCity(city);
//        unavailableProperty.setStatus(PublicationStatus.DELETED);
//        unavailableProperty.setAvailable(false);
//        properties.add(unavailableProperty);
//
//        properties.forEach(em::persist);
//
//        final List<Property> result = propertyDao.findAllAvailable();
//        assertEquals(properties.stream().filter(p -> p.getStatus() == PublicationStatus.PUBLISHED && p.getAvailable()).count(), result.size());
//        result.forEach(p -> assertTrue(p.getStatus() == PublicationStatus.PUBLISHED && p.getAvailable()));
//    }
//
//    @Test
//    public void findByOwnerReturnsOnlyReturnsPropertiesSpecificOwner() {
//        final City city = Generator.generateCity();
//        final Landlord ownerOne = Generator.generateLandlord();
//        final Landlord ownerTwo = Generator.generateLandlord();
//        em.persist(ownerOne);
//        em.persist(ownerTwo);
//        em.persist(city);
//        em.flush();
//        final List<Property> properties = IntStream.range(0, 10).mapToObj(i -> {
//                    Property p = Generator.generateProperty();
//                    p.setCity(city);
//                    p.setOwner(ownerOne);
//                    return p;
//                })
//                .collect(Collectors.toList());
//
//        for (int i = 0; i < Generator.randomInt(1, 20); i++) {
//            Property p = Generator.generateProperty();
//            p.setCity(city);
//            p.setOwner(ownerTwo);
//            properties.add(p);
//        }
//
//        properties.forEach(em::persist);
//
//        final List<Property> result = propertyDao.findByOwner(ownerOne);
//        assertEquals(properties.stream().filter(p -> p.getOwner().equals(ownerOne)).count(), result.size());
//        result.forEach(p -> assertSame(p.getOwner(), ownerOne));
//    }
}
