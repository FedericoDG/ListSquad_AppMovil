package com.federicodg80.listly.ui.payment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.federicodg80.listly.R;
import com.federicodg80.listly.databinding.FragmentPaymentResultBinding;

public class PaymentResultFragment extends Fragment {
    private FragmentPaymentResultBinding binding;

    private PaymentResultViewModel viewModel;

    public static PaymentResultFragment newInstance() {
        return new PaymentResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PaymentResultViewModel.class);
        binding = FragmentPaymentResultBinding.inflate(inflater, container, false);

        // Observers
        viewModel.getPaymentSuccess().observe(getViewLifecycleOwner(), success -> {
            binding.layoutSuccess.setVisibility(View.VISIBLE);
            binding.layoutPending.setVisibility(View.GONE);
            binding.layoutError.setVisibility(View.GONE);
        });

        viewModel.getPaymentPending().observe(getViewLifecycleOwner(), success -> {
            binding.layoutSuccess.setVisibility(View.GONE);
            binding.layoutPending.setVisibility(View.VISIBLE);
            binding.layoutError.setVisibility(View.GONE);
        });

        viewModel.getPaymentError().observe(getViewLifecycleOwner(), success -> {
            binding.layoutSuccess.setVisibility(View.GONE);
            binding.layoutPending.setVisibility(View.GONE);
            binding.layoutError.setVisibility(View.VISIBLE);
        });

        viewModel.showMessage(getArguments());

        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.returnToProfile(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}