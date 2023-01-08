package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.*;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class ReviewDao extends BaseDao<Review> {
    public Page<Review> findAll(Pageable pageable, Map<String, Object> filters) {
        Objects.requireNonNull(pageable);
        Objects.requireNonNull(filters);

        try {
            List<Review> result = ((TypedQuery<Review>) createFindAllQuery(pageable, filters, false)).getResultList();
            Long count = ((TypedQuery<Long>) createFindAllQuery(pageable, filters, true)).getSingleResult();

            return new PageImpl<>(result, pageable, count);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    private TypedQuery<?> createFindAllQuery(Pageable pageable, Map<String, Object> filters, boolean count) {
        Objects.requireNonNull(pageable);
        Objects.requireNonNull(filters);

        // Main Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq;
        if (count) {
            cq = cb.createQuery(Long.class);
        } else {
            cq = cb.createQuery(Review.class);
        }
        Root<Review> review = cq.from(Review.class);

        // Filters
        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Enum> status = null;
        if (filters.containsKey("status")) {
            status = cb.parameter(Enum.class);
            predicates.add(cb.equal(review.get("status"), status));
        }

        ParameterExpression<Consumer> author = null;
        if (filters.containsKey("authorId")) {
            author = cb.parameter(Consumer.class);
            predicates.add(cb.equal(review.get("author"), author));
        }

        ParameterExpression<Consumer> reviewed = null;
        if (filters.containsKey("reviewedId")) {
            reviewed = cb.parameter(Consumer.class);

            Predicate tenantOrOwner = cb.or(
                    cb.equal(review.get("contract").get("tenant"), reviewed),
                    cb.equal(review.get("contract").get("property").get("owner"), reviewed));
            Predicate notEqualPredicate = cb.notEqual(review.get("author"), reviewed);

            predicates.add(tenantOrOwner);
            predicates.add(notEqualPredicate);
        }

        ParameterExpression<Contract> contract = null;
        if (filters.containsKey("contractId")) {
            contract = cb.parameter(Contract.class);
            predicates.add(cb.equal(review.get("contract"), contract));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<?> query;

        if (count) {
            cq.select(cb.count(review));
            query = em.createQuery(cq);
        } else {
            cq.orderBy(QueryUtils.toOrders(pageable.getSort(), review, cb));
            cq.select(review);

            query = em.createQuery(cq)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult((int) pageable.getOffset());
        }

        // Setting up parameters
        if (status != null) {
            query.setParameter(status, PublicationStatus.valueOf(String.valueOf(filters.get("status"))));
        }

        // Setting up parameters
        if (author != null) {
            query.setParameter(author, em.getReference(Consumer.class, filters.get("authorId")));
        }

        // Setting up parameters
        if (reviewed != null) {
            query.setParameter(reviewed, em.getReference(Consumer.class, filters.get("reviewedId")));
        }

        // Setting up parameters
        if (contract != null) {
            query.setParameter(contract, em.getReference(Contract.class, filters.get("contractId")));
        }

        return query;
    }
}
