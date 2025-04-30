package com.example.animalwiki.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animalwiki.R;
import com.example.animalwiki.models.NewsArticle;

import java.util.List;

public class NewsCarouselAdapter extends RecyclerView.Adapter<NewsCarouselAdapter.NewsViewHolder> {

    private List<NewsArticle> articles;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NewsArticle article);
    }

    public NewsCarouselAdapter(List<NewsArticle> articles, OnItemClickListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_carousel, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articles.get(position);
        holder.bind(article, listener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
        }

        public void bind(NewsArticle article, OnItemClickListener listener) {
            title.setText(article.getTitle());
            Glide.with(image.getContext())
                    .load(article.getImage())
                    .into(image);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(article);
                }
            });
        }
    }
}