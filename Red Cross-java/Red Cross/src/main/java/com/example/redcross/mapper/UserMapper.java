package com.example.redcross.mapper;

import com.example.redcross.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUsers();    // 查询所有用户

    Integer register(User user);    // 注册

    Boolean isEmailExist(String email);    // 邮箱是否存在

    Boolean isAccountExist(String account);    // 账号是否存在

    Boolean isUserNameExist(String userName);    // 用户名是否存在

    Boolean isSuperAdmin(Integer userId);    // 是否超级管理员

    Boolean login(String account, String password);    // 登录

    Boolean admin(String account, String password);    // 管理员登录

    Boolean logout(String email, String account, String password);    // 用户注销

    //修改用户权限
    Boolean updateUserType(Integer userId,Integer changedUserId, Integer userType);

    Boolean updateUserInfo(User user);    // 修改用户信息

    // 修改密码
    Boolean updatePassword(String account, String oldPassword, String newPassword);

    // 忘记密码
    Boolean forgetPassword(String account,String email, String newPassword);

    User getUserByAccount(String account);

    User getUserById(Integer id);

    Integer getUserId(String account);



    Integer updateMessageUserName(String oldName, String userName);
}