package com.university.project;

import com.university.project.controller.AuthorController;
import com.university.project.view.AuthorView;
import io.github.cdimascio.dotenv.Dotenv;

import com.university.project.dao.ArticleDao;

/**
 * Main class for the University Researcher Data Integration application.
 * This is the entry point of the program. It sets up the MVC components,
 * loads configuration, and starts the data fetching process.
 */

public class Main {

    /**
     * The main method that runs when the program is executed.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        // Load environment variables from the .env file
        Dotenv dotenv = Dotenv.load();

        // Read the secret API Key from the loaded environment variables.
        final String API_KEY = dotenv.get("SERPAPI_KEY");

        // The ID of the Google Scholar author we want to search for.
        final String AUTHOR_ID = "kukA0LcAAAAJ";

        final String DB_URL = dotenv.get("DB_URL");
        final String DB_USER = dotenv.get("DB_USER");
        final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

        // Validate that the API Key was found. If not, exit with an error.
        if (API_KEY == null || DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
            System.err.println(
                    "Error: Check that all variables (API_KEY, DB_URL, DB_USER, DB_PASSWORD) are in the .env file.");
            return;
        }

        String[] authorIds = { "kukA0LcAAAAJ", "FRs-7gAAAAAJ" };

        // MVC INITIALIZATION

        // 1. Instantiate the View: Responsible for displaying the output.
        AuthorView view = new AuthorView();

        ArticleDao dao = new ArticleDao(DB_URL, DB_USER, DB_PASSWORD);

        // 2. Instantiate the Controller: Responsible for the business logic.
        AuthorController controller = new AuthorController(view, dao);

        // 3. Start the application logic by calling the controller.
        for (String authorId : authorIds) {
            controller.fetchAuthorData(authorId, API_KEY);
            System.out.println("\n--------------------------------------------------\n");
        }
    }
}
