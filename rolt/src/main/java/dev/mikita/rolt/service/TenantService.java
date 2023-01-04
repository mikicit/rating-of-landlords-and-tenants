package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.PropertyDao;
import dev.mikita.rolt.dao.TenantDao;
import dev.mikita.rolt.entity.ConsumerStatus;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.Role;
import dev.mikita.rolt.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class TenantService {
    private final TenantDao tenantDao;
    private final PropertyDao propertyDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TenantService(TenantDao tenantDao,
                         PasswordEncoder passwordEncoder,
                         PropertyDao propertyDao) {
        this.tenantDao = tenantDao;
        this.passwordEncoder = passwordEncoder;
        this.propertyDao = propertyDao;
    }

    @Transactional(readOnly = true)
    public List<Tenant> findAll() {
        return tenantDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Tenant> findAllInSearch() {
        return tenantDao.findAllInSearch();
    }

    @Transactional(readOnly = true)
    public Tenant find(Integer id) {
        return tenantDao.find(id);
    }

    @Transactional
    public void persist(Tenant user) {
        user.setRole(Role.TENANT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        tenantDao.persist(user);
    }

    @Transactional
    public void update(Tenant user) {
        tenantDao.update(user);
    }

    @Transactional
    public void remove(Tenant user) {
        Objects.requireNonNull(user);
        user.setInSearch(false);
        user.setStatus(ConsumerStatus.DELETED);
        tenantDao.update(user);
    }

    @Transactional
    public List<Property> getFavorites(Tenant user) {
        Objects.requireNonNull(user);
        return propertyDao.findFavorites(user);
    }

    @Transactional
    public void addFavorite(Property property, Tenant user) {
        Objects.requireNonNull(property);
        Objects.requireNonNull(user);
        user.addFavorite(property);
        tenantDao.update(user);
    }

    @Transactional
    public void removeFavorite(Property property, Tenant user) {
        Objects.requireNonNull(property);
        Objects.requireNonNull(user);
        user.removeFavorite(property);
        tenantDao.update(user);
    }

    @Transactional
    public void block(Tenant user) {
        Objects.requireNonNull(user);
        user.setInSearch(false);
        user.setStatus(ConsumerStatus.BANNED);
        tenantDao.update(user);
    }

    @Transactional
    public void active(Tenant user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.ACTIVE);
        tenantDao.update(user);
    }
}
