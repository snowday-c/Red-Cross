package com.example.redcross.controller;


import com.example.redcross.common.JwtTokenUtils;
import com.example.redcross.common.Result;
import com.example.redcross.email.VerificationCodeUtil;
import com.example.redcross.entity.User;
import com.example.redcross.exception.UserException;
import com.example.redcross.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/email/sendCode")
    public Result sendVerificationCode(@RequestBody User user) {
        String email = user.getEmail();

        // 生成验证码
        String code = VerificationCodeUtil.generateCode();
        // 存储验证码到 Redis，设置过期时间为 5 分钟
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

        // 发送邮件
        String subject = "您的验证码";
        String content = "您的验证码是：" + code + "，请勿泄露给他人，请在5分钟内使用。";
        userService.sendVerificationCode(email, subject, content);

        return Result.success();
    }

    @PostMapping("/email/verifyCode")
    public Result verifyCode(@RequestBody User user) {
        String email = user.getEmail();
        String code = user.getCode();
        // 从 Redis 中获取验证码
        String cachedCode = redisTemplate.opsForValue().get(email);

        if (cachedCode != null && cachedCode.equals(code)) {
            // 验证码正确，可以执行后续操作
            redisTemplate.delete(email); // 验证成功后移除验证码
            return Result.success("验证码正确");
        } else {
            return Result.error("验证码错误");
        }
    }

    @GetMapping("/all") // 显示所有用户
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    @PostMapping("/register") // 注册用户
    public Result registerUser(@RequestBody User user) {
        // 若账户已注册，则返回错误信息
        if (userService.isAccountExist(user.getAccount())) {
            throw new UserException("此账号已注册");
        }
        //若邮箱已注册，则返回错误信息
        if (userService.isEmailExist(user.getEmail())) {
            throw new UserException("此邮箱已注册");
        }
        //若用户名已注册，则返回错误信息
        if (userService.isUserNameExist(user.getUserName())) {
            throw new UserException("此用户名已注册");
        }

        userService.register(user);
        return Result.success();
    }

    @PostMapping("/login") // 用户登录
    public Result login(@RequestBody User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        // 登录成功返回用户，失败返回错误信息
        if (userService.login(account, password)) {
            User CurrentUser = userService.getUserByAccount(account);
            Integer userId = CurrentUser.getUserId();
            String token = JwtTokenUtils.genToken(userId.toString(), password);
            CurrentUser.setToken(token);
            // 手动剔除不需要的字段
//            CurrentUser.setAccount(null);  // 剔除账号
            return Result.success(CurrentUser);
        }
        throw new UserException("用户名或密码错误");
    }

    @PostMapping("/admin")  //管理员登录
    public Result admin(@RequestBody User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        // 登录成功返回用户，失败返回错误信息
        if (userService.admin(account, password)) {
            User CurrentUser = userService.getUserByAccount(account);
            Integer userId = CurrentUser.getUserId();
            String token = JwtTokenUtils.genToken(userId.toString(), password);
            CurrentUser.setToken(token);
            // 手动剔除不需要的字段
//            CurrentUser.setAccount(null);  // 剔除账号
            return Result.success(CurrentUser);
        }
        throw new UserException("用户名或密码错误");
    }
    // 注销用户
    //TODO:注销需要发送邮箱验证码
    @PostMapping("/logout")
    public Result logout(@RequestBody User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        if (userService.logout(account, password)) {
            return Result.success();
        }
        throw new UserException("注销失败，请检查账号和密码是否正确");
    }
    // 修改用户权限
    @PostMapping("/update/userType")
    public Result update(@RequestBody User user) {
        Integer userId = user.getUserId();
        Integer changedUserId = user.getChangedUserId();
        Integer userType = user.getUserType();

        if (userType == 0 || userType == 1) {
            if (userService.isSuperAdmin(userId)) {
                if (userService.updateUserType(userId, changedUserId, userType)) {
                    return Result.success();
                }
                throw new UserException("修改失败,请检查账号");
            }
            throw new UserException("修改人权限不足");
        }
        throw new UserException("将要修改的权限不合法");
    }
    //修改用户信息
    @PostMapping("/update/userInfo")
    public Result updateUserInfo(@RequestBody User user) {
        // 1. 根据账号查询当前用户信息
        User existingUser = userService.getUserById(user.getUserId());
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        // 2. 检查邮箱是否修改
        if (!existingUser.getEmail().equals(user.getEmail())) {
            // 如果邮箱已修改，检查新邮箱是否已被其他用户占用
            if (userService.isEmailExist(user.getEmail())) {
                return Result.error("此邮箱已绑定");
            }
        }
        // 3. 检查账号是否修改
        if (!existingUser.getAccount().equals(user.getAccount())) {
            // 如果账号已修改，检查新账号是否已被其他用户占用
            if (userService.isAccountExist(user.getAccount())) {
                return Result.error("此账号已注册");
            }
        }
        // 4. 更新用户信息
        if (userService.updateUserInfo(user)) {
            return Result.success();
        } else {
            return Result.error("修改失败");
        }
    }

    //修改密码
    @PostMapping("/update/password")
    public Result updatePassword(@RequestBody User user) {

        String account = user.getAccount();
        String oldPassword = user.getOldPassword();
        String newPassword = user.getNewPassword();

        if (userService.updatePassword(account, oldPassword, newPassword)) {
            return Result.success();
        }
        return Result.error("修改失败，请检查账号和密码是否正确");
    }

    //忘记密码
    //TODO:忘记密码需要发送邮箱验证码
    @PostMapping("/forget/password")
    public Result forgetPassword(@RequestBody User User) {

        String account = User.getAccount();
        String email = User.getEmail();
        String newPassword = User.getNewPassword();

        if (userService.forgetPassword(account,email, newPassword)) {
            return Result.success();
        }
        throw new UserException("找回密码失败，请检查账号是否正确");
    }
}