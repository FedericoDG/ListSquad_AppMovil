package com.federicodg80.listly.api.user;

import com.federicodg80.listly.models.Settings;
import com.federicodg80.listly.models.Subscription;
import com.federicodg80.listly.models.User;

public class UserDetailsResponse extends User {

    private Subscription Subscription;
    private Settings Settings;

    public UserDetailsResponse(
            String UId,
            String email,
            String displayName,
            String photoUrl,
            String fcmToken,
            Subscription subscription,
            Settings settings
    ) {
        super(UId, email, displayName, photoUrl, fcmToken);
        Subscription = subscription;
        Settings = settings;
    }

    public Subscription getSubscription() {
        return Subscription;
    }

    public Settings getSettings() {
        return Settings;
    }
}
