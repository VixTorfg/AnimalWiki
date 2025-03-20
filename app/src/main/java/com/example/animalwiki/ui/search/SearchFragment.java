package com.example.animalwiki.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.animalwiki.R;
import com.example.animalwiki.databinding.FragmentNotificationsBinding;

public class SearchFragment extends Fragment {
    private EditText searchInput;
    private Button searchButton;
    private TextView resultView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        resultView = view.findViewById(R.id.resultView);

        searchButton.setOnClickListener(v -> searchAnimal(searchInput.getText().toString()));

        return view;
    }

    private void searchAnimal(String name) {
        if (name.isEmpty()) {
            resultView.setText("Please enter an animal name");
            return;
        }
        // Aquí irá la lógica para buscar el animal en la API
    }
}
