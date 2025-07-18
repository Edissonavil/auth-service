package com.aec.aec.AuthSrv.dto;
import jakarta.validation.constraints.NotBlank;
import com.aec.aec.AuthSrv.dto.TokenRefreshRequest;

import lombok.Data;
@Data
public class LoginRequest {
     @NotBlank private String nombreUsuario;
    @NotBlank private String clave;

      // getters y setters
      public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
}


