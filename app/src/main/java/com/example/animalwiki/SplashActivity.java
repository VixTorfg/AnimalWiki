package com.example.animalwiki;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Aquí puedes poner un pequeño delay si quieres, o pasar enseguida
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
