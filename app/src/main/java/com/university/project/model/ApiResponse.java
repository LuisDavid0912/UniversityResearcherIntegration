package com.university.project.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Represents the top-level structure of the JSON response from the SerpApi Google Scholar Author API.
 * This class is used by Gson to parse the main objects from the response.
 */
public class ApiResponse {
    private Author author;
    private List<Article> articles;
    
    // Add the pagination object, mapping it from the JSON's "serpapi_pagination" field.
    @SerializedName("serpapi_pagination")
    private SerpApiPagination serpapiPagination;

    // --- Getters and Setters ---
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public List<Article> getArticles() { return articles; }
    public void setArticles(List<Article> articles) { this.articles = articles; }

    public SerpApiPagination getSerpapiPagination() { return serpapiPagination; }
    public void setSerpapiPagination(SerpApiPagination serpapiPagination) { this.serpapiPagination = serpapiPagination; }
}