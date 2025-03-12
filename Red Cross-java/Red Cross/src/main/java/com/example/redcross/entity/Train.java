package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // 忽略值为 null 的字段
@TableName(value = "train")
public class Train {

    @TableId(value = "train_id", type = IdType.AUTO)
    private Integer trainId;

    private String trainPlace;

    private String trainTime;

    private Integer trainPeople;

    private Integer trainType;

    private Integer currentPeople;

    private String userIds;

    //非数据库字段
    @TableField(exist = false)
    private Integer userId;
    @TableField(exist = false)
    private List<String> trainPlaces;

}
