package com.example.redcross.mapper;

import com.example.redcross.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    // 查询所有用户
    List<User> getAllUsers();

    // 注册
    int register(User user);

    //重新邮箱是否存在
    Boolean isEmailExist(String email);

    // 登录
    Boolean login(String account, String password);

    Boolean admin(String account, String password);

    Boolean logout(String account, String password);

    Boolean updateUserType(String account,String changedAccount, Integer userType);

    Boolean isSuperAdmin(String account);

    Boolean updateUserInfo(User user);

    Boolean updatePassword(String account, String oldPassword, String newPassword);
}