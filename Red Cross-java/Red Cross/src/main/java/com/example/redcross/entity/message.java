package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("message")
public class Message {

    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    private String title;

    private String content;

    private String sender;

    private String receiver;

    private Integer messageType;

    private String time;

//    private String status;
}
