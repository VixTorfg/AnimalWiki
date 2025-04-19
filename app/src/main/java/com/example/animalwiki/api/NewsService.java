package com.example.animalwiki.api;

import android.util.Log;

import com.example.animalwiki.models.NewsResponse;
import com.example.animalwiki.models.NewsArticle;
import com.example.animalwiki.ui.home.HomeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsService {
    private final GNewsApi gNewsApi;
    private final HomeViewModel homeViewModel;

    public NewsService(GNewsApi gNewsApi, HomeViewModel homeViewModel) {
        this.gNewsApi = gNewsApi;
        this.homeViewModel = homeViewModel;
    }

    public void fetchTopHeadlines(String API_KEY) {
        gNewsApi.getTopHeadlines(API_KEY, "en", "animals AND wildlife AND environment AND nature").enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsArticle> articles = response.body().getArticles();
                    if (articles.size() > 10) {
                        articles = articles.subList(0, 10); // Limitar a 5 noticias
                    }
                    homeViewModel.setNewsArticles(articles);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Registrar el error
                Log.e("NewsService", "Error fetching news", t);
            }
        });
    }
}