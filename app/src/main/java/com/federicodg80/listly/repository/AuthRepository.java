package com.federicodg80.listly.repository;

import android.util.Log;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.auth.LoginRequest;
import com.federicodg80.listly.api.auth.LoginResponse;
import com.federicodg80.listly.api.auth.RebootRequest;
import com.federicodg80.listly.api.auth.RebootResponse;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService api;

    public AuthRepository() {
        api = ApiClient.getClient().create(ApiService.class);
    }

    public void login(String UID, String email, String displayName, String photoUrl, String providerId, String fcmToken, LoginCallback callback) {
        api.login(new LoginRequest(UID, email, displayName, photoUrl, providerId, fcmToken))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            String errorMsg = "Error desconocido";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                Log.e("AuthRepository", "Error al leer errorBody: " + e.getMessage());
                            }
                            callback.onError(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String error);
    }

   /* public void reboot(String token, String UID, String fcmToken) {
        Log.d("ALGO", token + " " + UID + " " + fcmToken);

        api.reboot(token, new RebootRequest(UID,  fcmToken))
                .enqueue(new Callback<RebootResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RebootResponse> call, @NonNull Response<RebootResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("ALGO", "onResponse OK: ");
                        }else {
                            Log.d("ALGO", "onResponse NOT OK: " + response.code() + " " + response.message());
                            Log.d("ALGO", "onResponse NOT OK: " + response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RebootResponse> call, @NonNull Throwable t) {
                    }
                });
    }*/
}