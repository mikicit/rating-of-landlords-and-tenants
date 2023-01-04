package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.ConsumerStatus;
import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LandlordDao extends BaseDao<Landlord> {
    @Override
    public List<Landlord> findAll() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Landlord> cq = cb.createQuery(Landlord.class);
            Root<Landlord> landlord = cq.from(Landlord.class);
            ParameterExpression<Enum> s = cb.parameter(Enum.class);

            cq.select(landlord).where(cb.equal(landlord.get("status"), s));
            TypedQuery<Landlord> query = em.createQuery(cq);
            query.setParameter(s, ConsumerStatus.ACTIVE);

            return query.getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
