package com.example.redcross.service.impl;

import com.example.redcross.entity.User;
import com.example.redcross.mapper.UserMapper;
import com.example.redcross.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers(); // 调用 Mapper 方法
    }

    @Override
    public User login(String account, String password) {
        User user = userMapper.login(account, password);
        if (user == null) {
            throw new RuntimeException("登录失败：账号或密码错误");
        }
        return user;
    }

    @Override
    public User register(User user) {
        return userMapper.register(user);
    }
}