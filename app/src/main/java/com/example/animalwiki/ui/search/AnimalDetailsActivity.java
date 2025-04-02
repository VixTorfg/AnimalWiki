package com.example.animalwiki.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.animalwiki.R;

public class AnimalDetailsActivity extends AppCompatActivity {

    private boolean isFavorite = false;

    // Definir las variables al nivel de clase
    private String animalName;
    private String taxonomy;
    private String locations;
    private String characteristics;

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
        animalName = intent.getStringExtra("animalName");
        taxonomy = intent.getStringExtra("taxonomy");
        locations = intent.getStringExtra("locations");
        characteristics = intent.getStringExtra("characteristics");

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

        // Botón de favorito
        ImageButton favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_star_filled); // Icono estrella llena
                Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                saveFavorite(animalName); // Guarda el favorito
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_border); // Icono estrella vacía
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                removeFavorite(animalName); // Elimina el favorito
            }
        });

        // Inicializa el estado de favorito si ya está marcado
        if (isAnimalFavorite(animalName)) {
            isFavorite = true;
            favoriteButton.setImageResource(R.drawable.ic_star_filled);
        }
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

    // Métodos para gestionar favoritos (almacenar en SharedPreferences o base de datos)
    private void saveFavorite(String animalName) {
        getSharedPreferences("favorites", MODE_PRIVATE)
                .edit()
                .putBoolean(animalName, true)
                .apply();

        // Guardar detalles completos para mostrar en favoritos
        getSharedPreferences("animal_details", MODE_PRIVATE)
                .edit()
                .putString(animalName + "_taxonomy", taxonomy)
                .putString(animalName + "_locations", locations)
                .putString(animalName + "_characteristics", characteristics)
                .apply();
    }

    private void removeFavorite(String animalName) {
        getSharedPreferences("favorites", MODE_PRIVATE)
                .edit()
                .remove(animalName)
                .apply();

        // Eliminar detalles del animal
        getSharedPreferences("animal_details", MODE_PRIVATE)
                .edit()
                .remove(animalName + "_taxonomy")
                .remove(animalName + "_locations")
                .remove(animalName + "_characteristics")
                .apply();
    }

    private boolean isAnimalFavorite(String animalName) {
        return getSharedPreferences("favorites", MODE_PRIVATE)
                .getBoolean(animalName, false);
    }
}
