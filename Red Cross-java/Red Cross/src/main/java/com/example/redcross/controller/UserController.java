package com.example.redcross.controller;

import com.example.redcross.entity.User;
import com.example.redcross.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 标记为 RESTful 控制器
@RequestMapping("/user") // 定义基础路径
public class UserController {

    @Autowired // 自动注入 UserService
    private UserService userService;

    @GetMapping("/list") // 定义 GET 请求路径
    public List<User> listUsers() {
        return userService.list(); // 调用 Service 层方法
    }

}