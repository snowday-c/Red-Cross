package com.example.redcross.service;

import com.example.redcross.entity.Exam;
import com.example.redcross.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions();

    List<Question> getQuestionsByType(Integer questionType);

    Question addQuestion(Question question);

    void deleteQuestion(Integer questionId);

    Question updateQuestion(Question question);

    List<Question> getExam();

    void insertExam(String questionIds,Integer userId);

    Exam findLatestExamByUserId(Integer userId);

    List<Question> getQuestionsByIds(List<Integer> questionIds);

    void updateExamScore(Integer examId, Integer score);

    void insertUserAnswer(String userAnswersJson, Integer examId);
}