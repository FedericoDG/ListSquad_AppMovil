package com.federicodg80.listly.ui.home;

import android.app.Application;
import android.os.Bundle;

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
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;
import java.util.ArrayList;

public class ListDetailsViewModel extends AndroidViewModel {
    private final MutableLiveData<TaskList> mList = new MutableLiveData<>();
    private final MutableLiveData<User> mOwner = new MutableLiveData<>();
    private final MutableLiveData<List<User>> mCollaborators = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> mItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<UserDetailsResponse> mUserDetails = new MutableLiveData<>();
    private final MutableLiveData<Boolean> enableAddCollaboratorBtn = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
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
    public LiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadListDetails(Bundle bundle) {
        int listId = bundle.getInt("listId", -1);
        String token = PreferencesManager.getToken(getApplication());

        // Primero obtener los detalles del usuario
        UserRepository userRepository = new UserRepository();
        isLoading.postValue(true);
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
                        mList.postValue(new TaskList(response.getListId(), response.getTitle(), response.getDescription(), response.getTitle(), response.getOwnerUid()));
                        if (response.getOwner() != null) {
                            mOwner.postValue(new User(
                                    response.getOwner().getUId(),
                                    response.getOwner().getEmail(),
                                    response.getOwner().getDisplayName(),
                                    response.getOwner().getPhotoUrl(),
                                    response.getOwner().getFcmToken())
                            );
                        }

                        isLoading.postValue(false);

                        if(response.getCollaborators() != null && !response.getCollaborators().isEmpty()){
                            mCollaborators.postValue(response.getCollaborators());
                        }

                        isEmpty.postValue(response.getItems() == null || response.getItems().isEmpty());

                        mItems.postValue(response.getItems());

                        if (PreferencesManager.getUserUId(getApplication()).equals(response.getOwnerUid()) && !hasFreeSubscription) {
                            enableAddCollaboratorBtn.postValue(true);
                        }else {
                            enableAddCollaboratorBtn.postValue(false);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        isLoading.postValue(false);
                        FancyToast.makeText(getApplication(),"Error al obtener la lista: " + error,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                FancyToast.makeText(getApplication(),"Error al obtener datos del usuario: " + error,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            }
        });

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
                FancyToast.makeText(getApplication(),"Error al cambiar el estado",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            }
        });
    }

    public void deleteItem(int itemId) {
        String token = PreferencesManager.getToken(getApplication());

        ItemRepository itemRepository = new ItemRepository();
        itemRepository.deleteItem(token, itemId, new ItemRepository.ToggleCompletedCallback() {
            @Override
            public void onSuccess(Item response) {
                FancyToast.makeText(getApplication(),"Item eliminado",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
            }

            @Override
            public void onError(String error) {
                FancyToast.makeText(getApplication(),"Error al eliminar el ítem",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
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
                FancyToast.makeText(getApplication(),response.getMessage(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
            }
            @Override
            public void onError(String error) {
                FancyToast.makeText(getApplication(),"Error al eliminar el colaborador",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            }
        });
    }

    public void inviteCollaborator(Bundle bundle, String collaboratorEmail) {
        if (!isAValidEmail(collaboratorEmail)) {
            FancyToast.makeText(getApplication(),"Correo electrónico no válido",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            return;
        }

        int listId =  bundle.getInt("listId", -1);
        String token = PreferencesManager.getToken(getApplication());

        InvitationRepository repository = new InvitationRepository();
        repository.sendInvitation(token, listId, collaboratorEmail, new InvitationRepository.SendInvitationCallback() {
            @Override
            public void onSuccess(Invitation response) {
                FancyToast.makeText(getApplication(),"Invitación enviada",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
            }

            @Override
            public void onError(String error) {
                FancyToast.makeText(getApplication(),"Error: " + error,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
            }
        });
    }

    private boolean isAValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
    }
}