package com.example.redcross.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // 忽略值为 null 的字段
@TableName(value = "examtype")
public class ExamType {
    @TableId(value = "examtype_id", type = IdType.AUTO)
    private Integer examtypeId;

    private Integer choice;

    private Integer truefalse;

    private Integer blank;

    private Integer score;

    private Integer time;

    private Integer current;
}
