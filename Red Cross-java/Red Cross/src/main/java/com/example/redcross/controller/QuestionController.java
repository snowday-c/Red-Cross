package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Question;
import com.example.redcross.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}