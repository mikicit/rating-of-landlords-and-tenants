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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
@ComponentScan(basePackageClasses = App.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class ReviewDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReviewDao reviewDao;

    @Test
    public void findByAuthorReturnsReviewsFromCertainConsumer() {
        final Tenant author = Generator.generateTenant();
        em.persist(author);
        final Landlord notAuthor = Generator.generateLandlord();
        em.persist(notAuthor);
        final City city = Generator.generateCity();
        em.persist(city);
        final Property property = Generator.generateProperty();
        property.setCity(city);
        property.setOwner(notAuthor);
        em.persist(property);
        final Contract contract = Generator.generateContract();
        contract.setProperty(property);
        contract.setTenant(author);
        em.persist(contract);

        final Review reviewOne = Generator.generateReview();
        reviewOne.setContract(contract);
        reviewOne.setAuthor(author);
        reviewDao.persist(reviewOne);

        final Review reviewTwo = Generator.generateReview();
        reviewTwo.setContract(contract);
        reviewTwo.setAuthor(notAuthor);
        reviewDao.persist(reviewTwo);

        em.flush();

        List<Review> result = reviewDao.findByAuthor(author);
        assertEquals(1, result.size());
        result.forEach(r -> assertSame(r.getAuthor(), author));
    }
}
