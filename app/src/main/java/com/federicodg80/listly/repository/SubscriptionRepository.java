package com.federicodg80.listly.repository;

import androidx.annotation.NonNull;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.list.TaskListRequest;
import com.federicodg80.listly.api.subscription.SubscriptionResponse;
import com.federicodg80.listly.models.TaskList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionRepository {

    private final ApiService api;

    public SubscriptionRepository() {
        this.api = ApiClient.getClient().create(ApiService.class);
    }

    public void createPayment(String token, SubscriptionRepository.CreatePayment callback) {

        api.createPayment("Bearer " + token)
                .enqueue(new Callback<SubscriptionResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubscriptionResponse> call, @NonNull Response<SubscriptionResponse> response) {
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
                    public void onFailure(@NonNull Call<SubscriptionResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public interface CreatePayment {
        void onSuccess(SubscriptionResponse response);
        void onError(String error);
    }

}
