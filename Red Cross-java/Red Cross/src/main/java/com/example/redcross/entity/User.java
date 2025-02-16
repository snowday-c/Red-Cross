package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {

    @TableId(value = "user_id")
    private int userId;

    private String userName;

    private String pictureUrl;

    private String email;

    private String account;

    private String password;

    private int userType;

}
