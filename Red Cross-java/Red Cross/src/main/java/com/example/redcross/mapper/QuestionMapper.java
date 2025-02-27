package com.example.redcross.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.redcross.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    List<Question> getAllQuestions();

    List<Question> getQuestionsByType(Integer questionType);

    void insertQuestion(Question question);

    void deleteQuestionById(Integer questionId);

    void updateQuestion(Question question);


}