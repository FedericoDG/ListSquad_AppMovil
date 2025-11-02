package com.federicodg80.listly.repository;

import androidx.annotation.NonNull;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.user.UserDetailsResponse;
import com.federicodg80.listly.models.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService api;

    public UserRepository() {
        this.api = ApiClient.getClient().create(ApiService.class);
    }

    public void getMe(String token,  UserRepository.GetMeCallback callback) {
        api.getMe("Bearer " + token)
                .enqueue(new Callback<UserDetailsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDetailsResponse> call, @NonNull Response<UserDetailsResponse> response) {
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
                    public void onFailure(@NonNull Call<UserDetailsResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }



    public interface GetMeCallback {
        void onSuccess(UserDetailsResponse response);
        void onError(String error);
    }
}
