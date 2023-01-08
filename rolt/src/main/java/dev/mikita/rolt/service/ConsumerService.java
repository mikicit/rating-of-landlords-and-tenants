package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ConsumerDao;
import dev.mikita.rolt.entity.Consumer;
import dev.mikita.rolt.entity.ConsumerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ConsumerService {
    private final ConsumerDao consumerDao;

    @Autowired
    public ConsumerService(ConsumerDao consumerDao) {
        this.consumerDao = consumerDao;
    }

    @Transactional(readOnly = true)
    public List<Consumer> findAll() {
        return consumerDao.findAll();
    }

    @Transactional(readOnly = true)
    public Consumer find(Integer id) {
        return consumerDao.find(id);
    }

    @Transactional(readOnly = true)
    public Double getRating(Consumer consumer) {
        return consumerDao.getRating(consumer);
    }

    @Transactional
    public void persist(Consumer city) {
        consumerDao.persist(city);
    }

    @Transactional
    public void update(Consumer city) {
        consumerDao.update(city);
    }

    @Transactional
    public void remove(Consumer user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.DELETED);
        consumerDao.update(user);
    }

    @Transactional
    public void block(Consumer user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.BANNED);
        consumerDao.update(user);
    }

    @Transactional
    public void active(Consumer user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.ACTIVE);
        consumerDao.update(user);
    }
}
