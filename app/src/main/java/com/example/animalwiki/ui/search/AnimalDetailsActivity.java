package com.example.animalwiki.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.animalwiki.R;

public class AnimalDetailsActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);
    
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
        taxonomyTextView.setText("Taxonomy: " + taxonomy);
        locationsTextView.setText("Locations: " + locations);
        characteristicsTextView.setText("Characteristics: " + characteristics);
    
        // Aplicar padding dinámico para evitar que la cabecera tape el contenido
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}