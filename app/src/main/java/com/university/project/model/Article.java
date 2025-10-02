package com.university.project.model;

/**
 * Data model representing a single academic publication.
 * This class maps to an object within the "articles" array in the SerpApi JSON response.
 */

public class Article {
    private String title;
    private String link;
    private String publication;

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPublication() { return publication; }
    public void setPublication(String publication) { this.publication = publication; } 
    
        /**
     * Provides a simple string representation of the article for display purposes.
     * @return A formatted string with the article's title and publication info.
     */

    @Override
    public String toString() {
        return "  - Title: " + title + "\n" +
               "    Publication: " + (publication != null ? publication : "N/A");
    }
}