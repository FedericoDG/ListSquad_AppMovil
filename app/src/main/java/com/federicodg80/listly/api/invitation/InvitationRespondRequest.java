package com.federicodg80.listly.api.invitation;

public class InvitationRespondRequest {
    private boolean Accepted;
    public InvitationRespondRequest(boolean accepted) {
        Accepted = accepted;
    }
    public boolean isAccept() {
        return Accepted;
    }
}
