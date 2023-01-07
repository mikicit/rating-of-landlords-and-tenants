package dev.mikita.rolt.service.security;

import dev.mikita.rolt.dao.UserDao;
import dev.mikita.rolt.entity.User;
import dev.mikita.rolt.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found.");
        }
        return new CustomUserDetails(user);
    }
}
