package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.UserDao;
import dev.mikita.rolt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public User find(Integer id) {
        return userDao.find(id);
    }

    @Transactional
    public void persist(User user) {
        userDao.persist(user);
    }

    @Transactional
    public void update(User user) {
        userDao.update(user);
    }

    @Transactional
    public void remove(User user) {
        userDao.remove(user);
    }

    @Transactional(readOnly = true)
    public boolean exists(String email) {
        return userDao.findByEmail(email) != null;
    }
}
