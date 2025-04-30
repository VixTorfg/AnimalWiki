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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animalwiki.R;
import com.example.animalwiki.ui.search.AnimalDetailsActivity;

import java.util.Map;
import java.util.Set;

public class FavouritesFragment extends Fragment {

    private LinearLayout favoritesContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        favoritesContainer = view.findViewById(R.id.favoritesContainer);
        loadFavorites();
        return view;
    }

    private void loadFavorites() {
        favoritesContainer.removeAllViews(); // Limpiar vistas anteriores

        Map<String, ?> favorites = requireContext()
                .getSharedPreferences("favorites", Context.MODE_PRIVATE)
                .getAll();

        Set<String> favoriteAnimalNames = favorites.keySet();

        for (String animalName : favoriteAnimalNames) {
            // Inflar el layout de cada elemento de la lista (item_animal.xml)
            View favoriteItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_animal, favoritesContainer, false);

            // Obtener referencia al TextView del nombre del animal
            TextView animalNameTextView = favoriteItemView.findViewById(R.id.animalName);

            // Establecer el nombre del animal
            animalNameTextView.setText(animalName);

            // Añadir un listener para abrir los detalles del animal
            favoriteItemView.setOnClickListener(v -> {
                openAnimalDetails(animalName);
            });

            // Añadir la vista al contenedor
            favoritesContainer.addView(favoriteItemView);
        }
    }

    private void openAnimalDetails(String animalName) {
        // Obtener los detalles del animal desde SharedPreferences
        Map<String, ?> animalDetails = requireContext()
                .getSharedPreferences("animal_details", Context.MODE_PRIVATE)
                .getAll();

        String taxonomy = (String) animalDetails.get(animalName + "_taxonomy");
        String locations = (String) animalDetails.get(animalName + "_locations");
        String characteristics = (String) animalDetails.get(animalName + "_characteristics");

        // Crear un Intent para abrir AnimalDetailsActivity
        Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
        intent.putExtra("animalName", animalName);
        intent.putExtra("taxonomy", taxonomy);
        intent.putExtra("locations", locations);
        intent.putExtra("characteristics", characteristics);

        // Iniciar la actividad
        startActivity(intent);
    }
}