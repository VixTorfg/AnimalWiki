package com.example.animalwiki.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.animalwiki.adapters.NewsCarouselAdapter;
import com.example.animalwiki.api.GNewsApi;
import com.example.animalwiki.api.NewsService;
import com.example.animalwiki.databinding.FragmentHomeBinding;
import com.example.animalwiki.models.NewsArticle;
import com.example.animalwiki.models.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Handler carouselHandler;
    private Runnable carouselRunnable;
    private boolean isUserInteracting = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        ViewPager2 carousel = binding.newsCarousel;

        // Configurar el adaptador y observar los datos
        homeViewModel.getNewsArticles().observe(getViewLifecycleOwner(), articles -> {
            if (articles != null && !articles.isEmpty()) {
                NewsCarouselAdapter adapter = new NewsCarouselAdapter(articles, article -> {
                    // Mostrar detalles de la noticia seleccionada
                    binding.newsDetailContainer.setVisibility(View.VISIBLE);
                    binding.newsTitle.setText(article.getTitle());
                    binding.newsDescription.setText(article.getDescription());
                    binding.newsUrl.setText(article.getUrl());
                });
                carousel.setAdapter(adapter);

                // Iniciar rotación automática
                startAutoScroll(carousel, articles.size());
            }
        });

        // Configurar el callback para detener la rotación si el usuario interactúa
        carousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                isUserInteracting = (state == ViewPager2.SCROLL_STATE_DRAGGING);
            }
        });

        fetchNews();

        return root;
    }

    private void fetchNews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gnews.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GNewsApi gNewsApi = retrofit.create(GNewsApi.class);
        gNewsApi.getTopHeadlines("5d9d4e819d11f228b097c7624d84958c", "en", "animals").enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsArticle> articles = response.body().getArticles();
                    Log.d("API_RESPONSE", "Articles: " + articles.toString());
                    homeViewModel.setNewsArticles(articles);
                } else {
                    Log.e("API_RESPONSE", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "Failure: " + t.getMessage());
            }
        });
    }

    private void startAutoScroll(ViewPager2 carousel, int itemCount) {
        carouselHandler = new Handler();
        carouselRunnable = new Runnable() {
            int currentItem = 0;

            @Override
            public void run() {
                if (!isUserInteracting) {
                    currentItem = (currentItem + 1) % itemCount; // Rotar entre los elementos
                    carousel.setCurrentItem(currentItem, true);
                }
                carouselHandler.postDelayed(this, 5000); // Rotar cada 5 segundos
            }
        };
        carouselHandler.postDelayed(carouselRunnable, 5000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (carouselHandler != null && carouselRunnable != null) {
            carouselHandler.removeCallbacks(carouselRunnable);
        }
    }
}