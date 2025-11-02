package com.federicodg80.listly.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.models.TaskList;
import com.federicodg80.listly.repository.ListRepository;
import com.federicodg80.listly.utils.PreferencesManager;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MutableLiveData<List<TaskList>> mLists = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
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
}

