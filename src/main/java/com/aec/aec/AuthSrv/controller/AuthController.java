package com.aec.aec.AuthSrv.controller;

import com.aec.aec.AuthSrv.dto.AuthResponse;
import com.aec.aec.AuthSrv.model.RefreshToken;
import com.aec.aec.AuthSrv.model.User;
import com.aec.aec.AuthSrv.repository.RefreshTokenRepository;
import com.aec.aec.AuthSrv.repository.UserRepository;
import com.aec.aec.AuthSrv.util.JwtUtil;
import com.aec.aec.AuthSrv.dto.TokenRefreshRequest;
import com.aec.aec.AuthSrv.dto.LoginRequest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshRepo;
    private final UserRepository userRepo;


    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          RefreshTokenRepository refreshRepo,
                          UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepo = refreshRepo;
        this.userRepo = userRepo;
    }

    @Value("${jwt.accessMs}")
    private long jwtAccessMs;
    @Value("${jwt.refreshMs}")
    private long jwtRefreshMs;

    

// src/main/java/com/aec/aec/AuthSrv/controller/AuthController.java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    try {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.getNombreUsuario(), req.getClave())
        );
    } catch (BadCredentialsException | DisabledException ex) {
        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
    }

    /* 1️⃣  Recupera el usuario para conocer su rol */
    User user = userRepo.findByNombreUsuario(req.getNombreUsuario())
    .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error al recuperar datos de usuario"));

String roleName = user.getRol().name();   // «ROL_CLIENTE», «ROL_ADMIN»…

String access  = jwtUtil.generateToken(user.getNombreUsuario(), roleName, jwtAccessMs);
String refresh = jwtUtil.generateToken(user.getNombreUsuario(), roleName, jwtRefreshMs);

    /* 3️⃣  Guarda refresh token y responde */
    RefreshToken rt = new RefreshToken();
    rt.setToken(refresh);
    rt.setUserId(user.getId());
    rt.setExpiryDate(Instant.now().plusMillis(jwtRefreshMs));
    refreshRepo.save(rt);

    return ResponseEntity.ok(new AuthResponse(
        "Inicio de sesión exitoso", access, refresh));
}


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest req) {
        return refreshRepo.findByToken(req.getRefreshToken())
          .filter(rt -> rt.getExpiryDate().isAfter(Instant.now()))
          .flatMap(rt -> userRepo.findById(rt.getUserId())
            .map(user -> {
                String newAccess = jwtUtil.generateToken(user.getNombreUsuario(),
                user.getRol().name(),
                jwtAccessMs);
                AuthResponse body = new AuthResponse(
                    "Token renovado exitosamente",
                    newAccess,
                    rt.getToken()
                );
                return ResponseEntity.ok(body);
            })
          )
          .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido")
          );
    }
}
