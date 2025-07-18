package com.aec.aec.AuthSrv.exception;

import com.aec.aec.AuthSrv.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex,
                                                               HttpServletRequest req) {
        // Obtenemos el HttpStatus directamente
        HttpStatus status = ex.getStatusCode().is4xxClientError() || ex.getStatusCode().is5xxServerError()
            ? HttpStatus.valueOf(ex.getStatusCode().value())
            : HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse err = new ErrorResponse(
            Instant.now(),
            status.value(),
            ex.getReason(),
            req.getRequestURI()
        );
        return new ResponseEntity<>(err, status);
    }
}
