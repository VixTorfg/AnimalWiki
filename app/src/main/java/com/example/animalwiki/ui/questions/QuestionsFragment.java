package com.example.animalwiki.ui.questions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animalwiki.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionsFragment extends Fragment {

    private TextView questionText;
    private Button option1;
    private Button option2;
    private Button option3;
    private Button option4;

    private final String API_URL = "https://api.api-ninjas.com/v1/animals?name=";
    private final String API_KEY = "4w6KE4S22ypBI3Ibz7kJ/w==bzsWUNM8WpVhCdMR";

    private String correctAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);

        questionText = view.findViewById(R.id.questionText);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        option4 = view.findViewById(R.id.option4);

        loadQuestion();

        return view;
    }

    // Método para cargar una pregunta
    private void loadQuestion() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", API_KEY);

                if (connection.getResponseCode() == 200) {
                    InputStream responseStream = connection.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(responseStream);

                    if (root.isArray() && root.size() > 3) {
                        List<String> animalNames = new ArrayList<>();
                        for (JsonNode animal : root) {
                            animalNames.add(animal.path("name").asText("N/A"));
                        }

                        // Hacer que correctAnswer y options sean finales
                        final String correctAnswer = animalNames.get((int) (Math.random() * animalNames.size()));

                        final List<String> options = new ArrayList<>(animalNames);
                        options.remove(correctAnswer);
                        Collections.shuffle(options);
                        options.subList(0, 3).add(correctAnswer);
                        Collections.shuffle(options);

                        // Mostrar en el hilo principal
                        new Handler(Looper.getMainLooper()).post(() -> {
                            questionText.setText("¿Cuál de estos es un animal real?");
                            option1.setText(options.get(0));
                            option2.setText(options.get(1));
                            option3.setText(options.get(2));
                            option4.setText(options.get(3));

                            // Asignar listeners
                            setupAnswerButtons(options);
                        });
                    }

                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para comprobar la respuesta
    private void setupAnswerButtons(List<String> options) {
        View.OnClickListener listener = v -> {
            Button selectedButton = (Button) v;
            String selectedAnswer = selectedButton.getText().toString();

            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(getContext(), "¡Correcto! 😎", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Incorrecto 😕", Toast.LENGTH_SHORT).show();
            }

            // Cargar una nueva pregunta tras una pequeña pausa
            new Handler().postDelayed(this::loadQuestion, 1000);
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        option3.setOnClickListener(listener);
        option4.setOnClickListener(listener);
    }
}
