package com.university.project.model;

/**
 * Data model representing the 'serpapi_pagination' object in the API response.
 * It contains the URL for the next page of results.
 */
public class SerpApiPagination {

    private String next;

    /**
     * Gets the URL for the next page of results.
     * @return The 'next' page URL string, or null if it's the last page.
     */
    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}