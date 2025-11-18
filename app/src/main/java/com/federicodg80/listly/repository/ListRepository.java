package com.federicodg80.listly.repository;

import androidx.annotation.NonNull;

import com.federicodg80.listly.api.ApiClient;
import com.federicodg80.listly.api.ApiService;
import com.federicodg80.listly.api.list.TaskListDetails;
import com.federicodg80.listly.api.list.TaskListMessage;
import com.federicodg80.listly.api.list.TaskListMessage;
import com.federicodg80.listly.api.list.TaskListRequest;
import com.federicodg80.listly.models.Item;
import com.federicodg80.listly.models.TaskList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRepository {
    private final ApiService api;

    public ListRepository() {
        this.api = ApiClient.getClient().create(ApiService.class);
    }

    public void getLists(String token, ListRepository.GetListsCallback callback) {

        api.getLists("Bearer " + token)
                .enqueue(new Callback<List<TaskList>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<TaskList>> call, @NonNull Response<List<TaskList>> response) {
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
                    public void onFailure(@NonNull Call<List<TaskList>> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void createList(String token, String title, String description, String type, ListRepository.CreateListCallback callback) {
        TaskListRequest request = new TaskListRequest(title, description, type);

        api.createList("Bearer " + token, request)
                .enqueue(new Callback<TaskList>() {
                    @Override
                    public void onResponse(@NonNull Call<TaskList> call, @NonNull Response<TaskList> response) {
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
                    public void onFailure(@NonNull Call<TaskList> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void getList(String token, int listId, ListRepository.GetListCallback callback) {
        api.getList("Bearer " + token, listId)
                .enqueue(new Callback<TaskListDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<TaskListDetails> call, @NonNull Response<TaskListDetails> response) {
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
                    public void onFailure(@NonNull Call<TaskListDetails> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void deleteCollaboratorFromList(String token, int listId, String collaboratorId, ListRepository.RemoveCollaboratorCallback callback) {
        api.deleteCollaboratorFromList("Bearer " + token, listId, collaboratorId)
                .enqueue(new Callback<TaskListMessage>() {
                    @Override
                    public void onResponse(@NonNull Call<TaskListMessage> call, @NonNull Response<TaskListMessage> response) {
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
                    public void onFailure(@NonNull Call<TaskListMessage> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void deleteList(String token, int listId, ListRepository.RemoveCollaboratorCallback callback) {
        api.deleteList("Bearer " + token, listId)
                .enqueue(new Callback<TaskListMessage>() {
                    @Override
                    public void onResponse(@NonNull Call<TaskListMessage> call, @NonNull Response<TaskListMessage> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());
                        } else {
                            String errorMsg = "Error desconocido - Código: " + response.code();
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
                    public void onFailure(@NonNull Call<TaskListMessage> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void addItem(int listId, String token, int quantity, String unit, String title, String description, String notes, ListRepository.AddItemToListCallback callback) {
        Item request = new Item(listId, title, description, notes, quantity, unit);

        api.addItemToList("Bearer " + token, listId, request)
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

    public interface GetListsCallback {
        void onSuccess(List<TaskList> response);
        void onError(String error);
    }

    public interface CreateListCallback {
        void onSuccess(TaskList response);
        void onError(String error);
    }

    public interface GetListCallback {
        void onSuccess(TaskListDetails response);
        void onError(String error);
    }

    public interface RemoveCollaboratorCallback {
        void onSuccess(TaskListMessage response);
        void onError(String error);
    }

    public interface AddItemToListCallback {
        void onSuccess(Item response);
        void onError(String error);
    }
}
