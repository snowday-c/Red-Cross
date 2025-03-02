package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "retrain")
public class Retrain {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String trainTime;

    private String trainNext;

}
