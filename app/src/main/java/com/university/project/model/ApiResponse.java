package com.university.project.model;

import java.util.List;

/**
 * Represents the top-level structure of the JSON response from the SerpApi Google Scholar Author API.
 * This class is used by Gson to parse the main objects from the response.
 */
public class ApiResponse {
    private Author author;
    private List<Article> articles; // I Add the list here

    /**
     * Gets the author object from the response.
     * @return The Author object.
     */

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    /**
     * Gets the list of articles from the response.
     * @return A list of Article objects.
     */

    public List<Article> getArticles() { return articles; } // I add the methods here
    public void setArticles(List<Article> articles) { this.articles = articles; }
}