-- This script sets up the required database and tables for the project.

CREATE DATABASE IF NOT EXISTS university_research;

USE university_research;

-- Drop tables if they exist to ensure a clean start
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS researchers;

-- Create the 'researchers' table (Parent table)
CREATE TABLE researchers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    scholar_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    affiliations TEXT,
    is_tracked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the 'articles' table (Child table)
CREATE TABLE articles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    authors TEXT,
    publication_info VARCHAR(255),
    article_link TEXT,
    year VARCHAR(10),
    cited_by INT,
    researcher_id INT NOT NULL,
    FOREIGN KEY (researcher_id) REFERENCES researchers(id)
);

-- Optional: Pre-populate with the researchers to be tracked
INSERT INTO researchers (scholar_id, name, is_tracked) VALUES ('kukA0LcAAAAJ', 'Yoshua Bengio', TRUE);
INSERT INTO researchers (scholar_id, name, is_tracked) VALUES ('FRs-7gAAAAAJ', 'Geoffrey Hinton', TRUE);