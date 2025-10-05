package com.university.project.dao;

import com.university.project.model.Article;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticleDao {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ArticleDao(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public void saveArticle(Article article, String researcherName) {
        String sql = "INSERT INTO articles (title, publication_info, article_link, researcher_name) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getPublication());
            pstmt.setString(3, article.getLink());
            pstmt.setString(4, researcherName);
            
            pstmt.executeUpdate();
            System.out.println("-> Article saved in the database: " + article.getTitle());

        } catch (SQLException e) {
            System.err.println("!!! Error saving to database: " + e.getMessage());
        }
    }
}