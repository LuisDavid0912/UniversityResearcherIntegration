package com.university.project.controller;

import com.google.gson.Gson;
import com.university.project.model.ApiResponse;
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

/**
 * The Controller in the MVC pattern.
 * It handles the main business logic: fetching data from the API,
 * parsing it into the Model, and telling the View to display it.
 */
public class AuthorController {
    private AuthorView view;

    /**
     * Constructs the controller and links it to the view.
     * 
     * @param view The view instance responsible for displaying data.
     */

    public AuthorController(AuthorView view) {
        this.view = view;
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

            System.out.println("Ejecutando peticion a: " + url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                // 200 OK is the standard code for a successful HTTP request.
                if (statusCode == 200) {

                    // INICIO DE LA MODIFICACIÓN PARA DEPURAR ----
                    // System.out.println("--- RESPUESTA JSON RECIBIDA ---");
                    // System.out.println(responseBody);


                    // Use Gson to automatically map the JSON string to our Java objects.
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);

                    // Tell the view to display the data from the now-populated model objects.
                    view.displayAuthorDetails(apiResponse.getAuthor(), apiResponse.getArticles());
                } else {
                    // Handle cases where the API returns an error status (e.g., 400, 401, 500).
                    view.displayError("Error en la API: " + statusCode + "\nRespuesta: " + responseBody);
                }
            }
        } catch (IOException e) {
            // Handle network errors (e.g., no internet connection).
            view.displayError("Error de conexion: " + e.getMessage());
        }
    }


    /**
     * Builds the complete URL for the SerpApi request.
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
                encodedAuthorId,
                encodedApiKey);
    }
}