package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.City;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;

@Repository
public class CityDao extends BaseDao<City> {
    public Page<City> findAll(Pageable pageable, String name) {
        Objects.requireNonNull(pageable);

        try {
            List<City> result = ((TypedQuery<City>) createFindAllQuery(pageable, name, false)).getResultList();
            Long count = ((TypedQuery<Long>) createFindAllQuery(pageable, name, true)).getSingleResult();

            return new PageImpl<>(result, pageable, count);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    private TypedQuery<?> createFindAllQuery(Pageable pageable, String name, boolean count) {
        Objects.requireNonNull(pageable);

        // Main Query
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq;
        if (count) {
            cq = cb.createQuery(Long.class);
        } else {
            cq = cb.createQuery(City.class);
        }
        Root<City> city = cq.from(City.class);

        ParameterExpression<String> cityName = null;
        if (name != null) {
            cityName = cb.parameter(String.class);
            Predicate equalsName = cb.equal(city.get("name"), cityName);
            cq.where(cb.and(equalsName));
        }

        TypedQuery<City> query;

        if (count) {
            cq.select(cb.count(city));
            query = em.createQuery(cq);
        } else {
            cq.orderBy(QueryUtils.toOrders(pageable.getSort(), city, cb));
            cq.select(city);

            query = em.createQuery(cq)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult((int) pageable.getOffset());
        }

        if (cityName != null) {
            query.setParameter(cityName, name);
        }

        return query;
    }
}
