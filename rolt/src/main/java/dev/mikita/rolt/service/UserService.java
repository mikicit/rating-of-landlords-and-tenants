package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.UserDao;
import dev.mikita.rolt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserDao dao;

    @Autowired
    public UserService(UserDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public User find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(User user) {
        dao.persist(user);
    }

    @Transactional
    public void update(User user) {
        dao.update(user);
    }

    @Transactional
    public void remove(User user) {
        dao.remove(user);
    }
}
