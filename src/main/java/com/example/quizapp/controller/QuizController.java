package com.example.quizapp.controller;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.UserSession;
import com.example.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // 1. Start a new quiz session
    @PostMapping("/start")
    public ResponseEntity<UserSession> startSession(@RequestParam Long userId) {
        UserSession session = quizService.startNewSession(userId);
        return ResponseEntity.ok(session);
    }

    // 2. Get a random question
    @GetMapping("/question")
    public ResponseEntity<Question> getRandomQuestion() {
        Question question = quizService.getRandomQuestion();
        return ResponseEntity.ok(question);
    }

    // 3. Submit an answer
    @PostMapping("/submit")
    public ResponseEntity<UserSession> submitAnswer(
            @RequestParam Long userId,
            @RequestParam Long questionId,
            @RequestParam String chosenOption) {
        UserSession session = quizService.submitAnswer(userId, questionId, chosenOption);
        return ResponseEntity.ok(session);
    }

    // 4. Get user session details
    @GetMapping("/session")
    public ResponseEntity<UserSession> getSessionDetails(@RequestParam Long userId) {
        UserSession session = quizService.getSessionDetails(userId);
        return ResponseEntity.ok(session);
    }
}
