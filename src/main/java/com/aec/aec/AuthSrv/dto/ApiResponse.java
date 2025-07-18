package com.aec.aec.AuthSrv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper genérico para respuestas que incluyen un mensaje y un payload opcional.
 */
@Getter @Setter @AllArgsConstructor
public class ApiResponse<T> {
    private String mensaje;
    private T dato;
}
