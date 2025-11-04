package com.federicodg80.listly.models;

import androidx.annotation.NonNull;

public class User {
    private final String UId;
    private final String Email;
    private final String DisplayName;
    private final String PhotoUrl;
    private final String FcmToken;

    public User(String UId, String email, String displayName, String photoUrl, String fcmToken) {
        this.UId = UId;
        this.Email = email;
        this.DisplayName = displayName;
        this.PhotoUrl = photoUrl;
        this.FcmToken = fcmToken;
    }

    public String getUId() {
        return UId;
    }

    public String getEmail() {
        return Email;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }


    public String getFcmToken() {
        return FcmToken;
    }


    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "UId='" + UId + '\'' +
                ", email='" + Email + '\'' +
                ", displayName='" + DisplayName + '\'' +
                ", photoUrl='" + PhotoUrl + '\'' +
                ", fcmToken='" + FcmToken + '\'' +
                '}';
    }
}
