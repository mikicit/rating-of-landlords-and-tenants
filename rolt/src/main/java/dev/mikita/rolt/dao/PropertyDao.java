package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class PropertyDao extends BaseDao<Property> {
    @Override
    public List<Property> findAll() {
        try {
            return em.createQuery("SELECT p FROM Property p WHERE p.status = dev.mikita.rolt.entity.PublicationStatus.PUBLISHED", Property.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Property> findAllAvailable() {
        try {
            return em.createQuery("SELECT p FROM Property p WHERE p.status = dev.mikita.rolt.entity.PublicationStatus.PUBLISHED AND p.isAvailable = true", Property.class).getResultList();
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
}
