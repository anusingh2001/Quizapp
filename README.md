# Quiz App

## Overview
The Quiz App is a Spring Boot-based REST API that allows users to:
1. Start a new quiz session.
2. Fetch a random multiple-choice question from a database.
3. Submit answers to questions.
4. Retrieve session statistics, including the total number of questions answered and details on correct/incorrect submissions.

## Features
- Manage user quiz sessions.
- Fetch and store multiple-choice questions in a database.
- Submit and validate answers.
- Track user performance during a session.

## Tech Stack
- **Backend Framework**: Spring Boot
- **Database**: H2 (in-memory database for testing) or any SQL-based database
- **Testing**: JUnit, Spring Boot Test
- **Build Tool**: Maven

---

## Project Structure

```
quiz-app
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── quizapp
│   │   │               ├── controller
│   │   │               │   └── QuizController.java      # REST Controller for APIs
│   │   │               ├── model
│   │   │               │   ├── Question.java           # Entity for storing questions
│   │   │               │   └── UserSession.java        # Entity for tracking user sessions
│   │   │               ├── repository
│   │   │               │   ├── QuestionRepository.java # Repository for Question entity
│   │   │               │   └── UserSessionRepository.java # Repository for UserSession entity
│   │   │               ├── service
│   │   │               │   └── QuizService.java        # Service containing business logic
│   │   │               └── QuizAppApplication.java     # Main Spring Boot application class
│   │   └── resources
│   │       ├── application.properties                  # Spring Boot configuration
│   │       ├── data.sql                                # Optional: Preload questions into the database
│   │       └── schema.sql                              # Optional: Define database schema
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── quizapp
│                       └── QuizAppApplicationTests.java # Unit and integration tests
├── pom.xml                                              # Maven dependencies
└── README.md                                            # Project documentation
```

---

## How to Run the Project

### Prerequisites
- JDK 17 or later
- Maven 3.8+
- An IDE (e.g., IntelliJ IDEA, Eclipse) or a terminal

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/quiz-app.git
   cd quiz-app
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application will be accessible at:
   ```
   http://localhost:8080
   ```

---

## API Endpoints

### 1. Start a New Quiz Session
**Endpoint:**
```
POST /api/quiz/start?userId={userId}
```
**Description:** Starts a new quiz session for the specified user.
**Response Example:**
```json
{
  "userId": 1,
  "totalQuestionsAnswered": 0,
  "correctAnswers": 0,
  "incorrectAnswers": 0
}
```

### 2. Fetch a Random Question
**Endpoint:**
```
GET /api/quiz/question
```
**Description:** Retrieves a random multiple-choice question.
**Response Example:**
```json
{
  "id": 1,
  "questionText": "What is the capital of France?",
  "optionA": "Paris",
  "optionB": "London",
  "optionC": "Rome",
  "optionD": "Berlin"
}
```

### 3. Submit an Answer
**Endpoint:**
```
POST /api/quiz/submit?userId={userId}&questionId={questionId}&chosenOption={chosenOption}
```
**Description:** Submits the user's answer for a question.
**Response Example:**
```json
{
  "userId": 1,
  "totalQuestionsAnswered": 1,
  "correctAnswers": 1,
  "incorrectAnswers": 0
}
```

### 4. Get Session Details
**Endpoint:**
```
GET /api/quiz/session?userId={userId}
```
**Description:** Fetches the user's session statistics.
**Response Example:**
```json
{
  "userId": 1,
  "totalQuestionsAnswered": 3,
  "correctAnswers": 2,
  "incorrectAnswers": 1
}
```

---

## Testing

### Run Unit and Integration Tests
To execute all tests, run:
```bash
mvn test
```

### Test Coverage
Tests are included for:
- Service layer logic
- API endpoints

### Example Test File
`QuizAppApplicationTests.java` contains unit and integration tests to verify the correctness of the application.

---

## Configuration

### Database
By default, the project uses an in-memory H2 database. You can configure another database (e.g., MySQL, PostgreSQL) by updating `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quizdb
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

---

## Future Enhancements
- Add support for timed quizzes.
- Implement user authentication and authorization.
- Store quiz history for users.
- Enhance question pool with different categories and difficulty levels.

---

## Contributing
Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request.

---

## License
This project is licensed under the MIT License.

