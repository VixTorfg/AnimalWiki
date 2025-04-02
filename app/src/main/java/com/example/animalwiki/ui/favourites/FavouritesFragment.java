package com.example.animalwiki.ui.favourites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.animalwiki.R;
import com.example.animalwiki.ui.search.AnimalDetailsActivity;

import java.util.Map;

public class FavouritesFragment extends Fragment {

    private LinearLayout favoritesContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourites, container, false);
        favoritesContainer = root.findViewById(R.id.favoritesContainer);

        loadFavorites();

        return root;
    }

    // Método para cargar los favoritos desde SharedPreferences
    private void loadFavorites() {
        favoritesContainer.removeAllViews(); // Limpiar lista previa

        // Leer datos desde SharedPreferences
        Map<String, ?> favorites = requireContext()
                .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                .getAll();

        for (Map.Entry<String, ?> entry : favorites.entrySet()) {
            String animalName = entry.getKey();

            // Crear dinámicamente un TextView para cada animal favorito
            TextView textView = new TextView(getContext());
            textView.setText(animalName);
            textView.setTextSize(18);
            textView.setPadding(16, 16, 16, 16);
            textView.setBackgroundResource(R.drawable.bg_list_item);
            textView.setClickable(true);
            textView.setOnClickListener(v -> openAnimalDetails(animalName));

            favoritesContainer.addView(textView);
        }
    }

    // Método para abrir la pantalla de detalles
    private void openAnimalDetails(String animalName) {
        // Recuperar los detalles del animal desde SharedPreferences
        String taxonomy = requireContext()
                .getSharedPreferences("animal_details", Context.MODE_PRIVATE)
                .getString(animalName + "_taxonomy", "");
        String locations = requireContext()
                .getSharedPreferences("animal_details", Context.MODE_PRIVATE)
                .getString(animalName + "_locations", "");
        String characteristics = requireContext()
                .getSharedPreferences("animal_details", Context.MODE_PRIVATE)
                .getString(animalName + "_characteristics", "");

        Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
        intent.putExtra("animalName", animalName);
        intent.putExtra("taxonomy", taxonomy);
        intent.putExtra("locations", locations);
        intent.putExtra("characteristics", characteristics);
        startActivity(intent);
    }
}
