package com.example.quizapp;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.UserSession;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.UserSessionRepository;
import com.example.quizapp.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuizAppApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // Add some sample questions to the database for testing
        Question question1 = new Question();
        question1.setQuestionText("What is the capital of France?");
        question1.setOptionA("Paris");
        question1.setOptionB("London");
        question1.setOptionC("Rome");
        question1.setOptionD("Berlin");
        question1.setCorrectOption("A");

        Question question2 = new Question();
        question2.setQuestionText("What is 2 + 2?");
        question2.setOptionA("3");
        question2.setOptionB("4");
        question2.setOptionC("5");
        question2.setOptionD("6");
        question2.setCorrectOption("B");

        questionRepository.save(question1);
        questionRepository.save(question2);
    }

    @Test
    void testStartNewSession() {
        // Create a new session
        UserSession session = quizService.startNewSession(1L);

        // Verify the session is created with initial values
        assertThat(session).isNotNull();
        assertThat(session.getUserId()).isEqualTo(1L);
        assertThat(session.getTotalQuestionsAnswered()).isEqualTo(0);
        assertThat(session.getCorrectAnswers()).isEqualTo(0);
        assertThat(session.getIncorrectAnswers()).isEqualTo(0);
    }

    @Test
    void testGetRandomQuestion() {
        // Fetch a random question
        Question question = quizService.getRandomQuestion();

        // Verify the question is fetched
        assertThat(question).isNotNull();
        assertThat(question.getQuestionText()).isNotEmpty();
    }

    @Test
    void testSubmitAnswer() {
        // Start a new session for the user
        UserSession session = quizService.startNewSession(2L);

        // Submit an answer for a question
        Optional<Question> question = questionRepository.findAll().stream().findFirst();
        assertThat(question).isPresent();

        quizService.submitAnswer(2L, question.get().getId(), "A");

        // Fetch the updated session and verify the answer submission
        UserSession updatedSession = userSessionRepository.findByUserId(2L).orElseThrow();
        assertThat(updatedSession.getTotalQuestionsAnswered()).isEqualTo(1);

        // Depending on the answer submitted, correct/incorrect count should be updated
        if (question.get().getCorrectOption().equalsIgnoreCase("A")) {
            assertThat(updatedSession.getCorrectAnswers()).isEqualTo(1);
        } else {
            assertThat(updatedSession.getIncorrectAnswers()).isEqualTo(1);
        }
    }

    @Test
    void testSessionDetails() {
        // Start a new session and submit a few answers
        UserSession session = quizService.startNewSession(3L);
        quizService.submitAnswer(3L, 1L, "A");
        quizService.submitAnswer(3L, 2L, "B");

        // Fetch session details
        UserSession sessionDetails = quizService.getSessionDetails(3L);

        // Verify session details
        assertThat(sessionDetails).isNotNull();
        assertThat(sessionDetails.getTotalQuestionsAnswered()).isEqualTo(2);
        assertThat(sessionDetails.getCorrectAnswers() + sessionDetails.getIncorrectAnswers()).isEqualTo(2);
    }

    @Test
    void integrationTestStartSessionEndpoint() {
        // Test the /start endpoint
        String url = "http://localhost:" + port + "/api/quiz/start?userId=5";
        ResponseEntity<UserSession> response = restTemplate.postForEntity(url, null, UserSession.class);

        // Verify the response
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(5L);
    }

    @Test
    void integrationTestGetRandomQuestionEndpoint() {
        // Test the /question endpoint
        String url = "http://localhost:" + port + "/api/quiz/question";
        ResponseEntity<Question> response = restTemplate.getForEntity(url, Question.class);

        // Verify the response
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getQuestionText()).isNotEmpty();
    }

    @Test
    void integrationTestSubmitAnswerEndpoint() {
        // Test the /submit endpoint
        String urlStart = "http://localhost:" + port + "/api/quiz/start?userId=6";
        restTemplate.postForEntity(urlStart, null, UserSession.class);

        String urlSubmit = "http://localhost:" + port + "/api/quiz/submit?userId=6&questionId=1&chosenOption=A";
        ResponseEntity<UserSession> response = restTemplate.postForEntity(urlSubmit, null, UserSession.class);

        // Verify the response
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalQuestionsAnswered()).isGreaterThan(0);
    }
}
