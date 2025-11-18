package com.federicodg80.listly.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.api.list.TaskListMessage;
import com.federicodg80.listly.models.TaskList;
import com.federicodg80.listly.repository.ListRepository;
import com.federicodg80.listly.utils.PreferencesManager;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MutableLiveData<List<TaskList>> mLists = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TaskList>> getLists() {
        return mLists;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    public void loadLists(){
        isLoading.postValue(true);
        String token = PreferencesManager.getToken(getApplication());

        ListRepository repository = new ListRepository();
        repository.getLists(token, new ListRepository.GetListsCallback() {
            @Override
            public void onSuccess(List<TaskList> response) {
                mLists.postValue(response);
                isLoading.postValue(false);
                isEmpty.postValue(response.isEmpty());
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
            }
        });
    }

    public void deleteList(int listId) {
        String token = PreferencesManager.getToken(getApplication());

        ListRepository listRepository = new ListRepository();
        listRepository.deleteList(token, listId, new ListRepository.RemoveCollaboratorCallback() {
            @Override
            public void onSuccess(TaskListMessage response) {
                // Recargar las listas después de eliminar una
                loadLists();
                FancyToast.makeText(getApplication(),"Lista eliminada",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
            }

            @Override
            public void onError(String error) {
                String userMessage = "Error desconocido al eliminar la lista";

                // Errores específicos
                if (error != null) {
                    if (error.contains("Solo el propietario de la lista puede eliminarla") ||
                        error.contains("propietario")) {
                        userMessage = "Solo el propietario de la lista puede eliminarla";
                    } else if (error.contains("403")) {
                        userMessage = "No tienes permisos para eliminar esta lista";
                    } else if (error.contains("404")) {
                        userMessage = "La lista no fue encontrada";
                    } else if (error.contains("401")) {
                        userMessage = "Tu sesión ha expirado. Inicia sesión nuevamente";
                    } else {
                        // Mostrar una versión simplificada del error
                        userMessage = "Error: " + error.substring(0, Math.min(100, error.length()));
                        if (error.length() > 100) {
                            userMessage += "...";
                        }
                    }
                }

                FancyToast.makeText(getApplication(), userMessage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }
}

