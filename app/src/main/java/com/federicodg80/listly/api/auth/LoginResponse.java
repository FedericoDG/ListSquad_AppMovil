package com.federicodg80.listly.api.auth;

public class LoginResponse {
    public String UId;
    public String Token;

    public LoginResponse(String UId, String token) {
        this.UId = UId;
        this.Token = token;
    }

    public String getUid() {
        return UId;
    }

    public String getToken() {
        return Token;
    }
}

