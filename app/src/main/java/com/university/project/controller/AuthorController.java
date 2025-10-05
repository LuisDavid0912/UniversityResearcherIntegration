package com.university.project.controller;

import com.google.gson.Gson;
import com.university.project.dao.ArticleDao;
import com.university.project.dao.ResearcherDao;
import com.university.project.model.ApiResponse;
import com.university.project.model.Article;
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


public class AuthorController {
    private final AuthorView view;
    private final ArticleDao articleDao;
    private final ResearcherDao researcherDao;

    public AuthorController(AuthorView view, ArticleDao articleDao, ResearcherDao researcherDao) {
        this.view = view;
        this.articleDao = articleDao;
        this.researcherDao = researcherDao;
    }

    /**
     * Fetches author data from SerpApi, respecting a maximum number of articles, and handles pagination.
     * @param authorId The Google Scholar author ID.
     * @param apiKey   The SerpApi private key.
     * @param articleLimit The maximum number of articles to save for this author.
     */
    public void fetchAuthorData(String authorId, String apiKey, int articleLimit) {
        String currentUrl = buildUrl(authorId, apiKey, 0);
        int researcherId = -1;
        int pageCount = 1;
        int totalArticlesSaved = 0;

        do {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(currentUrl);
                System.out.println("Executing request for page " + pageCount + ": " + currentUrl);

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    String responseBody = EntityUtils.toString(response.getEntity());

                    if (statusCode == 200) {
                        Gson gson = new Gson();
                        ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
                        
                        if (pageCount == 1) {
                            if (apiResponse.getAuthor() == null) {
                                view.displayError("API response successful but no author data for ID: " + authorId);
                                return;
                            }
                            view.displayAuthorDetails(apiResponse.getAuthor(), apiResponse.getArticles());
                            System.out.println("\n--- Processing and saving to Database (limit: " + articleLimit + " articles) ---");
                            researcherId = researcherDao.findOrCreateResearcher(apiResponse.getAuthor(), authorId);
                        }
                        
                        if (researcherId != -1) {
                            List<Article> articles = apiResponse.getArticles();
                            if (articles != null && !articles.isEmpty()) {
                                for (Article article : articles) {
                                    // If we have reached the limit, stop saving articles.
                                    if (totalArticlesSaved >= articleLimit) {
                                        break; // Exit the inner 'for' loop.
                                    }
                                    articleDao.saveArticle(article, researcherId);
                                    totalArticlesSaved++;
                                }
                            }
                        }

                        // Check for the next page
                        if (apiResponse.getSerpapiPagination() != null && apiResponse.getSerpapiPagination().getNext() != null) {
                            currentUrl = apiResponse.getSerpapiPagination().getNext() + "&api_key=" + apiKey;
                            pageCount++;
                            Thread.sleep(1000);
                        } else {
                            currentUrl = null;
                        }

                    } else {
                        view.displayError("API Error: " + statusCode);
                        currentUrl = null;
                    }
                }
            } catch (IOException | InterruptedException e) {
                view.displayError("An error occurred: " + e.getMessage());
                currentUrl = null;
            }
        // The loop continues as long as there is a next URL AND we haven't reached our article limit.
        } while (currentUrl != null && totalArticlesSaved < articleLimit);

        System.out.println("\n--- Pagination complete. Total articles saved: " + totalArticlesSaved + " ---");
    }

    private String buildUrl(String authorId, String apiKey, int startIndex) {
        String encodedAuthorId = URLEncoder.encode(authorId, StandardCharsets.UTF_8);
        return String.format(
                "https://serpapi.com/search.json?engine=google_scholar_author&author_id=%s&api_key=%s&start=%d",
                encodedAuthorId, apiKey, startIndex);
    }
}