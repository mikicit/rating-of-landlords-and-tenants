package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.Contract;
import dev.mikita.rolt.entity.Property;
import dev.mikita.rolt.entity.User;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ContractDao extends BaseDao<Contract> {
    public List<Contract> findByProperty(Property property) {
        Objects.requireNonNull(property);
        try {
            return em.createNamedQuery("Contract.findByProperty", Contract.class).setParameter("property", property)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Contract> findByUser(User user) {
        Objects.requireNonNull(user);
        try {
            return em.createNamedQuery("Contract.findByUser", Contract.class).setParameter("user", user)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public List<Contract> findIntersectionsByDateRange(Property property, LocalDate start, LocalDate end) {
        Objects.requireNonNull(property);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        try {
            return em.createNamedQuery("Contract.findIntersectionsByDateRange", Contract.class)
                    .setParameter("property", property)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
