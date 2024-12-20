package com.example.app.core.auth.service;

import com.example.app.core.auth.model.AppUser;
import com.example.app.core.auth.model.AuthResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface AuthService {
    AuthResponse authenticate(ObjectNode credentials, String service) throws Exception;

    AuthResponse register(AppUser user) throws Exception;

    AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    GoogleIdToken.Payload verifyGoogleToken(String idTokenString) throws Exception;

    Map<String, Object> verifyAppleToken(String idToken) throws Exception;
}
