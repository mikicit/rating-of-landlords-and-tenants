package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Tenant;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TenantDao extends BaseDao<Tenant> {
    @Override
    public List<Tenant> findAll() {
        try {
            return em.createQuery("SELECT t FROM Tenant t WHERE t.status = dev.mikita.rolt.entity.ConsumerStatus.ACTIVE", Tenant.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Tenant> findAllInSearch() {
        try {
            return em.createQuery("SELECT t FROM Tenant t WHERE t.status = dev.mikita.rolt.entity.ConsumerStatus.ACTIVE AND t.inSearch = true", Tenant.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
