package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.PropertyDao;
import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.PublicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class PropertyService {
    private final PropertyDao propertyDao;

    @Autowired
    public PropertyService(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    @Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Property> findAll(Landlord landlord) {
        return propertyDao.findByOwner(landlord);
    }

    @Transactional(readOnly = true)
    public List<Property> findAllPublished(Landlord landlord) {
        return propertyDao.findByOwnerPublished(landlord);
    }

    @Transactional(readOnly = true)
    public List<Property> findAllAvailable() {
        return propertyDao.findAllAvailable();
    }

    @Transactional(readOnly = true)
    public Property find(Integer id) {
        return propertyDao.find(id);
    }

    @Transactional
    public void persist(Property property) {
        propertyDao.persist(property);
    }

    @Transactional
    public void update(Property property) {
        Objects.requireNonNull(property);
        propertyDao.update(property);
    }

    @Transactional
    public void remove(Property property) {
        Objects.requireNonNull(property);
        property.setAvailable(false);
        property.setStatus(PublicationStatus.DELETED);
        propertyDao.update(property);
    }

    @Transactional
    public void publish(Property property) {
        Objects.requireNonNull(property);
        property.setStatus(PublicationStatus.PUBLISHED);
        propertyDao.update(property);
    }

    @Transactional
    public void moderate(Property property) {
        Objects.requireNonNull(property);
        property.setStatus(PublicationStatus.MODERATION);
        propertyDao.update(property);
    }
}
