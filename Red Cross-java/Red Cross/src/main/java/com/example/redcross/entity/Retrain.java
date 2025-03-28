package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "retrain")
public class Retrain {

    @TableId(value = "retrain_id")
    private Integer retrainId;

    private Integer trainId;

    private String userId;

    private String trainPlace;

    private String trainTime;

    private String trainNext;

    private String participateType;


}
