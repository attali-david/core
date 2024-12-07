package com.example.app.core.auth.service;
import com.example.app.core.auth.model.AppUser;
import com.example.app.core.auth.model.AuthResponse;
import com.example.app.core.auth.model.Token;
import com.example.app.core.auth.repository.AppUserDao;
import com.example.app.core.auth.repository.TokenRepository;
import com.example.app.core.config.JwtService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.net.HttpHeaders;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// TODO: Handle users attempting to create accounts with existing emails
// TODO: Add refresh token

@Service
public class AuthServiceImpl implements AuthService {
    final static Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());
    final static String GOOGLE = "google";
    final static String APPLE = "apple";
    final static String EMAIL = "email";

    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    private final HttpTransport transport = new NetHttpTransport();
    private final JsonFactory jsonFactory = new GsonFactory();
    private final PasswordEncoder passwordEncoder;
    private final AppUserDao userDao;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           AppUserDao userDao,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           TokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.authManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public AuthResponse authenticate(ObjectNode credentials, String service) throws Exception {
        String email = null;
        if(GOOGLE.equalsIgnoreCase(service)) {
            // This should handle exceptions and bad credentials
            Payload googlePayload = this.verifyGoogleToken(credentials.get("credential").toString());
            email = googlePayload.getEmail();
        } else if(APPLE.equalsIgnoreCase(service)) {
            Map<String, Object> applePayload = this.verifyAppleToken(credentials.get("credential").toString());
            email = (String) applePayload.get("email");
        } else if(EMAIL.equalsIgnoreCase(service)) {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.get("email"), credentials.get("password")
                    ));
        }

        if (email == null) {
            throw new Exception("Invalid authentication service");
        }

        AppUser user = userDao.findByEmail(credentials.get("email").toString());

        return renewUserTokens(user);
    }

    @Override
    public AuthResponse register(AppUser user) throws Exception {
        // TODO: Add check or throw exception if user exist
        userDao.save(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, refreshToken);

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String userEmail;
        String refreshToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = Optional.of(userDao.findByEmail(userEmail))
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                return renewUserTokens(user);
            }
        }
        return null;
    }

    private AuthResponse renewUserTokens(AppUser user) {
        // Delete existing refresh tokens
        tokenRepository.deleteAllByUser(user.getId());
        // Generate new tokens
        String refreshToken = jwtService.generateRefreshToken(user);
        String token = jwtService.generateToken(user);
        // Save the new refresh token
        saveUserToken(user, refreshToken);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .build();

        tokenRepository.save(token);
    }

    public Payload verifyGoogleToken(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(System.getenv("GOOGLE_SERVER_ID")))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
            return null;
        } catch (Exception exception) {
            throw new Exception("Unable to verify Google token {}", exception);
        }
    }

    public Map<String, Object> verifyAppleToken(String idToken) throws Exception {
        try {
            // Fetch Apple public keys
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(APPLE_KEYS_URL, Map.class);

            JWKSet jwkSet = JWKSet.parse(response);

            // Decode the ID token to extract the key ID (kid)
            JWSObject jwsObject = JWSObject.parse(idToken);
            JWSHeader header = jwsObject.getHeader();

            String kid = header.getKeyID();

            // Find the corresponding public key
            JWK applePublicKey = jwkSet.getKeyByKeyId(kid);

            if (applePublicKey == null || !(applePublicKey instanceof RSAKey)) {
                throw new IllegalArgumentException("Invalid key ID or key type.");
            }

            // Cast JWK to RSAKey and create the verifier
            RSAKey rsaKey = (RSAKey) applePublicKey;
            JWSVerifier verifier = new RSASSAVerifier(rsaKey);

            // Verify the token
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            boolean isValid = signedJWT.verify(verifier);
            if (!isValid) {
                return null;
            }

            Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            // Check if token is expired
            if (new Date().after(expiration)) {
                return null;
            }

            return claims;
        } catch (Exception exception) {
            throw new Exception("Unable to verify Apple token: {}", exception);
        }
    }
}
