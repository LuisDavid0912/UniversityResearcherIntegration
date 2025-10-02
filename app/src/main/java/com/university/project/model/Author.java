package com.university.project.model;

import java.util.List;

/**
 * Data model representing a researcher's profile information.
 * This class maps to the "author" object in the SerpApi JSON response.
 */

public class Author {
    private String name;
    private String affiliations;
    // private List<String> interests;
    private List<Interest> interests;
    // private List<Article> articles;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAffiliations() { return affiliations; }
    public void setAffiliations(String affiliations) { this.affiliations = affiliations; }
    // public List<String> getInterests() { return interests; }
    // public void setInterests(List<String> interests) { this.interests = interests; }
    public List<Interest> getInterests() { return interests; }
public void setInterests(List<Interest> interests) { this.interests = interests; }
    // public List<Article> getArticles() { return articles; }
    // public void setArticles(List<Article> articles) { this.articles = articles; }
}