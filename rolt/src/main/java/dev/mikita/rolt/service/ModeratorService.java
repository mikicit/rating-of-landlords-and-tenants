package dev.mikita.rolt.service;

import dev.mikita.rolt.dao.ModeratorDao;
import dev.mikita.rolt.entity.Moderator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ModeratorService {
    private final ModeratorDao dao;

    @Autowired
    public ModeratorService(ModeratorDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Moderator> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Moderator find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Moderator user) {
        dao.persist(user);
    }

    @Transactional
    public void update(Moderator user) {
        dao.update(user);
    }

    @Transactional
    public void remove(Moderator user) {
        dao.remove(user);
    }
}
