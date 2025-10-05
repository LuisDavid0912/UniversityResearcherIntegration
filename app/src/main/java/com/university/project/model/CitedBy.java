package com.university.project.model;

/**
 * Data model representing the 'cited_by' object in the API response for an article.
 * Used by Gson to parse the nested JSON object.
 */
public class CitedBy {
    private int value;

    // Getter and Setter
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}