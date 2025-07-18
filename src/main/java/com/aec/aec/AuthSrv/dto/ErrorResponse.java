package com.aec.aec.AuthSrv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String mensaje;
    private String path;
}
