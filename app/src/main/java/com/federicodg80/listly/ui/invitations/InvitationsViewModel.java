package com.federicodg80.listly.ui.invitations;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.api.invitation.InvitationRespondResponse;
import com.federicodg80.listly.models.Invitation;
import com.federicodg80.listly.repository.InvitationRepository;
import com.federicodg80.listly.utils.PreferencesManager;

import java.util.List;

public class InvitationsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Invitation>> mInvitations = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();

    public InvitationsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Invitation>> getInvitations() {
        return mInvitations;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void fetchInvitations() {
        String token = PreferencesManager.getToken(getApplication());
        InvitationRepository repository = new InvitationRepository();

        repository.getInvitations(token, new InvitationRepository.GetInvitationsCallback() {
            @Override
            public void onSuccess(List<Invitation> response) {
                Log.d("INVITATIONS", "onSuccess: " + response.size());
                mInvitations.postValue(response);
            }

            @Override
            public void onError(String error) {
                mError.postValue(error);
            }
        });
    }

    public void respondToInvitation(int listId, boolean accept) {
        Log.d("LISTRESPONSE", "respondToInvitation: " + listId + " accept: " + accept);
        String token = PreferencesManager.getToken(getApplication());
        InvitationRepository repository = new InvitationRepository();

        repository.invtationResponse(token, listId, accept, new InvitationRepository.ReponseInvitationCallback() {
            @Override
            public void onSuccess(InvitationRespondResponse response) {
                String message = accept ? "Invitacion aceptada" : "Invitacion rechazada";
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                fetchInvitations();
            }

            @Override
            public void onError(String error) {
                mError.postValue(error);
            }
        });
    }

}