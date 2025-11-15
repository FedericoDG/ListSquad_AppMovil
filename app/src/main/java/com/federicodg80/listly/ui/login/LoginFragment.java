package com.federicodg80.listly.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.federicodg80.listly.R;
import com.federicodg80.listly.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginFragmentViewModel viewModel;
    private GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                } else {
                    Log.e("LoginFragment", "Google sign-in failed with result code: " + result.getResultCode());
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());

        viewModel.getError().observe(getViewLifecycleOwner(), msg -> {
            binding.errorText.setText(msg);
            binding.errorText.setVisibility(View.VISIBLE);
        });

        // Cambiar texto del botón
        SignInButton btnGoogleSignIn = (SignInButton) binding.btnGoogle;
        TextView textView = (TextView) binding.btnGoogle.getChildAt(0);
        textView.setText("Iniciar sesión con Google");

        // Observers
        viewModel.getIscheckExistingUser().observe(getViewLifecycleOwner(), isChecking -> {
            btnGoogleSignIn.setEnabled(!isChecking);
        });

        viewModel.getIsLoging().observe(getViewLifecycleOwner(), isLoging -> {
            binding.btnGoogle.setEnabled(!isLoging);
        });

        // Si ya hay usuario logueado ingresa directamente
        viewModel.checkExistingUser();

        // Events
        binding.btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        return binding.getRoot();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            viewModel.firebaseAuthWithGoogle(completedTask.getResult(ApiException.class));
        } catch (ApiException e) {
            Log.e("LoginFragment", "Error al iniciar sesión");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
