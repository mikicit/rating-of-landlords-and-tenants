package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ModeratorDao;
import dev.mikita.rolt.entity.Moderator;
import dev.mikita.rolt.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ModeratorService {
    private final ModeratorDao moderatorDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ModeratorService(ModeratorDao moderatorDao, PasswordEncoder passwordEncoder) {
        this.moderatorDao = moderatorDao;
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
        user.setRole(Role.MODERATOR);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
