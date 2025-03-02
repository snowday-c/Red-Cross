package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "train")
public class Train {

    @TableId(value = "train_id", type = IdType.AUTO)
    private Integer trainId;

    private String trainPlace;

    private String trainTime;

    private Integer trainPeople;

    private Integer trainType;

}
