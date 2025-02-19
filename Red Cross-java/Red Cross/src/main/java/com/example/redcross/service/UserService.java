package com.example.redcross.service;

import com.example.redcross.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(); // 查询所有用户

    int register(User user); // 注册

    Boolean isEmailExist(String email);

    Boolean login(String account, String password);

    Boolean admin(String account, String password);

    Boolean logout(String account, String password);

    Boolean updateUserType(String account,String changedAccount ,Integer userType);

    Boolean isSuperAdmin(String account);

    Boolean updateUserInfo(User user);
}