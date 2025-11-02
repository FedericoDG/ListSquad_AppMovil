package com.federicodg80.listly.api.invitation;

public class InvitationSendRequest {
    private int ListId;
    private String ToUserEmail;

    public InvitationSendRequest(int listId, String toUserEmail) {
        ListId = listId;
        ToUserEmail = toUserEmail;
    }

    public int getListId() {
        return ListId;
    }

    public String getToUserEmail() {
        return ToUserEmail;
    }
}
