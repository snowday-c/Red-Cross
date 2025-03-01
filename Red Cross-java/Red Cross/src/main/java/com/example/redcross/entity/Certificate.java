package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("certificate")
public class Certificate {
    @TableId(value = "certificate_id", type = IdType.AUTO)
    private Integer certificateId;

    private Integer userId;

    private String certificateTitle;

    private String certificateContent;

    private String certificateTime;

}
