package com.example.app.core.account.repository;

import com.example.app.core.account.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public class JpaAppUserDao implements AppUserDao {
    static Logger LOGGER = LoggerFactory.getLogger(JpaAppUserDao.class.getName());

    @Autowired
    private AppUserRepository userRepository;

    public AppUser save(AppUser user) {
        try {
           AppUser savedUser = userRepository.save(user);
           return savedUser;
        } catch (Exception exception) {
            LOGGER.error("JpaUserDao : error in save = {}", exception);
            throw exception;
        }
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
