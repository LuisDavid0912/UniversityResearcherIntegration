package com.university.project.controller;

import com.google.gson.Gson;

import com.university.project.dao.ArticleDao;

import com.university.project.model.ApiResponse;

import com.university.project.model.Article;
import com.university.project.model.Author;

import com.university.project.view.AuthorView;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.List;

/**
 * The Controller in the MVC pattern.
 * It handles the main business logic: fetching data from the API,
 * parsing it into the Model, and telling the View to display it.
 */
public class AuthorController {
    private final AuthorView view;
    private final ArticleDao dao;

    /**
     * Constructs the controller and links it to the view.
     * 
     * @param view The view instance responsible for displaying data.
     */

    public AuthorController(AuthorView view, ArticleDao dao) {
        this.view = view;
        this.dao = dao;
    }

    /**
     * Fetches author data from the SerpApi for a given author ID.
     * 
     * @param authorId The Google Scholar author ID to search for.
     * @param apiKey   The SerpApi private key for authentication.
     */

    public void fetchAuthorData(String authorId, String apiKey) {
        String url = buildUrl(authorId, apiKey);

        // I use a try-with-resources statement to ensure the HttpClient is closed
        // automatically.
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            System.out.println("Executing request to: " + url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                // 200 OK is the standard code for a successful HTTP request.
                if (statusCode == 200) {
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);

                    

                    // --- START OF ROBUSTNESS CORRECTION ---
                    // EVEN IF THE STATUS IS 200, WE VERIFY THAT THE API HAS PROVIDED US WITH THE DATA
                    if (apiResponse.getAuthor() == null || apiResponse.getArticles() == null) {
                        view.displayError("The API returned a successful response but no data for the author with ID: "
                                + authorId);
                        return; // We stop execution FOR THIS AUTHOR and continue with the next one
                    }
                    // --- END OF CORRECTION ---


                    // If we get here, it means that the data does exist.
                    view.displayAuthorDetails(apiResponse.getAuthor(), apiResponse.getArticles());

                    System.out.println("\n--- Saving items to the database ---");
                    List<Article> articles = apiResponse.getArticles();
                    String researcherName = apiResponse.getAuthor().getName();

                    if (articles != null && !articles.isEmpty()) {
                        for (int i = 0; i < 3 && i < articles.size(); i++) {
                            dao.saveArticle(articles.get(i), researcherName);
                        }
                    }
                } else {
                    view.displayError("API Error: " + statusCode + "\nResponse: " + responseBody);
                }
            }
        } catch (IOException e) {
            // Handle network errors (e.g., no internet connection).
            view.displayError("Connection error: " + e.getMessage());
        }
    }

    /**
     * Builds the complete URL for the SerpApi request.
     * 
     * @param authorId The author ID to include in the URL.
     * @param apiKey   The API key to include in the URL.
     * @return A fully formed and encoded URL string.
     */

    private String buildUrl(String authorId, String apiKey) {
        // URL-encode parameters to handle special characters safely.
        String encodedAuthorId = URLEncoder.encode(authorId, StandardCharsets.UTF_8);
        String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        return String.format(
                "https://serpapi.com/search.json?engine=google_scholar_author&author_id=%s&api_key=%s",
                encodedAuthorId, encodedApiKey);
    }
}