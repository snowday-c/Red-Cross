package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Grade {

    @TableId(value = "grade_id", type = IdType.AUTO)
    private Integer gradeId;

    private Integer userId;

    private String grade;

    private String gradeTime;

}
