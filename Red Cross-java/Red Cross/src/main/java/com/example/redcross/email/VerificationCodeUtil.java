package com.example.redcross.email;

import java.util.Random;

public class VerificationCodeUtil {

    /**
     * 生成 6 位随机验证码
     *
     * @return 验证码
     */
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成 100000 到 999999 之间的随机数
        return String.valueOf(code);
    }
}
