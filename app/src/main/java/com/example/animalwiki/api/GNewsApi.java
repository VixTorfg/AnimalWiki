package com.example.animalwiki.api;

import android.util.Log;

import com.example.animalwiki.models.NewsResponse;
import com.example.animalwiki.models.NewsArticle;
import com.example.animalwiki.ui.home.HomeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GNewsApi {
    @GET("v4/top-headlines")
    Call<NewsResponse> getTopHeadlines(
        @Query("token") String apiKey,  // Clave de API
        @Query("lang") String language, // Idioma de las noticias
        @Query("q") String query        // Palabras clave para filtrar noticias
    );
}
