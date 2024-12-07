package com.example.app.core.auth.repository;

import com.example.app.core.auth.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class JpaAppUserDao implements AppUserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaAppUserDao.class.getName());

    private AppUserRepository userRepository;

    public JpaAppUserDao(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser save(AppUser user) {
       return userRepository.save(user);
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public AppUser findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

}
