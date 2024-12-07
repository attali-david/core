package com.example.app.core.auth.repository;

import com.example.app.core.auth.model.AppUser;

public interface AppUserDao {

    AppUser save(AppUser user);

    AppUser findByEmail(String email);

    AppUser findById(Long userId);
}
