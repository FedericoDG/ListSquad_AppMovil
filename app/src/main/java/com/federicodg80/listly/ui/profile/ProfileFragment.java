package com.federicodg80.listly.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.federicodg80.listly.R;
import com.federicodg80.listly.databinding.FragmentProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private GoogleSignInClient googleSignInClient;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Inicializamos GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Observers
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.linearLayout.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.linearLayout.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getUserDetails().observe(getViewLifecycleOwner(), userDetails -> {
            Glide.with(requireContext())
                    .load(userDetails.getPhotoUrl())
                    .placeholder(R.drawable.lsq_logo)
                    .error(R.drawable.lsq_logo)
                    .into(binding.userAvatar);
            binding.userDisplayName.setText(userDetails.getDisplayName());
            binding.userEmail.setText(userDetails.getEmail());
            binding.tvPlanName.setText(userDetails.getSubscription().getName());
            binding.tvPlanDescription.setText(userDetails.getSubscription().getDescription());
            binding.switchInvitaciones.setChecked(userDetails.getSettings().isReceiveInvitationNotifications());
            binding.switchItemAgregado.setChecked(userDetails.getSettings().isReceiveItemAddedNotifications());
            binding.switchItemActualizado.setChecked(userDetails.getSettings().isReceiveItemStatusChangedNotifications());
            binding.switchItemEliminado.setChecked(userDetails.getSettings().isReceiveItemDeletedNotifications());
        });

        viewModel.getHasFreeSubscription().observe(getViewLifecycleOwner(), hasFreeSubscription -> {
            if (hasFreeSubscription) {
                binding.buttonBuyPremium.setVisibility(View.VISIBLE);
            } else {
                binding.buttonBuyPremium.setVisibility(View.GONE);
            }
        });

        viewModel.getPaymentResponse().observe(getViewLifecycleOwner(), paymentResponse -> {
            if ( paymentResponse != null ) {
            viewModel.openCheckout(requireContext(), paymentResponse.getSandboxInitPoint());
            }
        });

        // Events
        binding.buttonLogout.setOnClickListener(v -> viewModel.signOut(googleSignInClient));

        binding.buttonBuyPremium.setOnClickListener(v -> viewModel.createPayment());

        binding.switchInvitaciones.setOnClickListener(v -> {
            boolean isChecked = binding.switchInvitaciones.isChecked();
            viewModel.updateInvitationSetting(isChecked);
        });

        binding.switchItemAgregado.setOnClickListener(v -> {
            boolean isChecked = binding.switchItemAgregado.isChecked();
            viewModel.updateItemAddedSetting(isChecked);
        });

        binding.switchItemActualizado.setOnClickListener(v -> {
            boolean isChecked = binding.switchItemActualizado.isChecked();
            viewModel.updateItemStatusChangedSetting(isChecked);
        });

        binding.switchItemEliminado.setOnClickListener(v -> {
            boolean isChecked = binding.switchItemEliminado.isChecked();
            viewModel.updateItemDeletedSetting(isChecked);
        });

        viewModel.getMe();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}