package com.university.project.model;

import com.google.gson.annotations.SerializedName;

/**
 * Data model representing a single academic publication.
 * This class maps to an object within the "articles" array in the SerpApi JSON response.
 */
public class Article {
    private String title;
    private String link;
    private String publication;
    private String year;
    
    // Using @SerializedName because "cited_by" in JSON is not a standard Java variable name.
    // This tells Gson to map the "cited_by" JSON field to this 'citedBy' variable.
    @SerializedName("cited_by")
    private CitedBy citedBy;

    // --- Getters and Setters for existing fields ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPublication() { return publication; }
    public void setPublication(String publication) { this.publication = publication; }

    // --- Getters and Setters for new fields ---
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public CitedBy getCitedBy() { return citedBy; }
    public void setCitedBy(CitedBy citedBy) { this.citedBy = citedBy; }
    
    /**
     * Provides a simple string representation of the article for display purposes.
     * @return A formatted string with the article's title and publication info.
     */
    @Override
    public String toString() {
        // Safely get the citation count, defaulting to 0 if the object is null.
        int citations = (citedBy != null) ? citedBy.getValue() : 0;
        return "  - Title: " + title + "\n" +
               "    Publication: " + (publication != null ? publication : "N/A") + "\n" +
               "    Year: " + (year != null ? year : "N/A") + " | Cited by: " + citations;
    }
}