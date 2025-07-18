package com.aec.aec.AuthSrv.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.aec.aec.AuthSrv.model.User;
import com.aec.aec.AuthSrv.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        User usuario = userRepo.findByNombreUsuario(nombreUsuario)
    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));


        return org.springframework.security.core.userdetails.User.builder()
            .username(usuario.getNombreUsuario())
            .password(usuario.getClave())
            //.roles(usuario.getRol().name().substring(5)) 
            .authorities(usuario.getRol().name())
            .build();
    }
}
