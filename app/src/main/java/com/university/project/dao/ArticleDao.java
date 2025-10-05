package com.university.project.dao;

import com.university.project.model.Article;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO (Data Access Object) for handling all database operations related to Articles.
 * This class isolates the database logic from the rest of the application.
 */
public class ArticleDao {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ArticleDao(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Saves an Article object to the database, associated with a specific researcher ID.
     * @param article The Article object to save.
     * @param researcherId The foreign key of the researcher in the 'researchers' table.
     */
    public void saveArticle(Article article, int researcherId) {
        // The SQL query now includes the new columns.
        String sql = "INSERT INTO articles (title, publication_info, article_link, year, cited_by, researcher_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getPublication());
            pstmt.setString(3, article.getLink());
            pstmt.setString(4, article.getYear());
            
            // Safely get the citation count to avoid NullPointerException if 'citedBy' is missing.
            int citationCount = (article.getCitedBy() != null) ? article.getCitedBy().getValue() : 0;
            pstmt.setInt(5, citationCount);
            
            // Use the researcher ID as the foreign key.
            pstmt.setInt(6, researcherId);
            
            pstmt.executeUpdate();
            System.out.println("-> Article saved to DB: " + article.getTitle());

        } catch (SQLException e) {
            System.err.println("!!! Error saving article to database: " + e.getMessage());
        }
    }
}