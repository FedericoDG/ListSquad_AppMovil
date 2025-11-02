package com.federicodg80.listly.repository;

import androidx.annotation.NonNull;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.settings.UpdateSettingsResponse;
import com.federicodg80.listly.models.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRepository {
    private final ApiService api;

    public SettingsRepository() {
        this.api = ApiClient.getClient().create(ApiService.class);
    }
    public void updateInvitationSetting(String token, boolean state, SettingsRepository.UpdateSettingsCallback callback) {
        api.updateInvitationNotificationsSetting("Bearer " + token, state )
                .enqueue(new Callback<UpdateSettingsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateSettingsResponse> call, @NonNull Response<UpdateSettingsResponse> response) {
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
                    public void onFailure(@NonNull Call<UpdateSettingsResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void updateItemAddedSetting(String token, boolean state, SettingsRepository.UpdateSettingsCallback callback) {
        api.updateItemAddedNotificationsSetting("Bearer " + token, state )
                .enqueue(new Callback<UpdateSettingsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateSettingsResponse> call, @NonNull Response<UpdateSettingsResponse> response) {
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
                    public void onFailure(@NonNull Call<UpdateSettingsResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void updateItemStatusChangedSetting(String token, boolean state, SettingsRepository.UpdateSettingsCallback callback) {
        api.updateItemStatusChangedNotificationsSetting("Bearer " + token, state )
                .enqueue(new Callback<UpdateSettingsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateSettingsResponse> call, @NonNull Response<UpdateSettingsResponse> response) {
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
                    public void onFailure(@NonNull Call<UpdateSettingsResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void updateItemDeletedSetting(String token, boolean state, SettingsRepository.UpdateSettingsCallback callback) {
        api.updateItemDeletedNotificationsSetting("Bearer " + token, state )
                .enqueue(new Callback<UpdateSettingsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateSettingsResponse> call, @NonNull Response<UpdateSettingsResponse> response) {
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
                    public void onFailure(@NonNull Call<UpdateSettingsResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }


    public interface UpdateSettingsCallback {
        void onSuccess(UpdateSettingsResponse response);
        void onError(String error);
    }
}
