# SerpApi account

<img width="1124" height="671" alt="Screenshot 2025-09-29 at 2 59 26 p m" src="https://github.com/user-attachments/assets/daa770de-d56e-4679-a201-ce6c84ddb49c" />


# University Researcher Data Integration System

This repository contains the source code and documentation for the project to automate the integration of researcher data from Google Scholar.

### Project Purpose
The main goal of this project is to automate the process of retrieving and integrating publication data for the university's top researchers from Google Scholar into a local database. This system will serve as the primary source for generating accurate reports on scientific production.

### Key Functionalities
* Connects to the Google Scholar API via SerpApi to retrieve public data.
* Fetches researcher profiles based on their unique author IDs.
* Retrieves a complete list of all publications for a given researcher.
* Stores this information in a structured MySQL database for analysis and reporting.

### Project Relevance
This project solves the problem of manual, time-consuming, and error-prone data collection. It provides the university with an efficient, reliable, and up-to-date system for generating scientific production reports and analyzing research impact. By automating this workflow, staff can focus on analysis rather than data entry.

---
*This project is being developed as part of the Digital NAO "Server and Database Commands" challenge.*

<details>
<summary>🐛 <strong>Click to expand the detailed Debugging Log for Sprint 2</strong></summary>

### Error 1: `ClassNotFoundException`
* **Situation:** We had just created the Model classes (`Author.java`, `Article.java`) and when trying to run the project for the first time, the program wouldn't start.
* **Error:**
    ```text
    Error: Could not find or load main class com.university.Main
    Caused by: java.lang.ClassNotFoundException: com.university.Main
    ```
* **Diagnosis:** The error meant that Java couldn't find the program's "entry point." This was because the project structure was still incomplete (missing the `Main.java` class, View, and Controller).
* **Solution:** We understood that we couldn't run an incomplete program. The solution was to follow the development plan and build the remaining components (`View` and `Controller`) before attempting another execution.

### Error 2: API Key Not Found (Environment Variable)
* **Situation:** With all the MVC code written, the program would run but stop immediately. We were using `System.getenv()` to read the API Key.
* **Error:**
    ```text
    Error: No se encontró la variable de entorno 'SERPAPI_KEY'.
    Por favor, configúrala antes de ejecutar el programa.
    ```
* **Diagnosis:** The `export SERPAPI_KEY="..."` command in the terminal is temporary and only works for that specific session. When running the program from an IDE or a new terminal, that variable is "forgotten".
* **Solution:** We adopted a more robust and professional solution:
    1.  Added the `dotenv-java` dependency to `pom.xml`.
    2.  Created a `.env` file to securely store the API Key.
    3.  Added the `.env` file to `.gitignore` to keep the secret key out of the repository.
    4.  Modified `Main.java` to use the new library and read the key from the `.env` file.

### Error 3: `.env` File Not Found (`DotenvException`)
* **Situation:** After configuring the `dotenv-java` library, the program still couldn't find the key.
* **Error:**
    ```text
    io.github.cdimascio.dotenv.DotenvException: Could not find ./.env on the file system
    ```
* **Diagnosis:** The "working directory" changed depending on how the program was executed (IDE vs. Maven command line), creating confusion about where the `.env` file should be.
* **Solution:** We implemented a "foolproof" solution: placed a copy of the `.env` file in **two locations**: one in the project root (`UniversityResearcherIntegration/`) and another inside the module folder (`app/`). This way, the program would always find it.

### Error 4: Maven Failure (`No POM in this directory`)
* **Situation:** This error appeared several times when trying to run the program from the terminal.
* **Error:**
    ```text
    [ERROR] Failed to execute goal ... there is no POM in this directory
    ```
* **Diagnosis:** The `mvn` command was being executed in the wrong folder. Maven needs to be in the same folder as the `pom.xml` file to work, which in our case was the `app/` folder.
* **Solution:** We established the "Golden Rule": Always navigate into the correct folder with `cd app` **before** executing any `mvn` command.

### Error 5: API Returning Partial Data
* **Situation:** The program finally ran without configuration errors but the result was `No se pudo obtener la información del autor`.
* **Logical Error:** The JSON response only contained metadata, not the actual author data.
* **Diagnosis:** The `author_id` of the first researcher we tested was returning an incomplete JSON payload for an unknown reason.
* **Solution:** As a diagnostic step, we changed the `author_id` in `Main.java` to that of another researcher (Yoshua Bengio), which allowed us to receive a complete JSON response.

### Error 6: JSON Parsing Error (`JsonSyntaxException` in `interests`)
* **Situation:** Using the new `author_id`, the program received data but crashed when trying to read it.
* **Error:**
    ```text
    JsonSyntaxException: ... Expected a STRING but was BEGIN_OBJECT at path $.author.interests[0]
    ```
* **Diagnosis:** Our `Author.java` class expected a list of simple strings (`List<String>`) for the `interests` field, but the API sent a list of more complex objects.
* **Solution:**
    1.  Created a new `Interest.java` class to match the object structure.
    2.  Modified `Author.java` to use `List<Interest>`.
    3.  Adjusted `AuthorView.java` to correctly read and display the titles from the new list of objects.

### Error 7: Publication List Not Appearing
* **Situation:** After fixing the `interests` issue, the program showed author data but still no publications.
* **Logical Error:** The article list wasn't being parsed from the JSON.
* **Diagnosis:** After analyzing the full JSON, we discovered two things:
    1.  The `articles` list was a "sibling" to the `author` object, not nested inside it.
    2.  The publication field was named `publication`, not `publication_info`.
* **Solution:**
    1.  Moved the `List<Article> articles` field from `Author.java` to `ApiResponse.java`.
    2.  Renamed the field in `Article.java` from `publication_info` to `publication`.
    3.  Adjusted `AuthorView.java` and `AuthorController.java` to handle this correct structure.

### Error 8: "Ghost" Compilation Error (`Unresolved compilation problem`)
* **Situation:** After restructuring the Model classes, the program refused to compile despite the code being syntactically correct.
* **Error:**
    ```text
    java.lang.Error: Unresolved compilation problem: ... missing type Article
    ```
* **Diagnosis:** Maven or the IDE were using "stale" or corrupted versions of the previously compiled files (`.class`), causing type confusion.
* **Solution:** We forced a complete and clean rebuild of the project using the command `mvn clean install`. This deleted all old compiled files and rebuilt the project from scratch, resolving the "ghost" error.

</details>

---

## How to Execute?

### Prerequisites
* Java JDK 17 or higher
* Maven 3.6 or higher
* A SerpApi account and your private API key

### Installation and Execution
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/LuisDavid0912/UniversityResearcherIntegration.git](https://github.com/LuisDavid0912/UniversityResearcherIntegration.git)
    cd UniversityResearcherIntegration
    ```
2.  **Create the `.env` file:**
    Create a file named `.env` in the root of the `UniversityResearcherIntegration/` directory and also a copy inside the `app/` directory. Add your SerpApi key to both files:
    ```bash
    SERPAPI_KEY="your_private_api_key_here"
    ```
3.  **Navigate to the app directory:**
    ```bash
    cd app
    ```
4.  **Build the project:**
    This command cleans previous builds and compiles the entire project.
    ```bash
    mvn clean install
    ```
5.  **Run the application:**
    This command executes the main class.
    ```bash
    mvn exec:java -Dexec.mainClass="com.university.project.Main"
    ```

---

<p align="center"><i>This project is being developed as part of the Digital NAO "Server and Database Commands" challenge.</i></p>
