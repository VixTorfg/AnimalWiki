package com.example.animalwiki.models;

import java.util.List;

public class NewsResponse {
    private List<NewsArticle> articles;

    // Getter y setter
    public List<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
    }
}