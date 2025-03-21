package com.example.animalwiki.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.animalwiki.R;

public class AnimalDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        // Configurar la Toolbar como ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Habilitar el botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Obtener referencias a las vistas
        TextView animalNameTextView = findViewById(R.id.animalName);
        TextView taxonomyTextView = findViewById(R.id.taxonomy);
        TextView locationsTextView = findViewById(R.id.locations);
        TextView characteristicsTextView = findViewById(R.id.characteristics);
        View mainLayout = findViewById(R.id.main);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        String animalName = intent.getStringExtra("animalName");
        String taxonomy = intent.getStringExtra("taxonomy");
        String locations = intent.getStringExtra("locations");
        String characteristics = intent.getStringExtra("characteristics");

        // Establecer los datos en las vistas
        animalNameTextView.setText(animalName);
        taxonomyTextView.setText(taxonomy);
        locationsTextView.setText(locations);
        characteristicsTextView.setText(characteristics);

        // Aplicar padding dinámico para evitar que la cabecera tape el contenido
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Manejar el botón de retroceso
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}