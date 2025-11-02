package com.federicodg80.listly.ui.home;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.MainActivity;
import com.federicodg80.listly.R;
import com.federicodg80.listly.repository.ListRepository;
import com.federicodg80.listly.utils.PreferencesManager;

public class CreateNewListViewModel extends AndroidViewModel {
    private MutableLiveData<String> mErrorMessage = new MutableLiveData<>("");
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    public CreateNewListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public void createNewList(String title, String description, String type) {
        if (!isValidInput(title, description, type)) {
            return;
        }

        mIsLoading.setValue(true);

        ListRepository repository = new ListRepository();
        String token = PreferencesManager.getToken(getApplication());

        repository.createList(token, title, description, type, new ListRepository.CreateListCallback() {
            @Override
            public void onSuccess(com.federicodg80.listly.models.TaskList taskList) {
                mIsLoading.postValue(false);
                mIsLoading.setValue(true);
                // Navegar al fragment HomeFragment
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().startActivity(intent);
            }

            @Override
            public void onError(String errorMessage) {
                mIsLoading.postValue(false);
                mErrorMessage.postValue(errorMessage);
                mIsLoading.setValue(true);
            }
        });

    }

    private boolean isValidInput(String title, String description, String type) {
        if (title == null || title.trim().isEmpty()) {
            mErrorMessage.setValue("El título no puede estar vacío.");
            return false;
        }
        if (title.length() > 30) {
            mErrorMessage.setValue("El título no puede exceder 20 caracteres.");
            return false;
        }
       /* if (description == null || description.trim().isEmpty()) {
            mErrorMessage.setValue("La descripción no puede estar vacía.");
            return false;
        }*/
        if (description.length() > 45) {
            mErrorMessage.setValue("La descripción no puede exceder 30 caracteres.");
            return false;
        }
        if (type == null || type.trim().isEmpty()) {
            mErrorMessage.setValue("El tipo no puede estar vacío.");
            return false;
        }
        return true;
    }
}
