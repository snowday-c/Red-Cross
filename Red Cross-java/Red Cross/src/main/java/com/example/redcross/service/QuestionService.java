package com.example.redcross.service;

import com.example.redcross.entity.Exam;
import com.example.redcross.entity.ExamType;
import com.example.redcross.entity.Question;

import java.util.List;

public interface QuestionService {

    ExamType getExamType() ;

    List<Question> getAllQuestions();

    List<Question> getQuestionsByType(Integer questionType);

    Question addQuestion(Question question);

    void deleteQuestion(Integer questionId);

    Question updateQuestion(Question question);

    List<Question> getExam(Integer choice, Integer truefalse, Integer blank);

    void insertExam(String questionIds,Integer userId);

    Exam findLatestExamByUserId(Integer userId);

    List<Question> getQuestionsByIds(List<Integer> questionIds);

    void updateExamScore(Integer examId, Integer score);

    void insertUserAnswer(String userAnswersJson, Integer examId);


    List<Exam> findAllExamByUserId(Integer userId);

    Integer selectExam(Integer examTypeId);

    Integer insertExamType(Integer choice, Integer truefalse, Integer blank, Integer score,Integer time);

    List<ExamType> selectAllExam();

    ExamType selectCurrentExam();
}