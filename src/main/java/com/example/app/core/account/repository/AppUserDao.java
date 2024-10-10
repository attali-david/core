package com.example.app.core.account.repository;

import com.example.app.core.account.model.AppUser;

public interface AppUserDao {

    AppUser save(AppUser user);

    AppUser findByEmail(String email);
}
