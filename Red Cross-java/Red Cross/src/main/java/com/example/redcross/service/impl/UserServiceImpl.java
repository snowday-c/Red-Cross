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
        return userMapper.getAllUsers(); // 重新全部用户
    }

    @Override
    // 注册
    public int register(User user) {
        return userMapper.register(user);
    }

    @Override
    public Boolean isEmailExist(String email) {
        return userMapper.isEmailExist(email);
    }

    @Override
    public Boolean login(String account, String password) {
        return userMapper.login(account, password);
    }

    @Override
    public Boolean admin(String account, String password) {
        return userMapper.admin(account, password);
    }

    @Override
    public Boolean logout(String account, String password) {
        return userMapper.logout(account, password);
    }

    @Override
    public Boolean updateUserType(String account,String changedAccount, Integer userType) {
        return userMapper.updateUserType(account,changedAccount, userType);
    }

    @Override
    public Boolean isSuperAdmin(String account) {
        return userMapper.isSuperAdmin(account);
    }

    @Override
    public Boolean updateUserInfo(User user) {
        return userMapper.updateUserInfo(user);
    }

    @Override
    public Boolean updatePassword(String account, String oldPassword, String newPassword) {
        return userMapper.updatePassword(account, oldPassword, newPassword);
    }
}