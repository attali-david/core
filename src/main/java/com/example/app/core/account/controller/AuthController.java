package com.example.app.core.account.controller;

import com.example.app.core.account.model.AuthResponse;
import com.example.app.core.account.service.AuthService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {
    static Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String service, @RequestBody ObjectNode credentials) throws Exception {
        try {
            AuthResponse authResponse = authService.authenticate(credentials, service);

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestParam String service, @RequestBody ObjectNode credentials) throws Exception {
        try {
            AuthResponse authResponse = authService.register(credentials, service);

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AuthResponse authResponse = authService.refreshToken(request, response);

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
        }
    }
}