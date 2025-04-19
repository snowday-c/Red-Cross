package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Exam;
import com.example.redcross.entity.ExamType;
import com.example.redcross.entity.Question;
import com.example.redcross.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/insertExamType")  //新增试卷类型
    public Result insertExamType(@RequestBody ExamType examtype) {
        Integer choice= examtype.getChoice();
        Integer truefalse= examtype.getTruefalse();
        Integer blank= examtype.getBlank();
        Integer score= examtype.getScore();
        Integer time= examtype.getTime();
        if (questionService.insertExamType(choice,truefalse,blank,score,time) == 1) {
            return Result.success();
        }
        return Result.error("新增试卷类型失败");
    }

    @GetMapping("/selectAllExam")  //显示全部试卷类型
    public Result selectAllExam() {
        List<ExamType> examTypes= questionService.selectAllExam();
        if (examTypes != null) {
            return Result.success(examTypes);
        }
        return Result.error("查询全部试卷类型失败");
    }

    @GetMapping("/selectCurrentExam")  //查询当前试卷类型
    public Result selectCurrentExam() {
        ExamType examType= questionService.selectCurrentExam();
        if (examType != null) {
            return Result.success(examType);
        }
        return Result.error("获取当前试卷类型失败");
    }

    @PostMapping("/selectExam")  //修改当前试卷类型
    public Result selectExam(@RequestBody ExamType exam) {
        Integer examTypeId = exam.getExamtypeId();
        if (questionService.selectExam(examTypeId) >= 1) {
            return Result.success();
        }
        return Result.error("修改试卷类型失败");
    }

    @PostMapping("/exam")  //生成试卷
    public Result getExam(@RequestBody Exam exam) {
        Integer userId = exam.getUserId();
        //获取当前考试每种题型的题目数量
        ExamType examtype = questionService.getExamType();

        Integer choice= examtype.getChoice();
        Integer truefalse= examtype.getTruefalse();
        Integer blank= examtype.getBlank();


        // 从题库中获取试题
        List<Question> questions = questionService.getExam(choice,truefalse,blank);

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

        ExamType examtype = questionService.getExamType();

        Integer score1= examtype.getScore();
        // 对比答案并计算得分
        int score = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            String userAnswer = userAnswers.get(i);
            String correctAnswer = correctAnswers.get(i);

            if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                score += score1;
            }
        }

        // 更新试卷表中的得分
        latestExam.setScore(score);
        questionService.updateExamScore(latestExam.getExamId(), latestExam.getScore());

        // 返回得分给前端

        return Result.success(score);
    }

    @PostMapping("/getUserExam") // 查询用户的全部试卷
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

            // 解析用户答案
            List<String> userAnswers = null;
            if (examItem.getAnswers() != null) {
                try {
                    userAnswers = objectMapper.readValue(examItem.getAnswers(), new TypeReference<List<String>>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("解析用户答案失败", e);
                }
            }

            // 将 questions、correctAnswers 和 answers 组合成一个完整的题目对象列表
            List<Map<String, Object>> questionDetails = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                Map<String, Object> questionDetail = new HashMap<>();
                questionDetail.put("question", questions.get(i).getQuestion()); // 题目内容
                questionDetail.put("correctAnswer", questions.get(i).getAnswer()); // 正确答案
                questionDetail.put("userAnswer", userAnswers != null ? userAnswers.get(i) : null); // 用户答案
                questionDetails.add(questionDetail);
            }

            // 将组合后的题目对象列表存入 examItem
            examItem.setQuestions(questionDetails);
        }

        return Result.success(exams);
    }

    @GetMapping("/getAllGrade") // 查询全部成绩
    public Result getAllGrade() {
        List<Map<String, Object>> grades = questionService.getAllGrade();
        return Result.success(grades);
    }
    
    @GetMapping("/getLastGrade") // 查询最近50次考试的成绩
    public Result getLastGrade() {
        List<Map<String, Object>> grades = questionService.getLastGrade();
        return Result.success(grades);
    }

}