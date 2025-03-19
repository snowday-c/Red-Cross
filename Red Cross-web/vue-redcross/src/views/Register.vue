<template>
  <div class="register-container">
    <el-card class="register-card">
      <h1 class="register-title">注册</h1>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" @submit.native.prevent="register">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>

        <!-- 账号 -->
        <el-form-item prop="account">
          <el-input
            v-model="registerForm.account"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
          ></el-input>
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            placeholder="请再次输入密码"
            prefix-icon="el-icon-lock"
            show-password
          ></el-input>
        </el-form-item>

        <!-- 邮箱 -->
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            prefix-icon="el-icon-message"
          ></el-input>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item prop="code">
          <el-input
            v-model="registerForm.code"
            placeholder="请输入验证码"
            prefix-icon="el-icon-key"
          >
            <template #append>
              <el-button :disabled="isCodeSent" @click="sendCode">
                {{ isCodeSent ? `${countdown}秒后重试` : '发送验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>

        <!-- 注册按钮 -->
        <el-form-item>
          <el-button type="primary" native-type="submit" class="register-button">注册</el-button>
        </el-form-item>
      </el-form>

      <!-- 登录链接 -->
      <div class="login-link">
        <router-link to="/login">已有账号？立即登录</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        account: '',
        password: '',
        confirmPassword: '',
        email: '',
        code: '',
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
                callback(new Error('两次输入的密码不一致'));
              } else {
                callback();
              }
            },
            trigger: 'blur',
          },
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] },
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
        ],
      },
      isCodeSent: false, // 是否已发送验证码
      countdown: 60, // 倒计时
    };
  },
  methods: {
    // 注册逻辑
    register() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          try {
            // 校验验证码
            let res1 = await request.post('/user/email/verifyCode', {
              email: this.registerForm.email,
              code: this.registerForm.code,
            });
            if (res1.data.code != 0) {
              this.$message.error('验证码错误，请重新输入');
              return false;
            }

            // 注册请求
            let res = await request.post('/user/register', {
              userName: this.registerForm.username,
              account: this.registerForm.account,
              password: this.registerForm.password,
              email: this.registerForm.email,
              code: this.registerForm.code,
            });

            if (res.data.code == 0) {
              this.$message.success('注册成功，即将跳转登录页面');
              this.$router.push('/login');
            } else {
              this.$message.error(res.data.msg || '注册失败，请稍后再试');
            }
          } catch (error) {
            console.error('注册失败:', error);
            this.$message.error('注册失败，请检查网络或稍后再试');
          }
        } else {
          return false;
        }
      });
    },

    // 发送验证码
    sendCode() {
      this.$refs.registerForm.validateField('email', async (valid) => {
        if (!valid) {
          try {
            let res = await request.post('/user/email/sendCode', {
              email: this.registerForm.email,
            });

            if (res.data.code == 0) {
              this.$message.success('验证码已发送至邮箱，请注意查收');
              this.isCodeSent = true;
              this.startCountdown();
            } else {
              this.$message.error(res.data.msg || '验证码发送失败，请稍后再试');
            }
          } catch (error) {
            console.error('验证码发送失败:', error);
            this.$message.error('验证码发送失败，请检查网络或稍后再试');
          }
        }
      });
    },

    // 开始倒计时
    startCountdown() {
      if (this.countdown <= 0) {
        this.isCodeSent = false;
        this.countdown = 60;
        return;
      }

      const timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
        } else {
          clearInterval(timer);
          this.isCodeSent = false;
          this.countdown = 60;
        }
      }, 1000);
    },
  },
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.register-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #303133;
}

.register-button {
  width: 100%;
  margin-top: 10px;
}

.login-link {
  text-align: center;
  margin-top: 15px;
}

.login-link a {
  color: #409EFF;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>