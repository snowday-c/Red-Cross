<template>
  <div class="login-container">
    <el-card class="login-card">
      <h1 class="login-title">登录</h1>
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" @submit.native.prevent="login">
        <el-form-item prop="account">
          <el-input
            v-model="loginForm.account"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
          ></el-input>
          <!-- 显示错误信息 -->
          <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" class="login-button">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        <router-link to="/register">没有账号？立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        account: '',
        password: '',
      },
      loginRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
      },
      errorMessage: ''  // 错误信息
    };
  },
  methods: {
    login() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          // 管理员登录
          try {
            let res = await axios.post('http://localhost:8090/user/login', {
              account: this.loginForm.account,
              password: this.loginForm.password
            });
            if (res.data.code == 0) {
              localStorage.setItem('CurrentUser',JSON.stringify(res.data.data));
              localStorage.setItem('userId', res.data.data.userId);
              this.$router.push('/management');
            } else {
              this.errorMessage = '账号或密码错误';  // 设置错误信息
            }
          } catch (error) {
            this.errorMessage = '登录请求失败，请稍后再试';  // 网络错误时的提示
          }
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #303133;
}

.login-button {
  width: 100%;
  margin-top: 10px;
}

.register-link {
  text-align: center;
  margin-top: 15px;
}

.register-link a {
  color: #409EFF;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.error-message {
  color: red;
  font-size: 14px;
  margin-top: 5px;
}
</style>
