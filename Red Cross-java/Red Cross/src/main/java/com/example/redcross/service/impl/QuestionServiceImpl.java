package com.example.redcross.service.impl;

import com.example.redcross.entity.Exam;
import com.example.redcross.entity.Question;
import com.example.redcross.mapper.QuestionMapper;
import com.example.redcross.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> getAllQuestions() {
        return questionMapper.getAllQuestions();
    }

    @Override
    public List<Question> getQuestionsByType(Integer questionType){
        return questionMapper.getQuestionsByType(questionType);
    }

    @Override
    public Question addQuestion(Question question) {
        questionMapper.insertQuestion(question);
        return question;
    }

    @Override
    public void deleteQuestion(Integer questionId) {
        questionMapper.deleteQuestionById(questionId);
    }

    @Override
    public Question updateQuestion(Question question) {
        questionMapper.updateQuestion(question);
        return question;
    }

    @Override
    public List<Question> getExam() {
        return questionMapper.getExam();
    }

    @Override
    public void insertExam(String questionIds, Integer userId) {
        questionMapper.insertExam(questionIds, userId);
    }

    @Override
    public Exam findLatestExamByUserId(Integer userId) {
        return questionMapper.findLatestExamByUserId(userId);
    }

    @Override
    public List<Question> getQuestionsByIds(List<Integer> questionIds) {
        return questionMapper.getQuestionsByIds(questionIds);
    }

    @Override
    public void insertUserAnswer(String userAnswersJson, Integer examId) {
        questionMapper.insertUserAnswer(userAnswersJson, examId);
    }

    @Override
    public void updateExamScore(Integer examId, Integer score) {
        questionMapper.updateExamScore(examId, score);
    }
}