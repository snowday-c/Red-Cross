package com.example.redcross.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    public Result sendVerificationCode(@RequestBody User user, HttpServletRequest request) {
        String email = user.getEmail();
        String ip = request.getRemoteAddr(); // 获取客户端IP
        // 限制同一IP每分钟最多发送3次验证码
        String ipKey = "email:sendCode:ip:" + ip;
        Long ipCount = redisTemplate.opsForValue().increment(ipKey, 1);
        if (ipCount != null && ipCount == 1) {
            redisTemplate.expire(ipKey, 1, TimeUnit.MINUTES); // 设置过期时间为1分钟
        }
        if (ipCount != null && ipCount > 3) {
            return Result.error("请求过于频繁，请稍后再试！");
        }
        // 限制同一邮箱每天最多发送20次验证码
        String emailKey = "email:sendCode:email:" + email;
        Long emailCount = redisTemplate.opsForValue().increment(emailKey, 1);
        if (emailCount != null && emailCount == 1) {
            redisTemplate.expire(emailKey, 24, TimeUnit.HOURS); // 设置过期时间为24小时
        }
        if (emailCount != null && emailCount > 20) {
            return Result.error("该邮箱今日请求次数过多，请明天再试！");
        }
        // 生成验证码
        String code = VerificationCodeUtil.generateCode();
        // 存储验证码到 Redis，设置过期时间为 5 分钟
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
        // 发送邮件
        String subject = "红十字救生员培训";
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
            throw new UserException("此账号已存在");
        }
        //若邮箱已注册，则返回错误信息
        if (userService.isEmailExist(user.getEmail())) {
            throw new UserException("此邮箱已存在");
        }
        //若用户名已注册，则返回错误信息
        if (userService.isUserNameExist(user.getUserName())) {
            throw new UserException("此用户名已存在");
        }
        userService.register(user);
        return Result.success();
    }

    // 检测用户是否已登录
    @PostMapping("/checkLogin")
    public Result checkLogin(@RequestBody User user) {
        try {
            String account = user.getAccount();
            User dbUser = userService.getUserByAccount(account);
            if (dbUser == null) {
                return Result.error("用户不存在");
            }
            // 从Redis中获取用户id
            String redisKey = "USER_TOKEN:" + dbUser.getUserId();
            if (redisTemplate.hasKey(redisKey)) {
                return Result.error( "用户已登录");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error("系统异常，redis连接失败，请稍后再试");
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        // 登录验证
        if (userService.login(account, password) != null) {
            User currentUser = userService.getUserByAccount(account);
            Integer userId = currentUser.getUserId();
            // 生成新token（会自动使旧token失效）
            String token = JwtTokenUtils.genToken(userId.toString(), password);
            currentUser.setToken(token);
            return Result.success(currentUser);
        }
        throw new UserException("用户名或密码错误");
    }

    @PostMapping("/admin")  //管理员登录
    public Result admin(@RequestBody User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        // 登录验证
        if (userService.admin(account, password) != null) {
            User currentUser = userService.getUserByAccount(account);
            Integer userId = currentUser.getUserId();
            // 生成新token（会自动使旧token失效）
            String token = JwtTokenUtils.genToken(userId.toString(), password);
            currentUser.setToken(token);
            return Result.success(currentUser);
        }
        throw new UserException("用户名或密码错误");
    }

    @PostMapping("/forceLogout")     // 管理员强制用户下线
    public Result forceLogout(@RequestBody User user) {
        Integer userId = user.getUserId();
        JwtTokenUtils.invalidateToken(userId);
        return Result.success("强制下线成功");
    }

    @PostMapping("/logout")     //注销用户
    public Result logout(@RequestBody User user) {
        String email = user.getEmail();
        String account = user.getAccount();
        String password = user.getPassword();
        if (userService.logout(email, account, password)) {
            return Result.success();
        }
        throw new UserException("注销失败，请检查账号和密码是否正确");
    }

    @PostMapping("/update/userType")    // 修改用户权限
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

    @PostMapping("/update/userInfo")    //修改用户信息
    public Result updateUserInfo(@RequestBody User user) {
        // 1. 根据id查询当前用户信息
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
        // 4. 检查用户名是否修改
        if (!existingUser.getUserName().equals(user.getUserName())) {
            // 如果用户名已修改，检查新用户名是否已被其他用户占用
            if (userService.isUserNameExist(user.getUserName())) {
                return Result.error("此用户名已注册");
            }
        }
        // 5. 更新用户信息
        if (userService.updateUserInfo(user)) {
            if (userService.updateMessageUserName(user.getOldName(), user.getUserName()) >=0){
            return Result.success();
            }
        }
        return Result.error("修改失败");
    }

    @PostMapping("/update/password")    //修改密码
    public Result updatePassword(@RequestBody User user) {

        String account = user.getAccount();
        String oldPassword = user.getOldPassword();
        String newPassword = user.getNewPassword();

        if (userService.updatePassword(account, oldPassword, newPassword)) {
            return Result.success();
        }
        return Result.error("修改失败，请检查账号和密码是否正确");
    }

    @PostMapping("/forget/password")    //忘记密码
    public Result forgetPassword(@RequestBody User User) {

        String account = User.getAccount();
        String email = User.getEmail();
        String newPassword = User.getNewPassword();

        if (userService.forgetPassword(account,email, newPassword)) {
            return Result.success();
        }
        throw new UserException("找回密码失败，请检查账号和邮箱是否正确");
    }
}