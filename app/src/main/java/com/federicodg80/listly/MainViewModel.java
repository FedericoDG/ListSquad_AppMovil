package com.federicodg80.listly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<String> paymentStatus = new MutableLiveData<>();

    public LiveData<String> getPaymentStatus() {
        return paymentStatus;
    }

    public void handlePaymentResult(Intent intent) {
        Uri data = intent.getData();

        if (data != null) {
            String path = data.getPath();

            if ("/exitoso".equals(path)) {
                paymentStatus.setValue("success");
            } else if ("/error".equals(path)) {
                paymentStatus.setValue("error");
            } else if ("/pendiente".equals(path)) {
                paymentStatus.setValue("pending");
            }
        }
    }

    public void handleNavigationIntent(@NonNull Intent intent, @NonNull androidx.navigation.NavController navController) {
        if (intent == null || navController == null) {
            return;
        }

        String type = intent.getStringExtra("type");

        if (type != null) {
            switch (type) {
                case "tasks":
                    navController.navigate(R.id.navigation_home);
                    break;
                case "invitations":
                    navController.navigate(R.id.navigation_invitations);
                    break;
                case "profile":
                    navController.navigate(R.id.navigation_profile);
                    break;
                case "toggle_item":
                case "updated_item":
                case "deleted_item":
                case "added_item":
                    String listId = intent.getStringExtra("listId");
                    Log.d("NAZGUL", "handleNavigationIntent: " + listId);
                    Bundle args = new Bundle();
                    args.putInt("listId", Integer.parseInt(listId));
                    navController.navigate(R.id.navigation_list_details, args);
                    break;
            }
        }
    }
}
