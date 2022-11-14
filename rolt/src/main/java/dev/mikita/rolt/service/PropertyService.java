package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.PropertyDao;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.PublicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PropertyService {
    private final PropertyDao dao;

    @Autowired
    public PropertyService(PropertyDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Property> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Property find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Property property) {
        dao.persist(property);
    }

    @Transactional
    public void update(Property property) {
        Objects.requireNonNull(property);
        property.setUpdatedOn(new Date());
        dao.update(property);
    }

    @Transactional
    public void remove(Property property) {
        Objects.requireNonNull(property);
        property.setAvailable(false);
        property.setStatus(PublicationStatus.DELETED);
        dao.update(property);
    }

    @Transactional
    public void publish(Property property) {
        Objects.requireNonNull(property);
        property.setStatus(PublicationStatus.PUBLISHED);
        dao.update(property);
    }

    @Transactional
    public void moderate(Property property) {
        Objects.requireNonNull(property);
        property.setStatus(PublicationStatus.MODERATION);
        dao.update(property);
    }
}
