package com.example.redcross.mapper;

import com.example.redcross.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUsers(); // 查询所有用户

    User login(String account, String password); // 登录

    User register(User user); // 注册
}