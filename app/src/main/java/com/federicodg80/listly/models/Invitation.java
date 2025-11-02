package com.federicodg80.listly.models;

public class Invitation {
    private int InvitationId;
    private int ListId;
    private String FromUserId;
    private String ToUserId;
    private String Status;
    private TaskList List;
    private User FromUser;
    private User ToUser;

    public Invitation(int invitationId, int listId, String fromUserId, String toUserId, String status, TaskList list, User fromUser, User toUser) {
        InvitationId = invitationId;
        ListId = listId;
        FromUserId = fromUserId;
        ToUserId = toUserId;
        Status = status;
        List = list;
        FromUser = fromUser;
        ToUser = toUser;
    }

    public int getInvitationId() {
        return InvitationId;
    }

    public int getListId() {
        return ListId;
    }

    public String getFromUserId() {
        return FromUserId;
    }

    public String getToUserId() {
        return ToUserId;
    }

    public String getStatus() {
        return Status;
    }

    public TaskList getList() {
        return List;
    }

    public User getFromUser() {
        return FromUser;
    }

    public User getToUser() {
        return ToUser;
    }
}
