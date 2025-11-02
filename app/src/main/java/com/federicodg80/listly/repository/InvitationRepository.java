package com.federicodg80.listly.repository;

import androidx.annotation.NonNull;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.invitation.InvitationRespondRequest;
import com.federicodg80.listly.api.invitation.InvitationRespondResponse;
import com.federicodg80.listly.api.invitation.InvitationSendRequest;
import com.federicodg80.listly.models.Invitation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationRepository {
    private final ApiService api;

    public InvitationRepository() {
        this.api = ApiClient.getClient().create(ApiService.class);
    }

    public void getInvitations(String token, InvitationRepository.GetInvitationsCallback callback) {
        api.getPendingInvitations("Bearer " + token )
                .enqueue(new Callback<List<Invitation>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Invitation>> call, @NonNull Response<List<Invitation>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            String errorMsg = "Error desconocido";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            callback.onError(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Invitation>> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void invtationResponse(String token, int listId, boolean response, InvitationRepository.ReponseInvitationCallback callback) {
        InvitationRespondRequest request = new InvitationRespondRequest(response);
        api.respondToInvitation("Bearer " + token, listId, request )
                .enqueue(new Callback<InvitationRespondResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<InvitationRespondResponse> call, @NonNull Response<InvitationRespondResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            String errorMsg = "Error desconocido";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            callback.onError(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<InvitationRespondResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void sendInvitation(String token, int listId, String toUserEmail, InvitationRepository.SendInvitationCallback callback) {
        InvitationSendRequest request = new InvitationSendRequest(listId, toUserEmail);
        api.sendInvitation("Bearer " + token, request )
                .enqueue(new Callback<Invitation>() {
                    @Override
                    public void onResponse(@NonNull Call<Invitation> call, @NonNull Response<Invitation> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            String errorMsg = "Error desconocido";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            callback.onError(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Invitation> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }


    public interface GetInvitationsCallback {
        void onSuccess(List<Invitation> response);
        void onError(String error);
    }

    public interface ReponseInvitationCallback {
        void onSuccess(InvitationRespondResponse response);
        void onError(String error);
    }

    public interface SendInvitationCallback {
        void onSuccess(Invitation response);
        void onError(String error);
    }
}
