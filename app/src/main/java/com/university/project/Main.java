package com.university.project;

import com.university.project.controller.AuthorController;
import com.university.project.dao.ResearcherDao;
import com.university.project.view.AuthorView;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;

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

        final String DB_URL = dotenv.get("DB_URL");
        final String DB_USER = dotenv.get("DB_USER");
        final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

        if (API_KEY == null || DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
            System.err.println("Error: Check that all variables are in the .env file.");
            return;
        }

        // --- INITIALIZE COMPONENTS ---
        AuthorView view = new AuthorView();
        ArticleDao articleDao = new ArticleDao(DB_URL, DB_USER, DB_PASSWORD);
        ResearcherDao researcherDao = new ResearcherDao(DB_URL, DB_USER, DB_PASSWORD);
        AuthorController controller = new AuthorController(view, articleDao, researcherDao);
        
        // --- DYNAMICALLY FETCH THE LIST OF RESEARCHERS TO PROCESS ---
        // The hardcoded array is gone! Now we ask the database for the list of tasks.
        List<String> authorIdsToProcess = researcherDao.findAllTrackedResearcherIds();

        if (authorIdsToProcess.isEmpty()) {
            System.out.println("No researchers are marked for tracking in the database. Exiting.");
            return;
        }

        // --- EXECUTE PROCESS ---
        for (String authorId : authorIdsToProcess) {
            controller.fetchAuthorData(authorId, API_KEY, 50); // Using the limit of 50 we set before
            System.out.println("\n--------------------------------------------------\n");
        }
    }
}