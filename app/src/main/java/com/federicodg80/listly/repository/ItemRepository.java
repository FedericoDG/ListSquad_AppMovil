package com.federicodg80.listly.repository;

import androidx.annotation.NonNull;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.list.TaskListDetails;
import com.federicodg80.listly.api.list.TaskListRequest;
import com.federicodg80.listly.models.Item;
import com.federicodg80.listly.models.TaskList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepository {
    private final ApiService api;

    public ItemRepository() {
        this.api = ApiClient.getClient().create(ApiService.class);
    }

    public void toggleCompleted(String token, int itemId, ItemRepository.ToggleCompletedCallback callback) {
        api.toggleCompleted("Bearer " + token, itemId )
                .enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
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
                    public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void deleteItem(String token, int itemId, ItemRepository.ToggleCompletedCallback callback) {
        api.deleteItem("Bearer " + token, itemId )
                .enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
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
                    public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void updateItem(String token, int itemId, Item item,  ItemRepository.ToggleCompletedCallback callback) {
        api.updateItem("Bearer " + token, itemId, item )
                .enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
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
                    public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void getItem(String token, int itemId, ItemRepository.ToggleCompletedCallback callback) {
        api.getItem("Bearer " + token, itemId )
                .enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
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
                    public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }



    public interface ToggleCompletedCallback {
        void onSuccess(Item response);
        void onError(String error);
    }

}
