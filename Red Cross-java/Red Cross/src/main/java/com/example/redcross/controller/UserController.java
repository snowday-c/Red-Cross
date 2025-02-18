package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.User;
import com.example.redcross.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/all") // 返回所有用户
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    @PostMapping("/register") // 注册用户
    public Result registerUser(User user) {
        userService.register(user);
        return Result.success();
    }

    @GetMapping("/login") // 登录
    public Result login(String account, String password) {
        User user = userService.login(account, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success(user);
    }


}