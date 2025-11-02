package com.federicodg80.listly;

import android.content.Intent;
import android.net.Uri;

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
}
