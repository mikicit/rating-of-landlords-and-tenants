package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Consumer;
import dev.mikita.rolt.entity.Review;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ConsumerDao extends BaseDao<Consumer> {
    public Double getRating(Consumer consumer) {
        Objects.requireNonNull(consumer);

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<Review> reviewRoot = cq.from(Review.class);

            List<Predicate> predicates = new ArrayList<>();

            ParameterExpression<Consumer> reviewed = cb.parameter(Consumer.class);

            Predicate tenantOrOwner = cb.or(
                    cb.equal(reviewRoot.get("contract").get("tenant"), reviewed),
                    cb.equal(reviewRoot.get("contract").get("property").get("owner"), reviewed));
            Predicate notEqualPredicate = cb.notEqual(reviewRoot.get("author"), reviewed);

            predicates.add(tenantOrOwner);
            predicates.add(notEqualPredicate);

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            Expression<Double> avgRating = cb.avg(reviewRoot.get("rating"));
            cq.select(avgRating);
            TypedQuery<Double> query = em.createQuery(cq);
            query.setParameter(reviewed, consumer);

            return query.getSingleResult();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
