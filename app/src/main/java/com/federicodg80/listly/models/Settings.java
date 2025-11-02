package com.federicodg80.listly.models;

public class Settings {
    private int SettingId;
    private boolean ReceiveInvitationNotifications;
    private boolean ReceiveItemAddedNotifications;
    private boolean ReceiveItemStatusChangedNotifications;
    private boolean ReceiveItemDeletedNotifications;

    public Settings(int settingId, boolean receiveInvitationNotifications, boolean receiveItemAddedNotifications, boolean receiveItemStatusChangedNotifications, boolean receiveItemDeletedNotifications) {
        SettingId = settingId;
        ReceiveInvitationNotifications = receiveInvitationNotifications;
        ReceiveItemAddedNotifications = receiveItemAddedNotifications;
        ReceiveItemStatusChangedNotifications = receiveItemStatusChangedNotifications;
        ReceiveItemDeletedNotifications = receiveItemDeletedNotifications;
    }

    public int getSettingId() {
        return SettingId;
    }

    public boolean isReceiveInvitationNotifications() {
        return ReceiveInvitationNotifications;
    }

    public boolean isReceiveItemAddedNotifications() {
        return ReceiveItemAddedNotifications;
    }

    public boolean isReceiveItemStatusChangedNotifications() {
        return ReceiveItemStatusChangedNotifications;
    }

    public boolean isReceiveItemDeletedNotifications() {
        return ReceiveItemDeletedNotifications;
    }
}
