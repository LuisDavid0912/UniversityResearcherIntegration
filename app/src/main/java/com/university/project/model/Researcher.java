package com.university.project.model;

/**
 * Data model for a researcher stored in the database.
 * This class is distinct from the API's Author model and represents the persistent data structure.
 */
public class Researcher {
    private int id;
    private String scholarId;
    private String name;
    private String affiliations;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getScholarId() { return scholarId; }
    public void setScholarId(String scholarId) { this.scholarId = scholarId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAffiliations() { return affiliations; }
    public void setAffiliations(String affiliations) { this.affiliations = affiliations; }
}