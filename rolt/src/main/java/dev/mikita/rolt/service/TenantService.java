package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.TenantDao;
import dev.mikita.rolt.entity.ConsumerStatus;
import dev.mikita.rolt.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TenantService {
    private final TenantDao dao;

    @Autowired
    public TenantService(TenantDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Tenant> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Tenant> findAllInSearch() {
        return dao.findAllInSearch();
    }

    @Transactional(readOnly = true)
    public Tenant find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Tenant user) {
        dao.persist(user);
    }

    @Transactional
    public void update(Tenant user) {
        dao.update(user);
    }

    @Transactional
    public void remove(Tenant user) {
        Objects.requireNonNull(user);
        user.setInSearch(false);
        user.getDetails().setStatus(ConsumerStatus.DELETED);
        dao.update(user);
    }

    @Transactional
    public void block(Tenant user) {
        Objects.requireNonNull(user);
        user.setInSearch(false);
        user.getDetails().setStatus(ConsumerStatus.BANNED);
        dao.update(user);
    }

    @Transactional
    public void active(Tenant user) {
        Objects.requireNonNull(user);
        user.getDetails().setStatus(ConsumerStatus.ACTIVE);
        dao.update(user);
    }
}
