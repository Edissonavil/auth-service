package com.aec.aec.AuthSrv.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    // Getters
    public Long getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getClave() { return clave; }
    public Rol getRol() { return rol; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setClave(String clave) { this.clave = clave; }
    public void setRol(Rol rol) { this.rol = rol; }
}

