package com.federicodg80.listly.ui.profile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.LoginActivity;
import com.federicodg80.listly.api.settings.UpdateSettingsResponse;
import com.federicodg80.listly.api.subscription.SubscriptionResponse;
import com.federicodg80.listly.api.user.UserDetailsResponse;
import com.federicodg80.listly.models.TaskList;
import com.federicodg80.listly.repository.AuthRepository;
import com.federicodg80.listly.repository.ListRepository;
import com.federicodg80.listly.repository.SettingsRepository;
import com.federicodg80.listly.repository.SubscriptionRepository;
import com.federicodg80.listly.repository.UserRepository;
import com.federicodg80.listly.utils.PreferencesManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileViewModel extends AndroidViewModel {
    private static final String TAG = "ProfileFragmentVM";
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<UserDetailsResponse> mUserDetails = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHasFreeSubscription = new MutableLiveData<>();
    private MutableLiveData<SubscriptionResponse> mPaymentResponse = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<UserDetailsResponse> getUserDetails() {
        return mUserDetails;
    }

    public LiveData<Boolean> getHasFreeSubscription() {
        return mHasFreeSubscription;
    }

    public LiveData<SubscriptionResponse> getPaymentResponse() {
        return mPaymentResponse;
    }

    public void getMe(){
        isLoading.postValue(true);
        String token = PreferencesManager.getToken(getApplication());

        UserRepository repository = new UserRepository();
        repository.getMe(token, new UserRepository.GetMeCallback() {
            @Override
            public void onSuccess(UserDetailsResponse response) {
                isLoading.postValue(false);
                mUserDetails.postValue(response);

                if (response.getSubscription().getName().equals("Free")) {
                    mHasFreeSubscription.postValue(true);
                }else {
                    mHasFreeSubscription.postValue(false);
                }
            }

            @Override
            public void onError(String error) {
                // Manejar el error
                isLoading.postValue(false);
                Log.e(TAG, "Error al obtener las listas: " + error);
            }
        });
    }

    public void createPayment(){
        String token = PreferencesManager.getToken(getApplication());
        Log.d(TAG, "createPayment: " + token);

        SubscriptionRepository repository = new SubscriptionRepository();
        repository.createPayment(token, new SubscriptionRepository.CreatePayment() {
            @Override
            public void onSuccess(SubscriptionResponse response) {
                mPaymentResponse.postValue(response);
            }

            @Override
            public void onError(String error) {
                // Manejar el error
                Log.e(TAG, "Error al crear el pago: " + error);
            }
        });

    }

    public void openCheckout(Context context, String initPointUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(initPointUrl));
        context.startActivity(intent);
    }
    
    public void updateInvitationSetting(boolean state) {
        String token = PreferencesManager.getToken(getApplication());

        SettingsRepository repository = new SettingsRepository();
        repository.updateInvitationSetting(token, state, new SettingsRepository.UpdateSettingsCallback() {
            @Override
            public void onSuccess(UpdateSettingsResponse response) {
                Toast.makeText(getApplication(), "Configuración Actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al actualizar la configuración: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateItemAddedSetting(boolean state) {
        String token = PreferencesManager.getToken(getApplication());

        SettingsRepository repository = new SettingsRepository();
        repository.updateItemAddedSetting(token, state, new SettingsRepository.UpdateSettingsCallback() {
            @Override
            public void onSuccess(UpdateSettingsResponse response) {
                Toast.makeText(getApplication(), "Configuración Actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al actualizar la configuración: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateItemStatusChangedSetting(boolean state) {
        String token = PreferencesManager.getToken(getApplication());

        SettingsRepository repository = new SettingsRepository();
        repository.updateItemStatusChangedSetting(token, state, new SettingsRepository.UpdateSettingsCallback() {
            @Override
            public void onSuccess(UpdateSettingsResponse response) {
                Toast.makeText(getApplication(), "Configuración Actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al actualizar la configuración: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateItemDeletedSetting(boolean state) {
        String token = PreferencesManager.getToken(getApplication());

        SettingsRepository repository = new SettingsRepository();
        repository.updateItemDeletedSetting(token, state, new SettingsRepository.UpdateSettingsCallback() {
            @Override
            public void onSuccess(UpdateSettingsResponse response) {
                Toast.makeText(getApplication(), "Configuración Actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), "Error al actualizar la configuración: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void signOut(GoogleSignInClient googleSignInClient) {
        firebaseAuth.signOut();

        googleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    // Detener el servicio KeepAlive
                    Intent serviceIntent = new Intent(getApplication(), com.federicodg80.listly.services.KeepAliveService.class);
                    getApplication().stopService(serviceIntent);

                    // Eliminar el token FCM local y (opcionalmente) del backend
                    FirebaseMessaging.getInstance().deleteToken();

                    // Limpiar preferencias de sesión
                    AuthRepository authRepository = new AuthRepository();
                    String userId = PreferencesManager.getUserUId(getApplication());
                    String token = PreferencesManager.getToken(getApplication());
                    // authRepository.reboot("Bearer " + token, userId, " ");
                    // PreferencesManager.clearAll(getApplication());

                    navigateToLogin();
                });
    }
    private void navigateToLogin(){
        Application app = getApplication();
        Intent intent = new Intent(app, LoginActivity.class);
        // Limpiar pila para que el usuario no pueda volver al login con back
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Ejecutar en hilo principal para mayor robustez
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            try {
                app.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "No se pudo iniciar LoginActivity desde ViewModel: " + e.getMessage(), e);
            }
        });
    }

}