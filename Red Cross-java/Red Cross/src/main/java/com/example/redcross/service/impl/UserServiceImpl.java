package com.example.redcross.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.redcross.entity.User;
import com.example.redcross.mapper.UserMapper;
import com.example.redcross.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 标记为 Spring 的 Service 组件
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}