package com.federicodg80.listly.ui.invitations;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.api.invitation.InvitationRespondResponse;
import com.federicodg80.listly.models.Invitation;
import com.federicodg80.listly.repository.InvitationRepository;
import com.federicodg80.listly.utils.PreferencesManager;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class InvitationsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Invitation>> mInvitations = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public InvitationsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Invitation>> getInvitations() {
        return mInvitations;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public LiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchInvitations() {
        isLoading.postValue(true);
        String token = PreferencesManager.getToken(getApplication());
        InvitationRepository repository = new InvitationRepository();

        repository.getInvitations(token, new InvitationRepository.GetInvitationsCallback() {
            @Override
            public void onSuccess(List<Invitation> response) {
                mInvitations.postValue(response);
                isLoading.postValue(false);
                if (response.isEmpty()) isEmpty.postValue(true);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                mError.postValue(error);
            }
        });
    }

    public void respondToInvitation(int listId, boolean accept) {
        String token = PreferencesManager.getToken(getApplication());
        InvitationRepository repository = new InvitationRepository();

        repository.invtationResponse(token, listId, accept, new InvitationRepository.ReponseInvitationCallback() {
            @Override
            public void onSuccess(InvitationRespondResponse response) {
                String message = accept ? "Invitacion aceptada" : "Invitacion rechazada";
                FancyToast.makeText(getApplication(),message, FancyToast.LENGTH_LONG,FancyToast.DEFAULT,true).show();
                fetchInvitations();
            }

            @Override
            public void onError(String error) {
                mError.postValue(error);
            }
        });
    }

}