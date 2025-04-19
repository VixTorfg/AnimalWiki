package com.example.animalwiki.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.animalwiki.models.NewsArticle;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<NewsArticle>> newsArticles = new MutableLiveData<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<NewsArticle>> getNewsArticles() {
        return newsArticles;
    }

    public void setNewsArticles(List<NewsArticle> articles) {
        newsArticles.setValue(articles);
    }
}