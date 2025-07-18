package com.aec.aec.AuthSrv.repository;

import com.aec.aec.AuthSrv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNombreUsuario(String nombreUsuario);
}
