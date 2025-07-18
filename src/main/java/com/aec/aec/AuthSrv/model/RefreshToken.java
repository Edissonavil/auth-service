package com.aec.aec.AuthSrv.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // getters y setters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public Instant getExpiryDate() { return expiryDate; }
    public Long getUserId() { return userId; }

    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
    public void setUserId(Long userId) { this.userId = userId; }
}
