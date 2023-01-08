package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ModeratorDao;
import dev.mikita.rolt.dao.UserDao;
import dev.mikita.rolt.entity.Moderator;
import dev.mikita.rolt.entity.Role;
import dev.mikita.rolt.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ModeratorService {
    private final ModeratorDao moderatorDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ModeratorService(ModeratorDao moderatorDao,
                            PasswordEncoder passwordEncoder,
                            UserDao userDao) {
        this.moderatorDao = moderatorDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Moderator> findAll() {
        return moderatorDao.findAll();
    }

    @Transactional(readOnly = true)
    public Moderator find(Integer id) {
        return moderatorDao.find(id);
    }

    @Transactional
    public void persist(Moderator user) {
        Objects.requireNonNull(user);
        if (userDao.findByEmail(user.getEmail()) == null) {
            throw new ValidationException("A user with this email already exists.");
        }

        user.setRole(Role.MODERATOR);
        user.encodePassword(passwordEncoder);
        moderatorDao.persist(user);
    }

    @Transactional
    public void update(Moderator user) {
        moderatorDao.update(user);
    }

    @Transactional
    public void remove(Moderator user) {
        moderatorDao.remove(user);
    }
}
