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

    @GetMapping("/all") // 显示所有用户
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    @PostMapping("/register") // 注册用户
    public Result registerUser(@RequestBody User user) {
        //若邮箱已注册，则返回错误信息
        if (userService.isEmailExist(user.getEmail())) {
            return Result.error("此邮箱已注册");
        }
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/login") // 用户登录
    public Result login(@RequestParam("account") String account,
                        @RequestParam("password") String password) {
        // 登录成功返回token，失败返回错误信息
        if (userService.login(account, password)) {
            return Result.success();
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/admin")  //管理员登录
    public Result admin(@RequestParam("account") String account,
                        @RequestParam("password") String password) {
        // 登录成功返回token，失败返回错误信息
        if (userService.admin(account, password)) {
            return Result.success();
        }
        return Result.error("用户名或密码错误");
    }
    // 注销用户
    @PostMapping("/logout")
    public Result logout(@RequestParam("account") String account,
                         @RequestParam("password") String password) {
        if (userService.logout(account, password)) {
            return Result.success();
        }
        return Result.error( "注销失败" );
    }
    // 修改用户权限
    @PostMapping("/update/userType")
    public Result update(@RequestParam("account") String account,
                         @RequestParam("changedAccount") String changedAccount,
                         @RequestParam("userType") Integer userType) {
        if (userType == 0 || userType == 1) {
            if (userService.isSuperAdmin(account)) {
                if (userService.updateUserType(account, changedAccount, userType)) {
                    return Result.success();
                }
                return Result.error("修改失败");
            }
            return Result.error("修改人权限不足");
        }
        return Result.error("将要修改的权限不合法");
    }
    //修改用户信息
    @PostMapping("/update/userInfo")
    public Result updateUserInfo(@RequestBody User user) {
        // 若邮箱已注册，则返回错误信息
        if (userService.isEmailExist(user.getEmail())) {
            return Result.error("此邮箱已绑定");
        }else if (userService.updateUserInfo(user)) {
            return Result.success();
        }
        return Result.error("修改失败");
    }
    //修改密码
    @PostMapping("/update/password")
    public Result updatePassword(@RequestParam("account") String account,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (userService.updatePassword(account, oldPassword, newPassword)) {
            return Result.success();
        }
        return Result.error("修改失败，请检查账号和密码是否正确");
    }
}