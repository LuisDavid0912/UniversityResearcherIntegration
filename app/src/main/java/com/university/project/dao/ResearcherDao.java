package com.university.project.dao;

import com.university.project.model.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for handling all database operations related to Researchers.
 */
public class ResearcherDao {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ResearcherDao(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Finds a researcher by their Google Scholar ID. If not found, saves them to
     * the database.
     * This "find or create" logic prevents creating duplicate researchers.
     * 
     * @param author    The author data from the API.
     * @param scholarId The unique Google Scholar ID.
     * @return The database ID (primary key) of the researcher, or -1 if an error
     *         occurred.
     */
    public int findOrCreateResearcher(Author author, String scholarId) {
        String selectSql = "SELECT id FROM researchers WHERE scholar_id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

            pstmt.setString(1, scholarId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If researcher is found, return their existing database ID.
                int existingId = rs.getInt("id");
                System.out.println("Researcher found in DB: " + author.getName() + " (ID: " + existingId + ")");
                return existingId;
            } else {
                // If not found, save a new researcher record.
                System.out.println("Researcher not found. Creating new record for: " + author.getName());
                return saveResearcher(author, scholarId);
            }
        } catch (SQLException e) {
            System.err.println("!!! Error finding or creating researcher: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Saves a new researcher to the database and returns their newly generated
     * primary key.
     * 
     * @param author    The author data from the API.
     * @param scholarId The unique Google Scholar ID.
     * @return The new database ID of the researcher, or -1 if an error occurred.
     */
    private int saveResearcher(Author author, String scholarId) throws SQLException {
        String insertSql = "INSERT INTO researchers (scholar_id, name, affiliations) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, scholarId);
            pstmt.setString(2, author.getName());
            pstmt.setString(3, author.getAffiliations());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        System.out.println("-> Researcher saved to DB with ID: " + newId);
                        return newId;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Finds and returns the Google Scholar IDs of all researchers marked as
     * 'tracked'.
     * 
     * @return A list of scholar_id strings.
     */
    public List<String> findAllTrackedResearcherIds() {
        List<String> trackedIds = new ArrayList<>();
        String sql = "SELECT scholar_id FROM researchers WHERE is_tracked = TRUE";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                trackedIds.add(rs.getString("scholar_id"));
            }
            System.out.println("Found " + trackedIds.size() + " researchers to track in the database.");

        } catch (SQLException e) {
            System.err.println("!!! Error fetching tracked researchers: " + e.getMessage());
        }
        return trackedIds;
    }
}