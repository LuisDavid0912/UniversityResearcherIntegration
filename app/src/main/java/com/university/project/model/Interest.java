package com.university.project.model;

/**
 * Data model representing a single research interest.
 * The API returns interests as objects with a title and a link.
 * This class maps to an object within the "interests" array.
 */

public class Interest {
    // The API sends us the interests with a title and a link.
    // For now, we are only interested in the title.
    private String title;

    // Methods for obtaining the title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}