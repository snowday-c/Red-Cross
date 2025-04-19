package com.example.redcross.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.redcross.entity.Exam;
import com.example.redcross.entity.ExamType;
import com.example.redcross.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    List<Question> getAllQuestions();

    List<Question> getQuestionsByType(Integer questionType);

    void insertQuestion(Question question);

    void deleteQuestionById(Integer questionId);

    void updateQuestion(Question question);


    List<Question> getExam(Integer choice, Integer truefalse, Integer blank);

    void insertExam(String questionIds,Integer userId);

    Exam findLatestExamByUserId(Integer userId);

    List<Question> getQuestionsByIds(List<Integer> questionIds);

    void updateExamScore(Integer examId, Integer score);

    void insertUserAnswer(String userAnswersJson, Integer examId);

    List<Exam> findAllExamByUserId(Integer userId);

    ExamType getExamType();

    Integer selectExam(Integer examTypeId);

    Integer insertExamType(Integer choice, Integer truefalse, Integer blank, Integer score, Integer time);

    List<ExamType> selectAllExam();

    /**
     * 获取所有考试成绩
     * @return 所有考试成绩列表
     */
    List<Map<String, Object>> getAllGrade();

    /**
     * 获取最近50次考试成绩
     * @return 最近50次考试成绩列表
     */
    List<Map<String, Object>> getLastGrade();
}