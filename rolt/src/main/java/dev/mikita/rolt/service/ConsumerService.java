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
    private final ConsumerDao dao;

    @Autowired
    public ConsumerService(ConsumerDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Consumer> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Consumer find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Consumer city) {
        dao.persist(city);
    }

    @Transactional
    public void update(Consumer city) {
        dao.update(city);
    }

    @Transactional
    public void remove(Consumer user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.DELETED);
        dao.update(user);
    }

    @Transactional
    public void block(Consumer user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.BANNED);
        dao.update(user);
    }

    @Transactional
    public void active(Consumer user) {
        Objects.requireNonNull(user);
        user.setStatus(ConsumerStatus.ACTIVE);
        dao.update(user);
    }
}
