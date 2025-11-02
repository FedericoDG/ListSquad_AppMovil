package com.federicodg80.listly.ui.payment;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.federicodg80.listly.R;

public class PaymentResultViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mPaymentSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> mPaymentPending = new MutableLiveData<>();
    private MutableLiveData<Boolean> mPaymentError = new MutableLiveData<>();
    public PaymentResultViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getPaymentSuccess() {
        return mPaymentSuccess;
    }

    public LiveData<Boolean> getPaymentPending() {
        return mPaymentPending;
    }

    public LiveData<Boolean> getPaymentError() {
        return mPaymentError;
    }

    public void showMessage(Bundle args) {
        String status = args.getString("payment_status");

        if ("success".equals(status)) {
            mPaymentSuccess.setValue(true);
        } else if ("pending".equals(status)) {
            mPaymentPending.setValue(true);
        } else if ("error".equals(status)) {
            mPaymentError.setValue(true);
        }
    }

    public void returnToProfile(View view) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> Navigation.findNavController(view).navigate(R.id.navigation_profile), 7000);
    }
}