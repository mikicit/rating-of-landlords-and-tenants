package dev.mikita.rolt.dao;

import dev.mikita.rolt.App;
import dev.mikita.rolt.environment.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@DataJpaTest
@ComponentScan(basePackageClasses = App.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class PropertyDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private PropertyDao propertyDao;


}
