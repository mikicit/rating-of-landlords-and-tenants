package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Consumer;
import dev.mikita.rolt.entity.Review;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Objects;

@Repository
public class ReviewDao extends BaseDao<Review> {
    public List<Review> findByAuthor(Consumer consumer) {
        Objects.requireNonNull(consumer);
        try {
            return em.createNamedQuery("Review.findByAuthor", Review.class).setParameter("user", consumer)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Review> findByReviewed(Consumer consumer) {
        Objects.requireNonNull(consumer);
        try {
            return em.createNamedQuery("Review.findByReviewed", Review.class).setParameter("user", consumer)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
