✅1. **Initialization**: The app should read the CSV file with the countries and continents and store it in a suitable SQLite database. The main UI thread should not be adversely affected by these I/O operations, so you must perform this operation within an AsyncTask. Initially, when the app is deployed, the SQLite database does not exist, and the app should create a suitable SQLite database to store the countries and continents, quizzes, and their results. On the second and subsequent executions, your app should check if the SQLite database already exists, and if so, use it instead of initializing it using the CSV file.

2. **Splash Screen**: The initial (splash) screen should display the purpose of the quiz and explain the basic rules (you may also offer a help screen). From the initial screen, the user should be able to start a new quiz or view the results of the past quizzes.

✅3. **Quiz Initialization**: When the user wants to start a new quiz, the app should randomly select 6 countries from the database as the new quiz questions (use `java.util.Random` and its `nextInt(int bound)` method). Be careful about selecting the countries randomly, as the chosen countries should not have any duplicates. The app should also populate a suitable Java data structure to store the countries in the quiz, the current score, and how many questions have been answered so far. 

4. **Quiz Questions**: The app should quiz the user, asking about the continent of each of the selected countries. For each country, the question should include 3 possible choices, including the correct continent and 2 additional ones (selected randomly, without repeats). The possible answers should be shown in a random order, and the choices should be labeled 1 through 3 (or A through C). The quiz question should be clearly stated (e.g., “Name the continent on which XXX is located”), and the answer choices should be shown below as radio buttons.

5. **Swipe Navigation**: After selecting an answer to a question, the user should be able to swipe left (right-to-left) to go to the next question. Also, when the user swipes left, the system should record the user’s answer to the currently shown question. You may offer an additional way to move to the next question, but swiping left to get to the next question must be possible.

6. **Quiz Results**: Once the user answers all the questions in a quiz, the app should display the result of the quiz. This should be right after the final swipe, following the final question. The app should store the date of the quiz and the result in the SQLite database. Again, this should be done using an AsyncTask.

7. **Viewing Results**: From the splash screen, or from the results screen of the current quiz, the user should be able to view their prior quizzes and results or start another quiz. The results should include the date and the score of each prior quiz (not the individual answers). Database retrieval should be done within an AsyncTask, as well.

8. **Navigation Drawer (Optional)**: You may incorporate the navigation drawer in your app if you would like.

9. **State Saving & Restoring**: Implement state saving and restoring in your app. Specifically, the app should store quiz progress and update the quiz score and how many questions have been answered after each answer is given. The user should be able to continue a quiz if it was interrupted by switching to a different app or a phone call. However, you do not have to implement resuming of a quiz after a device restart.

10. **Discard Partial Quiz**: When the app is terminated, any partially completed quiz is simply discarded (not stored in the database).

11. **Fresh Quiz Start**: On a fresh start of the app, even if the database already exists, if the user elects to run a quiz, the app should start a new quiz, and any partially finished quizzes should be discarded. Similarly, when viewing the past quizzes, any partial quizzes should not appear in the list.

## Additional Requirements for CSCI 6060/4060 Honors Teams:

12. **App Termination**: The user should be able to terminate the app, but not necessarily in the middle of a quiz.

13. **Advanced Quiz**: Your app must implement an advanced version of the quiz, where, in addition to the country’s continent, the user will be asked to name at least one neighboring country.

14. **Answer Choices**: Here, the user should have 4 choices in random ordering, with a correct answer being one of them.

15. **No Neighbors Option**: Since there exist countries with no neighbors, one of the choices should always include “No neighbors.” For a country with no real neighbors, “No neighbors” would be a correct answer.

16. **Combined Questions**: Both questions (continent and neighbors) should be placed on the same screen in the quiz, if possible.

17. **Neighboring Countries File**: Countries and their neighbors are listed in the file `country_neighbors.csv`, which is available in the Projects folder on eLC. Note that the format of this file is somewhat different from the continents file, as the number_
