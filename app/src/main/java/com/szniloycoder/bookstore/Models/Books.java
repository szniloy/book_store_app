package com.szniloycoder.bookstore.Models;

import java.util.ArrayList;

public class Books {

    String Description;
    String Language;
    Long Pages;
    String Pdf;
    String Poster;
    String Title;
    Float  ratings;
    ArrayList<String> Genre;

    // Default constructor
    public Books() {
    }

    // Constructor with parameters
    public Books(String description, String language, Long pages, String pdf, String poster, String title, Float ratings, ArrayList<String> genre) {
        this.Description = description;
        this.Language = language;
        this.Pages = pages;
        this.Pdf = pdf;
        this.Poster = poster;
        this.Title = title;
        this.ratings = ratings;
        this.Genre = genre;
    }

    // Getters and Setters
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        this.Language = language;
    }

    public Long getPages() {
        return Pages;
    }

    public void setPages(Long pages) {
        if (pages > 0) {
            this.Pages = pages;
        } else {
            throw new IllegalArgumentException("Pages must be a positive number.");
        }
    }

    public String getPdf() {
        return Pdf;
    }

    public void setPdf(String pdf) {
        this.Pdf = pdf;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        if (ratings >= 0 && ratings <= 5) {
            this.ratings = ratings;
        } else {
            throw new IllegalArgumentException("Ratings must be between 0 and 5.");
        }
    }

    public ArrayList<String> getGenre() {
        return Genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.Genre = genre;
    }
}
