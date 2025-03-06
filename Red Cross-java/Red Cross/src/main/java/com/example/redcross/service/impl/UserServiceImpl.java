package com.example.redcross.service.impl;

import com.example.redcross.entity.User;
import com.example.redcross.mapper.UserMapper;
import com.example.redcross.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2572886630@qq.com"); // 发件人邮箱
        message.setTo(to);                         // 收件人邮箱
        message.setSubject(subject);               // 邮件主题
        message.setText(content);                  // 邮件内容
        mailSender.send(message);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers(); // 重新全部用户
    }

    @Override
    // 注册
    public Integer register(User user) {
        return userMapper.register(user);
    }

    @Override
    public Boolean isEmailExist(String email) {
        return userMapper.isEmailExist(email);
    }

    @Override
    public Boolean isAccountExist(String account) {
        return userMapper.isAccountExist(account);
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
    public Boolean updateUserType(Integer userId,Integer changedUserId, Integer userType) {
        return userMapper.updateUserType(userId, changedUserId, userType);
    }

    @Override
    public Boolean isSuperAdmin(Integer userId) {
        return userMapper.isSuperAdmin(userId);
    }

    @Override
    public Boolean updateUserInfo(User user) {
        return userMapper.updateUserInfo(user);
    }

    @Override
    public Boolean updatePassword(String account, String oldPassword, String newPassword) {
        return userMapper.updatePassword(account, oldPassword, newPassword);
    }

    @Override
    public Boolean forgetPassword(String account, String email, String newPassword) {
        return userMapper.forgetPassword(account, email, newPassword);
    }

    @Override
    public User getUserByAccount(String account) {
        return userMapper.getUserByAccount(account);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public Integer getUserId(String account) {
        return userMapper.getUserId(account);
    }
}