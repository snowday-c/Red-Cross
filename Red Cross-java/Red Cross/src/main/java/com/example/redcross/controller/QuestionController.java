package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Exam;
import com.example.redcross.entity.Question;
import com.example.redcross.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/all") // 查询所有题目
    public Result getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return Result.success(questions);
    }

    @PostMapping("/type") // 查询一种题型的题目
    public Result getQuestionsByType(@RequestBody Question question) {
        Integer questionType = question.getQuestionType();
        List<Question> questions = questionService.getQuestionsByType(questionType);
        return Result.success(questions);
    }

    @PostMapping("/add") // 添加题目
    public Result addQuestion(@RequestBody Question question) {
        Question addedQuestion = questionService.addQuestion(question);
        return Result.success(addedQuestion);
    }

    @PostMapping("/delete") // 删除题目
    public Result deleteQuestion(@RequestBody Question question) {
        Integer questionId = question.getQuestionId();
        questionService.deleteQuestion(questionId);
        return Result.success();
    }

    @PostMapping("/update") // 更新题目
    public Result updateQuestion(@RequestBody Question question) {
        Question updatedQuestion = questionService.updateQuestion(question);
        return Result.success(updatedQuestion);
    }

    @PostMapping("/exam")  //生成试卷
    public Result getExam(@RequestBody Exam exam) {
        Integer userId = exam.getUserId();
        // 从题库中获取试题
        List<Question> questions = questionService.getExam();

        // 提取试题的ID列表
        List<Integer> questionIds = questions.stream()
                .map(Question::getQuestionId)
                .collect(Collectors.toList());

        // 将题目ID列表转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String questionIdsJson;
        try {
            questionIdsJson = objectMapper.writeValueAsString(questionIds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert question IDs to JSON", e);
        }

        // 将JSON字符串插入试卷表
        questionService.insertExam(questionIdsJson, userId);

        // 返回试题列表给前端
        return Result.success(questions);
    }

    @PostMapping("/submit") // 提交试卷
    public Result submitExam(@RequestBody Exam exam) {
        Integer userId = exam.getUserId(); // 获取用户id
        String userAnswersJson = exam.getAnswers(); // 获取用户提交的答案（JSON格式）

        // 根据用户id寻找试卷表中最大试卷id（即最新提交的试卷）
        Exam latestExam = questionService.findLatestExamByUserId(userId);
        if (latestExam == null) {
            throw new RuntimeException("未找到用户的试卷");
        }
        //将用户提交的答案插入试卷表
        questionService.insertUserAnswer(userAnswersJson, latestExam.getExamId());
        // 获取题目ID列表
        String questionIdsJson = latestExam.getQuestionIds();

        // 将 JSON 字符串转换为题目ID列表（保留原始顺序）
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> questionIds;
        try {
            questionIds = objectMapper.readValue(questionIdsJson, new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析题目ID列表失败", e);
        }

        // 根据题目ID列表从题库中获取每道题的正确答案
        List<Question> questions = questionService.getQuestionsByIds(questionIds);
        List<String> correctAnswers = questions.stream()
                .map(Question::getAnswer)
                .collect(Collectors.toList());
        // 获取用户提交的答案
        List<String> userAnswers;
        try {
            userAnswers = objectMapper.readValue(userAnswersJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析用户答案失败", e);
        }

        // 对比答案并计算得分
        int score = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            String userAnswer = userAnswers.get(i);
            String correctAnswer = correctAnswers.get(i);

            if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                score += 20; // 每道题20分
            }
        }

        // 更新试卷表中的得分
        latestExam.setScore(score);
        questionService.updateExamScore(latestExam.getExamId(), latestExam.getScore());

        // 返回得分给前端

        return Result.success(score);
    }

    @PostMapping("/getUserExam") // 查询用户的试卷
    public Result getUserExam(@RequestBody Exam exam) {
        Integer userId = exam.getUserId(); // 获取用户id
        List<Exam> exams = questionService.findAllExamByUserId(userId);

        // 遍历每个试卷
        for (Exam examItem : exams) {
            // 获取题目ID列表
            String questionIdsJson = examItem.getQuestionIds();

            // 将 JSON 字符串转换为题目ID列表（保留原始顺序）
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> questionIds;
            try {
                questionIds = objectMapper.readValue(questionIdsJson, new TypeReference<List<Integer>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException("解析题目ID列表失败", e);
            }

            // 根据题目ID列表从题库中获取每道题的详细信息
            List<Question> questions = questionService.getQuestionsByIds(questionIds);

            // 提取问题列表（只包含 question 字段）
            List<String> questionList = questions.stream()
                    .map(Question::getQuestion)
                    .collect(Collectors.toList());

            // 将问题列表存入 examItem
            examItem.setQuestions(questionList);

            // 提取正确答案并存入 examItem
            List<String> correctAnswers = questions.stream()
                    .map(Question::getAnswer)
                    .collect(Collectors.toList());
            examItem.setCorrectAnswers(correctAnswers);
        }

        return Result.success(exams);
    }
}