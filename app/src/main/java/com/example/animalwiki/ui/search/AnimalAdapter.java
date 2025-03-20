package com.example.animalwiki.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalwiki.R;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private final List<String> animalNames;
    private final OnAnimalClickListener listener;

    public interface OnAnimalClickListener {
        void onAnimalClick(String animalName);
    }

    public AnimalAdapter(List<String> animalNames, OnAnimalClickListener listener) {
        this.animalNames = animalNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        String animalName = animalNames.get(position);
    
        holder.animalName.setText(animalName);
        holder.itemView.setOnClickListener(v -> {
            listener.onAnimalClick(animalName);
        });
    }

    @Override
    public int getItemCount() {
        return animalNames.size();
    }

    static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView animalName;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            animalName = itemView.findViewById(R.id.animalName);
        }
    }
}
