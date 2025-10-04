package com.university.project.view;

import com.university.project.model.Article;
import com.university.project.model.Author;
import java.util.List;

/**
 * The View in the MVC pattern.
 * This class is responsible for presenting data to the user via the console.
 * It does not contain any business logic.
 */
public class AuthorView {

    /**
     * Displays the formatted details of an author and their list of publications.
     * @param author The Author object containing profile information.
     * @param articles The list of Article objects to display.
     */
    public void displayAuthorDetails(Author author, List<Article> articles) {
        // First, check if the author object is valid before trying to access its data.
        if (author == null || author.getName() == null) {
            System.out.println("Could not retrieve author information.");
            return;
        }

        System.out.println("=============================================");
        System.out.println("  Researcher Information Found");
        System.out.println("=============================================");
        System.out.println("Name: " + author.getName());
        System.out.println("Affiliation: " + author.getAffiliations());
        
        // Handle the list of Interest objects correctly
        System.out.print("Interests: ");
        if (author.getInterests() != null) {
            for (int i = 0; i < author.getInterests().size(); i++) {
                System.out.print(author.getInterests().get(i).getTitle());
                if (i < author.getInterests().size() - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println(); // New line after interests

        System.out.println("\n --------------- Publications: ");

        // Check if the articles list is valid and not empty.
        if (articles != null && !articles.isEmpty()) {
            for (Article article : articles) {
                System.out.println(article.toString());
                System.out.println(); // Add a blank line for better readability
            }
        } else {
            System.out.println("No publications were found.");
        }
    }

    /**
     * Displays a formatted error message to the console.
     * @param errorMessage The error message to display.
     */

    public void displayError(String errorMessage) {
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.err.println("An error occurred: " + errorMessage);
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}