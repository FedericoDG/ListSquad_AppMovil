package com.federicodg80.listly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.annotation.NonNull;

import com.federicodg80.listly.databinding.ActivityMainBinding;
import com.federicodg80.listly.services.KeepAliveService;
import com.tapadoo.alerter.Alerter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d("MainActivity", "Permiso de notificaciones concedido: " + isGranted);
            });

    private final BroadcastReceiver alertReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");

            Alerter.create(MainActivity.this)
                    .setTitle(title)
                    .setText(body)
                    .setBackgroundColorRes(R.color.purple_500)
                    .setDuration(3000)
                    .setOnClickListener(v -> {
                        // Aquí navegamos usando los extras de la notificación
                        viewModel.handleNavigationIntent(intent, navController);
                    })
                    .show();
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        // Primero, manejamos el pago si viene por esquema
        viewModel.handlePaymentResult(intent);

        // Después, manejamos navegación por notificaciones push
        viewModel.handleNavigationIntent(intent, navController);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forzar modo claro (desactivar modo oscuro)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Iniciar Foreground Service para mantener la app viva
        Intent serviceIntent = new Intent(this, KeepAliveService.class);
        startForegroundService(serviceIntent);

        // Configurar navegación
        setupNavigation();

        // Solicitar permiso de notificaciones en Android 13+
        askNotificationPermission();

        // Observar estado de pago
        viewModel.getPaymentStatus().observe(this, status -> {
           /* if (status != null) {*/
                Bundle args = new Bundle();
                args.putString("payment_status", status);
                navController.navigate(R.id.navigation_payment_result, args);
         /*   }*/
        });
        // Manejar intención de navegación si existe
        viewModel.handleNavigationIntent(getIntent(), navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("ACTION_SHOW_MOTION_TOAST");
        LocalBroadcastManager.getInstance(this).registerReceiver(alertReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alertReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void setupNavigation() {
        try {
            setSupportActionBar(binding.appbar);

            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_invitations, R.id.navigation_profile
            ).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        } catch (Exception e) {
            Log.e("MainActivity", "Error en navegación: " + e.getMessage(), e);
        }
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int permissionStatus = ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
            );

            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            Log.d("MainActivity", "Android < 13, no se necesita solicitar permiso");
        }
    }
}