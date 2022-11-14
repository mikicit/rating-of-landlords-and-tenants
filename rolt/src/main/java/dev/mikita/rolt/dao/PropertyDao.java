package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
