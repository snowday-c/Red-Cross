package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "advice")
public class Advice {

    @TableId(value = "advice_id", type = IdType.AUTO)
    private Integer adviceId;

    private String adviceContent;

    private String adviceSender;

    private String adviceTime;

    private String adviceType;

    private String adviceHandler;


}
