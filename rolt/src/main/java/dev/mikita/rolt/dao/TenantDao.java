package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Tenant;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TenantDao extends BaseDao<Tenant> {
    @Override
    public List<Tenant> findAll() {
        try {
            return em.createQuery("SELECT t FROM Tenant t WHERE t.details.status = dev.mikita.rolt.entity.ConsumerStatus.ACTIVE", Tenant.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
