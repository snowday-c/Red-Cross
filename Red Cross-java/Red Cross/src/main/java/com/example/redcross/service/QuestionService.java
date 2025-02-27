package com.example.redcross.service;

import com.example.redcross.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions();

    List<Question> getQuestionsByType(Integer questionType);

    Question addQuestion(Question question);

    void deleteQuestion(Integer questionId);

    Question updateQuestion(Question question);

}