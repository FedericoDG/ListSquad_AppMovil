package com.federicodg80.listly.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.federicodg80.listly.MainActivity;
import com.federicodg80.listly.api.auth.LoginResponse;
import com.federicodg80.listly.repository.AuthRepository;
import com.federicodg80.listly.services.KeepAliveService;
import com.federicodg80.listly.utils.PreferencesManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginFragmentViewModel extends AndroidViewModel {
    private static final String TAG = "LoginFragmentViewModel";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final MutableLiveData<Boolean> mIscheckExistingUser = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> mIsLoging = new MutableLiveData<>(false);
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LoginFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getError() {
        return mError;
    }

    public LiveData<Boolean> getIscheckExistingUser() {
        return mIscheckExistingUser;
    };

    public LiveData<Boolean> getIsLoging() {
        return mIsLoging;
    }

    public void checkExistingUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            sendUserDataAndNavigate(currentUser);
        } else {
            mIscheckExistingUser.setValue(false);
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mIsLoging.setValue(true);

        if (acct == null) {
            mError.postValue("Cuenta de Google nula");
            mIsLoging.postValue(false);
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            sendUserDataAndNavigate(user);
                        } else {
                            mError.postValue("Usuario Firebase es nulo tras autenticación");
                        }
                    } else {
                        Exception e = task.getException();
                        String msg = e != null ? e.getMessage() : "Fallo autenticación con Firebase";
                        mError.postValue(msg);
                    }
                })
                .addOnFailureListener(e -> {
                    mError.postValue("Error: " + e.getMessage());
                    mIsLoging.postValue(false);
                });
    }

    private void sendUserDataAndNavigate(@NonNull FirebaseUser user) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(tokenTask -> {
                    String uid = user.getUid();
                    String email = user.getEmail();
                    String displayName = user.getDisplayName() != null ? user.getDisplayName() : "";
                    String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
                    String providerId = user.getProviderId();
                    String fcmToken = tokenTask.isSuccessful() ? tokenTask.getResult() : null;

                    AuthRepository authRepository = new AuthRepository();
                    authRepository.login(uid, email, displayName, photoUrl, providerId, fcmToken, new AuthRepository.LoginCallback() {
                        @Override
                        public void onSuccess(LoginResponse response) {
                            PreferencesManager.setLoggedIn(getApplication(), true);
                            PreferencesManager.saveUserUId(getApplication(), user.getUid());
                            PreferencesManager.saveToken(getApplication(), response.getToken());
                            mIsLoging.postValue(false);
                            startKeepAliveService();
                            navigateToMain();
                        }

                        @Override
                        public void onError(String error) {
                            mError.postValue("Error en login al backend: " + error);
                            mIsLoging.postValue(false);
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    mError.postValue("No se pudo obtener el token FCM: " + e.getMessage());
                    Log.e(TAG, "No se pudo obtener el token FCM: " + e.getMessage());
                });
    }

    private void startKeepAliveService() {
        Application app = getApplication();
        Intent serviceIntent = new Intent(app, KeepAliveService.class);
        app.startForegroundService(serviceIntent);
        Log.d(TAG, "KeepAliveService iniciado tras login");
    }

    private void navigateToMain(){
        Application app = getApplication();
        Intent intent = new Intent(app, MainActivity.class);
        // Limpiar pila para que el usuario no pueda volver al login con back
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Ejecutar en hilo principal para mayor robustez
        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
            try {
                app.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "No se pudo iniciar MainActivity desde ViewModel: " + e.getMessage(), e);
                mError.postValue("No se pudo abrir la app: " + e.getMessage());
            }
        });
    }
}
