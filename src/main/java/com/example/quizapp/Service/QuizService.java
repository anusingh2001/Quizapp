package com.example.quizapp.service;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.UserSession;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    private final Random random = new Random();

    public UserSession startNewSession(Long userId) {
        // If session exists, return it; else create a new one
        Optional<UserSession> existingSession = userSessionRepository.findByUserId(userId);
        return existingSession.orElseGet(() -> {
            UserSession session = new UserSession();
            session.setUserId(userId);
            session.setTotalQuestionsAnswered(0);
            session.setCorrectAnswers(0);
            session.setIncorrectAnswers(0);
            return userSessionRepository.save(session);
        });
    }

    public Question getRandomQuestion() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions available in the database.");
        }
        return questions.get(random.nextInt(questions.size()));
    }

    public UserSession submitAnswer(Long userId, Long questionId, String chosenOption) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        UserSession session = userSessionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User session not found"));

        session.setTotalQuestionsAnswered(session.getTotalQuestionsAnswered() + 1);

        if (question.getCorrectOption().equalsIgnoreCase(chosenOption)) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        } else {
            session.setIncorrectAnswers(session.getIncorrectAnswers() + 1);
        }

        return userSessionRepository.save(session);
    }

    public UserSession getSessionDetails(Long userId) {
        return userSessionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User session not found"));
    }
}
