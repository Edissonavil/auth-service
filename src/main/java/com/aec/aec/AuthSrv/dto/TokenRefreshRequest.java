package com.aec.aec.AuthSrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenRefreshRequest {

    @JsonProperty("refreshToken")
    private String refreshToken;

    public TokenRefreshRequest() {}

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
