package com.university.project;

import com.university.project.controller.AuthorController;
import com.university.project.view.AuthorView;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Main class for the University Researcher Data Integration application.
 * This is the entry point of the program. It sets up the MVC components,
 * loads configuration, and starts the data fetching process.
 */

public class Main {

    /**
     * The main method that runs when the program is executed.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        // Load environment variables from the .env file
        Dotenv dotenv = Dotenv.load();

        // Read the secret API Key from the loaded environment variables.
        final String API_KEY = dotenv.get("SERPAPI_KEY"); 
        
        // The ID of the Google Scholar author we want to search for.
        final String AUTHOR_ID = "kukA0LcAAAAJ";


        // Validate that the API Key was found. If not, exit with an error.
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("Error: 'SERPAPI_KEY' not found in the .env file.");
            return;
        }


// MVC INITIALIZATION

        // 1. Instantiate the View: Responsible for displaying the output.
        AuthorView view = new AuthorView();

        // 2. Instantiate the Controller: Responsible for the business logic.
        AuthorController controller = new AuthorController(view);

        // 3. Start the application logic by calling the controller.
        controller.fetchAuthorData(AUTHOR_ID, API_KEY);
    }
}