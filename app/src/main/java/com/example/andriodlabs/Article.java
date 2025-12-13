package com.example.andriodlabs;

public class Article {
    public String title;
    public String link;
    public String description;
    public String pubDate;

    public Article(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return title;
    }
}
