package com.federicodg80.listly.api.invitation;

public class InvitationRespondResponse {
    private boolean accepted;

    public InvitationRespondResponse(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
