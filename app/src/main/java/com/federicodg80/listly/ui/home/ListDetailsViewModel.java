package com.federicodg80.listly.ui.home;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.api.list.TaskListDetails;
import com.federicodg80.listly.api.list.TaskListMessage;
import com.federicodg80.listly.api.user.UserDetailsResponse;
import com.federicodg80.listly.models.Invitation;
import com.federicodg80.listly.models.Item;
import com.federicodg80.listly.models.TaskList;
import com.federicodg80.listly.models.User;
import com.federicodg80.listly.repository.InvitationRepository;
import com.federicodg80.listly.repository.ItemRepository;
import com.federicodg80.listly.repository.ListRepository;
import com.federicodg80.listly.repository.UserRepository;
import com.federicodg80.listly.utils.PreferencesManager;

import java.util.List;
import java.util.ArrayList;

public class ListDetailsViewModel extends AndroidViewModel {
    private final MutableLiveData<TaskList> mList = new MutableLiveData<>();
    private final MutableLiveData<User> mOwner = new MutableLiveData<>();
    private final MutableLiveData<List<User>> mCollaborators = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> mItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<UserDetailsResponse> mUserDetails = new MutableLiveData<>();
    private final MutableLiveData<Boolean> enableAddCollaboratorBtn = new MutableLiveData<>(false);
    private boolean hasFreeSubscription = true;

    public ListDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<TaskList> getList() {
        return mList;
    }
    public LiveData<User> getOwner() {
        return mOwner;
    }
    public LiveData<List<User>> getCollaborators() {
        return mCollaborators;
    }
    public LiveData<List<Item>> getItems() {
        return mItems;
    }

    public LiveData<Boolean> getEnableAddCollaboratorBtn() {
        return enableAddCollaboratorBtn;
    }

    public void loadListDetails(Bundle bundle) {
        int listId = bundle.getInt("listId", -1);
        String token = PreferencesManager.getToken(getApplication());

        // Primero obtener los detalles del usuario
        UserRepository userRepository = new UserRepository();
        Log.d("ListDetailsVM", "Calling UserRepository.getMe");
        userRepository.getMe(token, new UserRepository.GetMeCallback() {
            @Override
            public void onSuccess(UserDetailsResponse response) {
                // Actualizar LiveData de usuario
                mUserDetails.postValue(response);

                hasFreeSubscription = response.getSubscription() != null && "Free".equals(response.getSubscription().getName());

                // Una vez obtenido el usuario, cargar los detalles de la lista
                ListRepository listRepository = new ListRepository();
                listRepository.getList(token, listId, new ListRepository.GetListCallback() {
                    @Override
                    public void onSuccess(TaskListDetails response) {
                        Log.d("ListDetailsVM", "getList onSuccess: itemsCount=" + (response != null && response.getItems() != null ? response.getItems().size() : 0));
                        mList.postValue(new TaskList(response.getListId(), response.getTitle(), response.getDescription(), response.getTitle(), response.getOwnerUid()));
                        if (response.getOwner() != null) {
                            mOwner.postValue(new User(
                                    response.getOwner().getUId(),
                                    response.getOwner().getEmail(),
                                    response.getOwner().getDisplayName(),
                                    response.getOwner().getPhotoUrl(),
                                    response.getOwner().getProviderId(),
                                    response.getOwner().getFcmToken())
                            );
                        }

                        if(response.getCollaborators() != null && !response.getCollaborators().isEmpty()){
                            mCollaborators.postValue(response.getCollaborators());
                        }

                        mItems.postValue(response.getItems());

                        if (PreferencesManager.getUserUId(getApplication()).equals(response.getOwnerUid()) && !hasFreeSubscription) {
                            enableAddCollaboratorBtn.postValue(true);
                        }else {
                            enableAddCollaboratorBtn.postValue(false);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplication(), "Error al obtener la lista: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al obtener datos del usuario: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("listid", "loadListDetails: " + listId);

    }

    public void toggleItemCompleted(int itemId) {
        String token = PreferencesManager.getToken(getApplication());

        ItemRepository itemRepository = new ItemRepository();
        itemRepository.toggleCompleted(token, itemId, new ItemRepository.ToggleCompletedCallback() {
            @Override
            public void onSuccess(Item response) {
                // Update the item in the LiveData list
                List<Item> currentItems = mItems.getValue();
                if (currentItems != null) {
                    for (int i = 0; i < currentItems.size(); i++) {
                        if (currentItems.get(i).getItemId() == response.getItemId()) {
                            currentItems.set(i, response);
                            break;
                        }
                    }
                    mItems.postValue(currentItems);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error cambiar el estado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteItem(int itemId) {
        String token = PreferencesManager.getToken(getApplication());

        ItemRepository itemRepository = new ItemRepository();
        itemRepository.deleteItem(token, itemId, new ItemRepository.ToggleCompletedCallback() {
            @Override
            public void onSuccess(Item response) {
                Toast.makeText(getApplication(), "Item Eliminado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al eliminar item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeCollaborator(Bundle bundle, String collaboratorId) {
        int listId =  bundle.getInt("listId", -1);
        String token = PreferencesManager.getToken(getApplication());

        ListRepository listRepository = new ListRepository();
        listRepository.deleteCollaboratorFromList(token, listId, collaboratorId, new ListRepository.RemoveCollaboratorCallback() {

            @Override
            public void onSuccess(TaskListMessage response) {
                Toast.makeText(getApplication(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al eliminar colaborador", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void inviteCollaborator(Bundle bundle, String collaboratorEmail) {
        if (!isAValidEmail(collaboratorEmail)) {
            Toast.makeText(getApplication(), "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int listId =  bundle.getInt("listId", -1);
        String token = PreferencesManager.getToken(getApplication());

        InvitationRepository repository = new InvitationRepository();
        repository.sendInvitation(token, listId, collaboratorEmail, new InvitationRepository.SendInvitationCallback() {
            @Override
            public void onSuccess(Invitation response) {
                Toast.makeText(getApplication(), "Invitación enviada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isAValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
    }
}