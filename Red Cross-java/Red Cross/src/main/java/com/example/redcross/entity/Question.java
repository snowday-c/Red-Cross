package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("question")
public class Question {

    @TableId(value = "question_id", type = IdType.AUTO)
    private Integer questionId;

    private Integer questionType;   // 1:选择 2:判断 3:填空

    private String question;

    private String answer;

}
