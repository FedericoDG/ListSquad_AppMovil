package com.federicodg80.listly.api.settings;

public class UpdateSettingsResponse {
    private boolean enabled;

    public UpdateSettingsResponse(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
