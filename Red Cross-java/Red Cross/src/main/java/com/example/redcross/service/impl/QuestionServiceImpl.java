package com.example.redcross.service.impl;

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


}