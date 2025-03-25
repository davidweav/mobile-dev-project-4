# Automatic Zoom
## Programming Project 4: Country Quiz
**Due:** April 3, 2025

In this project, you and your partner (you will work in pairs) will create a simple quiz application, called the **Country Quiz**, testing the user’s knowledge of countries and continents. As in the previous project, you will work with fragments to make your app more flexible. Additionally, you will work with saving and restoring the state of your app (a quiz). You will also learn how to work with Comma-Separated Values (CSV) files, asynchronous tasks, and an SQLite database. Finally, you will further develop your skills in developing apps and utilizing new widgets, as needed.

A CSV file with countries and their continents, `country_continent.csv`, is available on eLC. You can use Microsoft Excel, LibreOffice Calc, or even a text editor to view CSV files (they are text files).

Each team must have 2 students, both of whom are registered in CSCI 4060, or both in CSCI 6060 or 4060 Honors. The CSCI 6060 and Honors teams will have extra requirements, described below.

## Requirements for CSCI 4060 teams:

1. **Initialization**: The app should read the CSV file with the countries and continents and store it in a suitable SQLite database. The main UI thread should not be adversely affected by these I/O operations, so you must perform this operation within an AsyncTask. Initially, when the app is deployed, the SQLite database does not exist, and the app should create a suitable SQLite database to store the countries and continents, quizzes, and their results. On the second and subsequent executions, your app should check if the SQLite database already exists, and if so, use it instead of initializing it using the CSV file.

2. **Splash Screen**: The initial (splash) screen should display the purpose of the quiz and explain the basic rules (you may also offer a help screen). From the initial screen, the user should be able to start a new quiz or view the results of the past quizzes.

3. **Quiz Initialization**: When the user wants to start a new quiz, the app should randomly select 6 countries from the database as the new quiz questions (use `java.util.Random` and its `nextInt(int bound)` method). Be careful about selecting the countries randomly, as the chosen countries should not have any duplicates. The app should also populate a suitable Java data structure to store the countries in the quiz, the current score, and how many questions have been answered so far. 

4. **Quiz Questions**: The app should quiz the user, asking about the continent of each of the selected countries. For each country, the question should include 3 possible choices, including the correct continent and 2 additional ones (selected randomly, without repeats). The possible answers should be shown in a random order, and the choices should be labeled 1 through 3 (or A through C). The quiz question should be clearly stated (e.g., “Name the continent on which XXX is located”), and the answer choices should be shown below as radio buttons.

5. **Swipe Navigation**: After selecting an answer to a question, the user should be able to swipe left (right-to-left) to go to the next question. Also, when the user swipes left, the system should record the user’s answer to the currently shown question. You may offer an additional way to move to the next question, but swiping left to get to the next question must be possible.

6. **Quiz Results**: Once the user answers all the questions in a quiz, the app should display the result of the quiz. This should be right after the final swipe, following the final question. The app should store the date of the quiz and the result in the SQLite database. Again, this should be done using an AsyncTask.

7. **Viewing Results**: From the splash screen, or from the results screen of the current quiz, the user should be able to view their prior quizzes and results or start another quiz. The results should include the date and the score of each prior quiz (not the individual answers). Database retrieval should be done within an AsyncTask, as well.

8. **Navigation Drawer (Optional)**: You may incorporate the navigation drawer in your app if you would like.

9. **State Saving & Restoring**: Implement state saving and restoring in your app. Specifically, the app should store quiz progress and update the quiz score and how many questions have been answered after each answer is given. The user should be able to continue a quiz if it was interrupted by switching to a different app or a phone call. However, you do not have to implement resuming of a quiz after a device restart.

10. **Discard Partial Quiz**: When the
