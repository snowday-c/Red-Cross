package com.example.redcross.service;

import com.example.redcross.entity.User;

import java.util.List;

public interface UserService {
    public void sendVerificationCode(String to, String subject, String content); // 发送验证码
    List<User> getAllUsers(); // 查询所有用户

    Integer register(User user); // 注册

    Boolean isEmailExist(String email);

    Boolean isAccountExist(String account);

    Boolean isUserNameExist(String userName);

    Boolean login(String account, String password);

    Boolean admin(String account, String password);

    Boolean logout(String email, String account, String password);

    Boolean updateUserType(Integer userId ,Integer changedUserId,Integer userType);

    Boolean isSuperAdmin(Integer userId);

    Boolean updateUserInfo(User user);

    Boolean updatePassword(String account, String oldPassword, String newPassword);

    Boolean forgetPassword(String account, String email, String newPassword);

    User getUserByAccount(String account);

    User getUserById(Integer id);

    Integer getUserId(String account);


    Integer updateMessageUserName(String oldName, String userName);
}