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
public class PropertyDao extends BaseDao<Property> {
    public Page<Property> findAll(Pageable pageable, Map<String, Object> filters) {
        Objects.requireNonNull(pageable);
        Objects.requireNonNull(filters);

        try {
            List<Property> result = ((TypedQuery<Property>) createFindAllQuery(pageable, filters, false)).getResultList();
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
            cq = cb.createQuery(Property.class);
        }
        Root<Property> property = cq.from(Property.class);

        // Filters
        List<Predicate> predicates = new ArrayList<>();

        ParameterExpression<Enum> status = null;
        if (filters.containsKey("status")) {
            status = cb.parameter(Enum.class);
            predicates.add(cb.equal(property.get("status"), status));
        }

        ParameterExpression<City> city = null;
        if (filters.containsKey("cityId")) {
            city = cb.parameter(City.class);
            predicates.add(cb.equal(property.get("city"), city));
        }

        ParameterExpression<Enum> propertyType = null;
        if (filters.containsKey("propertyType")) {
            propertyType = cb.parameter(Enum.class);
            predicates.add(cb.equal(property.get("type"), propertyType));
        }

        ParameterExpression<Integer> minSquare = null;
        if (filters.containsKey("minSquare")) {
            minSquare = cb.parameter(Integer.class);
            predicates.add(cb.greaterThanOrEqualTo(property.get("square"), minSquare));
        }

        ParameterExpression<Integer> maxSquare = null;
        if (filters.containsKey("maxSquare")) {
            maxSquare = cb.parameter(Integer.class);
            predicates.add(cb.lessThanOrEqualTo(property.get("square"), maxSquare));
        }

        ParameterExpression<Boolean> isAvailable = null;
        if (filters.containsKey("isAvailable")) {
            isAvailable = cb.parameter(Boolean.class);
            predicates.add(cb.equal(property.get("isAvailable"), isAvailable));
        }

        ParameterExpression<Landlord> owner = null;
        if (filters.containsKey("ownerId")) {
            owner = cb.parameter(Landlord.class);
            predicates.add(cb.equal(property.get("owner"), owner));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<?> query;

        if (count) {
            cq.select(cb.count(property));
            query = em.createQuery(cq);
        } else {
            cq.orderBy(QueryUtils.toOrders(pageable.getSort(), property, cb));
            cq.select(property);

            query = em.createQuery(cq)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult((int) pageable.getOffset());
        }

        // Setting up parameters
        if (status != null) {
            query.setParameter(status, PublicationStatus.valueOf(String.valueOf(filters.get("status"))));
        }

        if (city != null) {
            query.setParameter(city, em.getReference(City.class, filters.get("cityId")));
        }

        if (propertyType != null) {
            query.setParameter(propertyType, PropertyType.valueOf(String.valueOf(filters.get("propertyType"))));
        }

        if (minSquare != null) {
            query.setParameter(minSquare, (Integer) filters.get("minSquare"));
        }

        if (maxSquare != null) {
            query.setParameter(maxSquare, (Integer) filters.get("maxSquare"));
        }

        if (isAvailable != null) {
            query.setParameter(isAvailable, (Boolean) filters.get("isAvailable"));
        }

        if (owner != null) {
            query.setParameter(owner, em.getReference(Landlord.class, filters.get("ownerId")));
        }

        return query;
    }
}
