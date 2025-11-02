package com.federicodg80.listly.api.auth;

public class LoginRequest {
    private String UId;
    private String Email;
    private String DisplayName;
    private String PhotoUrl;
    private String ProviderId;
    private String FcmToken;

    public LoginRequest(String uid, String email, String displayName, String photoUrl, String providerId, String fcmToken) {
        this.UId = uid;
        this.Email = email;
        this.DisplayName = displayName;
        this.PhotoUrl = photoUrl;
        this.ProviderId = providerId;
        this.FcmToken = fcmToken;
    }
}