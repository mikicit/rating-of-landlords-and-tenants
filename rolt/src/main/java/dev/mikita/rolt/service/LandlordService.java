package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.LandlordDao;
import dev.mikita.rolt.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Objects;

@Service
public class LandlordService {
    private final LandlordDao landlordDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LandlordService(LandlordDao landlordDao, PasswordEncoder passwordEncoder) {
        this.landlordDao = landlordDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<Landlord> findAll(Pageable pageable, Map<String, Object> filters) {
        return landlordDao.findAll(pageable, filters);
    }

    @Transactional(readOnly = true)
    public Landlord find(Integer id) {
        return landlordDao.find(id);
    }

    @Transactional
    public void persist(Landlord user) {
        user.setRole(Role.LANDLORD);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        landlordDao.persist(user);
    }

    @Transactional
    public void update(Landlord user) {
        landlordDao.update(user);
    }

    @Transactional
    public void remove(Landlord user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.DELETED);
        landlordDao.update(user);
    }

    @Transactional
    public void block(Landlord user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.BANNED);
        user.getProperties().forEach(p -> p.setStatus(PublicationStatus.DELETED));
        landlordDao.update(user);
    }

    @Transactional
    public void active(Landlord user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.ACTIVE);
        landlordDao.update(user);
    }
}
