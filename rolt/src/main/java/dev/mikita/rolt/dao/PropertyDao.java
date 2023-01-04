package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.PublicationStatus;
import dev.mikita.rolt.entity.Tenant;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;

@Repository
public class PropertyDao extends BaseDao<Property> {
    @Override
    public List<Property> findAll() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Property> cq = cb.createQuery(Property.class);
            Root<Property> property = cq.from(Property.class);
            ParameterExpression<Enum> s = cb.parameter(Enum.class);

            cq.select(property).where(cb.equal(property.get("status"), s));
            TypedQuery<Property> query = em.createQuery(cq);
            query.setParameter(s, PublicationStatus.PUBLISHED);

            return query.getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Property> findAllAvailable() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Property> cq = cb.createQuery(Property.class);
            Root<Property> property = cq.from(Property.class);
            ParameterExpression<Enum> s = cb.parameter(Enum.class);
            ParameterExpression<Boolean> a = cb.parameter(Boolean.class);

            Predicate isPublished = cb.equal(property.get("status"), PublicationStatus.PUBLISHED);
            Predicate isAvailable = cb.isTrue(property.get("is_available"));

            cq.select(property).where(cb.and(isPublished, isAvailable));
            TypedQuery<Property> query = em.createQuery(cq);

            return query.getResultList();
//            return em.createQuery("SELECT p FROM Property p WHERE p.status = dev.mikita.rolt.entity.PublicationStatus.PUBLISHED AND p.isAvailable = true", Property.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Property> findByOwner(Landlord landlord) {
        Objects.requireNonNull(landlord);
        try {
            return em.createNamedQuery("Property.findByOwner", Property.class).setParameter("owner", landlord)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Property> findByOwnerPublished(Landlord landlord) {
        Objects.requireNonNull(landlord);
        try {
            return em.createNamedQuery("Property.findByOwnerPublished", Property.class).setParameter("owner", landlord)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Property> findFavorites(Tenant tenant) {
        Objects.requireNonNull(tenant);
        try {
            TypedQuery<Property> query = em.createQuery("SELECT p FROM Property p WHERE p.owner = :tenant AND p.status = dev.mikita.rolt.entity.PublicationStatus.PUBLISHED", Property.class);
            query.setParameter("tenant", tenant);
            return query.getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
