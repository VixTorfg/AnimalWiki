package com.example.animalwiki.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalwiki.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AnimalAdapter.OnAnimalClickListener {
    private EditText searchInput;
    private Button searchButton;
    private RecyclerView animalRecyclerView;
    private AnimalAdapter adapter;
    private List<String> animalNames = new ArrayList<>();

    private final String API_URL = "https://api.api-ninjas.com/v1/animals?name=";
    private final String API_KEY = "4w6KE4S22ypBI3Ibz7kJ/w==bzsWUNM8WpVhCdMR";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        animalRecyclerView = view.findViewById(R.id.animalRecyclerView);

        adapter = new AnimalAdapter(animalNames, this);
        animalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        animalRecyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> searchAnimal(searchInput.getText().toString()));

        return view;
    }

    @Override
    public void onAnimalClick(String animalName) {
        fetchAnimalDetails(animalName);
    }

    private void searchAnimal(String name) {
        if (name.isEmpty()) {
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL(API_URL + name);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", API_KEY);

                if (connection.getResponseCode() == 200) {
                    InputStream responseStream = connection.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(responseStream);

                    animalNames.clear();
                    if (root.isArray() && root.size() > 0) {
                        for (JsonNode animal : root) {
                            animalNames.add(animal.path("name").asText("N/A"));
                        }
                    }

                    new Handler(Looper.getMainLooper()).post(() -> adapter.notifyDataSetChanged());
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchAnimalDetails(String animalName) {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL + animalName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", API_KEY);

                if (connection.getResponseCode() == 200) {
                    InputStream responseStream = connection.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(responseStream);

                    if (root.isArray() && root.size() > 0) {
                        JsonNode animalNode = root.get(0);
                        String taxonomy = animalNode.path("taxonomy").toString();
                        String locations = animalNode.path("locations").toString();
                        String characteristics = animalNode.path("characteristics").toString();

                        Intent intent = new Intent(getContext(), AnimalDetailsActivity.class);
                        intent.putExtra("animalName", animalName);
                        intent.putExtra("taxonomy", taxonomy);
                        intent.putExtra("locations", locations);
                        intent.putExtra("characteristics", characteristics);
                        startActivity(intent);
                    }
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
