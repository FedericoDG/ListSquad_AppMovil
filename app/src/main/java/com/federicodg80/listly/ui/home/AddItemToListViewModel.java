package com.federicodg80.listly.ui.home;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.federicodg80.listly.models.Item;
import com.federicodg80.listly.repository.ItemRepository;
import com.federicodg80.listly.repository.ListRepository;
import com.federicodg80.listly.utils.PreferencesManager;

public class AddItemToListViewModel extends AndroidViewModel {
    private MutableLiveData<String> mSuccessMessage = new MutableLiveData<>();
    private MutableLiveData<String> mErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<Item> mCurrentItem = new MutableLiveData<>();

    public AddItemToListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return mSuccessMessage;
    }

    public LiveData<Item> getCurrentItem() {
        return mCurrentItem;
    }

    public void addItemToList(Bundle bundle, String quantityStr, String unit, String title, String description, String notes) {
        int listId = bundle.getInt("listId");

        if (!isValidInput(quantityStr, unit, title)) return;

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            mErrorMessage.postValue("La cantidad debe ser un número válido");
            return;
        }

        String token = PreferencesManager.getToken(getApplication());

        ListRepository repository = new ListRepository();

        repository.addItem(listId, token, quantity, unit, title, description, notes, new ListRepository.AddItemToListCallback() {
            @Override
            public void onSuccess(Item response) {
                mSuccessMessage.postValue("Ítem agregado exitosamente");
            }

            @Override
            public void onError(String error) {
                mErrorMessage.postValue(error);
            }
        });
    }

    public void updateItem(Bundle bundle, String quantityStr, String unit, String title, String description, String notes) {
        if (!isValidInput(quantityStr, unit, title)) return;

        if (bundle == null) {
            return;
        }

        // Verificar si el bundle contiene un item
        if (!bundle.containsKey("itemId")) {
            return;
        }

        // Extraer el itemId del bundle
        int itemId =  bundle.getInt("itemId", -1);


        // Extraer el listId del bundle
        int listId = bundle.getInt("listId");

        if (!isValidInput(quantityStr, unit, title)) {
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            mErrorMessage.postValue("La cantidad debe ser un número válido");
            return;
        }

        String token = PreferencesManager.getToken(getApplication());

        Item updatedItem = new Item(itemId, listId, title, description, notes, quantity, unit);

        ItemRepository repository = new ItemRepository();

        repository.updateItem(token, itemId, updatedItem, new ItemRepository.ToggleCompletedCallback() {
            @Override
            public void onSuccess(Item response) {
                mSuccessMessage.postValue("Ítem actualizado exitosamente");
            }

            @Override
            public void onError(String error) {
                mErrorMessage.postValue(error);
            }
        });
    }

    public void getItem(Bundle bundle) {
        if (bundle == null) return;

        // Verificar si el bundle contiene un item
        if (!bundle.containsKey("itemId")) return;

        // Extraer el itemId del bundle
        int itemId =  bundle.getInt("itemId", -1);

        if (itemId != -1) {
            ItemRepository repository = new ItemRepository();
            String token = PreferencesManager.getToken(getApplication());

            repository.getItem(token, itemId, new ItemRepository.ToggleCompletedCallback() {
                @Override
                public void onSuccess(Item response) {
                    mCurrentItem.postValue(response);
                }

                @Override
                public void onError(String error) {
                    mErrorMessage.postValue(error);
                }
            });
        }
    }

    private boolean isValidInput(String quantityStr, String unit, String title) {
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            mErrorMessage.postValue("La cantidad no puede estar vacía");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityStr.trim());

            if (quantity <= 0) {
                mErrorMessage.postValue("La cantidad debe ser mayor a 0");
                return false;
            }
        } catch (NumberFormatException e) {
            mErrorMessage.postValue("La cantidad debe ser un número válido");
            return false;
        }

        if (unit == null || unit.trim().isEmpty()) {
            mErrorMessage.postValue("La unidad no puede estar vacía");
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            mErrorMessage.postValue("El título no puede estar vacío");
            return false;
        }

        return true;
    }
}