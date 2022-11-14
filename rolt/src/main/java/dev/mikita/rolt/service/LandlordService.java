package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.LandlordDao;
import dev.mikita.rolt.entity.ConsumerStatus;
import dev.mikita.rolt.entity.Landlord;
import dev.mikita.rolt.entity.PublicationStatus;
import dev.mikita.rolt.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class LandlordService {
    private final LandlordDao dao;

    @Autowired
    public LandlordService(LandlordDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Landlord> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Landlord find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Landlord user) {
        dao.persist(user);
    }

    @Transactional
    public void update(Landlord user) {
        dao.update(user);
    }

    @Transactional
    public void remove(Landlord user) {
        Objects.requireNonNull(user);
        user.getDetails().setStatus(ConsumerStatus.DELETED);
        dao.update(user);
    }

    @Transactional
    public void block(Landlord user) {
        Objects.requireNonNull(user);
        user.getDetails().setStatus(ConsumerStatus.BANNED);
        user.getProperties().forEach(p -> p.setStatus(PublicationStatus.DELETED));
        dao.update(user);
    }

    @Transactional
    public void active(Landlord user) {
        Objects.requireNonNull(user);
        user.getDetails().setStatus(ConsumerStatus.ACTIVE);
        dao.update(user);
    }
}
