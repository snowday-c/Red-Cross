package com.example.redcross.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String userName;

    private String pictureUrl;

    private String email;

    private String account;

    private String password;

    private Integer userType;

    //非数据库字段
    @TableField(exist = false)
    private Integer changedUserId;//修改用户权限时，被修改用户的id
    @TableField(exist = false)
    private String oldPassword;//修改密码时，旧密码
    @TableField(exist = false)
    private String newPassword;//修改密码时，新密码

}
