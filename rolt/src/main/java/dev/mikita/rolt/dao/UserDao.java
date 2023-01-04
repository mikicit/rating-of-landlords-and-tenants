package dev.mikita.rolt.dao;

import dev.mikita.rolt.entity.User;
import dev.mikita.rolt.exception.PersistenceException;
import org.springframework.stereotype.Repository;
import java.util.Objects;

@Repository
public class UserDao extends BaseDao<User> {
    public User findByEmail(String email) {
        Objects.requireNonNull(email);
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email)
                    .getSingleResult();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
