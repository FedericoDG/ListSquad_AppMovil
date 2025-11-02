package com.federicodg80.listly;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.federicodg80.listly.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ImageView logo = findViewById(R.id.logoImage);
        View googleBtn = findViewById(R.id.btnGoogle);

        logo.setAlpha(0f);
        googleBtn.setAlpha(0f);

        logo.animate().alpha(1f).setDuration(1000).start();
        googleBtn.animate().alpha(1f).setDuration(1000).setStartDelay(500).start();
    }

}
