package com.example.redcross.controller;

import com.example.redcross.entity.User;
import com.example.redcross.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers(); // 返回所有用户
    }

    @PostMapping ("/register")


    @GetMapping("/login")
    public User login(int account, String password) {
        return userService.login(account, password); // 登录
    }
}