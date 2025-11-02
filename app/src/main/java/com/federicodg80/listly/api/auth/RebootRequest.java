package com.federicodg80.listly.api.auth;

public class RebootRequest {
    private String UId;
    private String FcmToken;

    public RebootRequest(String uid, String fcmToken) {
        this.UId = uid;
        this.FcmToken = fcmToken;
    }
}
