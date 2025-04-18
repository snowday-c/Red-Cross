package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@TableName("exam")
public class Exam {
    @TableId(value = "exam_id", type = IdType.AUTO)
    private Integer examId;

    private Integer userId;  //用户id

    private String questionIds;  //题目id列表

    private String answers;  //答案列表

    private Integer score;  //得分

    private String examTime;  //考试时间

    @TableField(exist = false)
    private List<Map<String, Object>> questions;

    @TableField(exist = false)
    private List<String> correctAnswers;

}
