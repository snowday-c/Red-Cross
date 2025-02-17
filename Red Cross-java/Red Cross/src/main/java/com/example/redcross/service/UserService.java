package com.example.redcross.service;

import com.example.redcross.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(); // 查询所有用户

    User login(String account, String password); // 登录

    User register(User user); // 注册
}