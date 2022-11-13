package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LandlordDao extends BaseDao<Landlord> {
    @Override
    public List<Landlord> findAll() {
        try {
            return em.createQuery("SELECT l FROM Landlord l WHERE l.details.status = dev.mikita.rolt.entity.ConsumerStatus.ACTIVE", Landlord.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
