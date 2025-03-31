<template>
  <div class="login-container">
    <el-card class="login-card">
      <!-- 添加红十字标志的图片 -->
      <img src="@/assets/picture/picture02.jpg" alt="Red Cross Logo" class="logo">
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
      <div class="links">
        <router-link to="/register" class="link">没有账号？立即注册</router-link>
        <a href="javascript:;" class="link" @click="showForgotPasswordDialog">忘记密码？</a>
      </div>
    </el-card>

    <!-- 找回密码对话框 - 添加 close-on-click-modal 和 before-close 属性 -->
    <el-dialog 
      title="找回密码" 
      :visible.sync="forgotPasswordDialogVisible" 
      width="30%"
      :close-on-click-modal="false"
      :before-close="handleCloseForgotPasswordDialog"
    >
      <el-form :model="forgotPasswordForm" :rules="forgotPasswordRules" ref="forgotPasswordForm" label-width="100px">
        <el-form-item label="账号" prop="account">
          <el-input v-model="forgotPasswordForm.account" placeholder="请输入您的账号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="forgotPasswordForm.email" placeholder="请输入绑定的邮箱"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode">
          <div class="verification-code-container">
            <el-input v-model="forgotPasswordForm.verificationCode" placeholder="请输入验证码"></el-input>
            <el-button 
              type="primary" 
              :disabled="sendCodeDisabled" 
              @click="sendVerificationCode"
              class="send-code-btn"
            >
              {{ sendCodeButtonText }}
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="forgotPasswordForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="forgotPasswordForm.confirmPassword" type="password" placeholder="请再次输入新密码"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCloseForgotPasswordDialog">取 消</el-button>
        <el-button type="primary" @click="resetPassword">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'Login',
  data() {
    // 密码确认验证规则
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.forgotPasswordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    return {
      loginForm: {
        account: '',
        password: '',
      },
      loginRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 6, max: 20, message: '账号长度应为 6 到 20 位', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度应为 6 到 20 位', trigger: 'blur' },
        ],
      },
      errorMessage: '',
      
      // 找回密码相关数据
      forgotPasswordDialogVisible: false,
      forgotPasswordForm: {
        account: '',
        email: '',
        verificationCode: '',
        newPassword: '',
        confirmPassword: ''
      },
      forgotPasswordRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 6, max: 20, message: '账号长度应为 6 到 20 位', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { len: 6, message: '验证码长度为6位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度应为 6 到 20 位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      sendCodeDisabled: false,
      sendCodeButtonText: '获取验证码',
      countdown: 60
    };
  },
  methods: {
    async login() {
  this.$refs.loginForm.validate(async (valid) => {
    if (valid) {
      try {
        // 1. 先检查用户状态
        const checkRes = await request.post('/user/checkLogin', {
          account: this.loginForm.account
        });
        
        // 根据不同的返回码处理不同情况
        if (checkRes.data.code === '-1') {
          // 用户已登录或用户不存在
          if (checkRes.data.message === "用户已登录") {
            // 用户已登录，询问是否继续登录
            this.$confirm('该账号已在其他地方登录，是否继续登录？继续登录将使之前的登录无效', '提示', {
              confirmButtonText: '继续登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(async () => {
              // 用户确认继续登录
              await this.performLogin();
            }).catch(() => {
              // 用户取消登录
              this.errorMessage = '登录已取消';
            });
          } else {
            // 其他错误情况（用户不存在或系统异常）
            this.errorMessage = checkRes.data.message || '账号验证失败';
          }
        } else if (checkRes.data.code === '0') {
          // 用户未登录，直接登录
          await this.performLogin();
        } else {
          // 其他未知状态码
          this.errorMessage = checkRes.data.message || '登录检查失败';
        }
      } catch (error) {
        this.errorMessage = '登录请求失败，请稍后再试';
        console.error('登录错误:', error);
      }
    } else {
      return false;
    }
  });
},
    
    // 执行实际的登录操作
    async performLogin() {
      try {
        let res = await request.post('/user/admin', {
          account: this.loginForm.account,
          password: this.loginForm.password
        });
        
        if (res.data.code == 0) {
          localStorage.setItem('CurrentUser', JSON.stringify(res.data.data));
          localStorage.setItem('userId', res.data.data.userId);
          localStorage.setItem('token', res.data.data.token);
          this.$router.push('/management');
        } else {
          this.errorMessage = '账号或密码错误';
        }
      } catch (error) {
        this.errorMessage = '登录请求失败，请稍后再试';
      }
    },
    
    // 显示找回密码对话框
    showForgotPasswordDialog() {
      this.forgotPasswordDialogVisible = true;
      // 重置表单
      this.$nextTick(() => {
        if (this.$refs.forgotPasswordForm) {
          this.$refs.forgotPasswordForm.resetFields();
        }
      });
    },
    
    // 处理对话框关闭
    handleCloseForgotPasswordDialog() {
      // 检查是否有填写内容
      const hasContent = Object.values(this.forgotPasswordForm).some(
        value => value && value.trim() !== ''
      );
      
      if (hasContent) {
        this.$confirm('确定要放弃当前填写的内容吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.forgotPasswordDialogVisible = false;
          this.$refs.forgotPasswordForm.resetFields();
        }).catch(() => {
          // 取消关闭，不做任何操作
        });
      } else {
        // 没有填写内容，直接关闭
        this.forgotPasswordDialogVisible = false;
      }
    },
    
    // 发送验证码
    async sendVerificationCode() {
      // 验证邮箱和账号
      try {
        await this.$refs.forgotPasswordForm.validateField('account');
        await this.$refs.forgotPasswordForm.validateField('email');
      } catch (e) {
        return;
      }
      
      try {
        const res = await request.post('/user/email/sendCode', {
          account: this.forgotPasswordForm.account,
          email: this.forgotPasswordForm.email
        });
        
        if (res.data.code == 0) {
          this.$message.success('验证码发送成功');
          this.startCountdown();
        } else {
          this.$message.error(res.data.msg || '验证码发送失败');
        }
      } catch (error) {
        this.$message.error('验证码发送失败，请稍后再试');
      }
    },
    
    // 开始倒计时
    startCountdown() {
      this.sendCodeDisabled = true;
      this.sendCodeButtonText = `${this.countdown}秒后重新获取`;
      
      const timer = setInterval(() => {
        this.countdown--;
        this.sendCodeButtonText = `${this.countdown}秒后重新获取`;
        
        if (this.countdown <= 0) {
          clearInterval(timer);
          this.sendCodeDisabled = false;
          this.sendCodeButtonText = '获取验证码';
          this.countdown = 60;
        }
      }, 1000);
    },
    
    // 重置密码
    async resetPassword() {
      try {
        await this.$refs.forgotPasswordForm.validate();
      } catch (e) {
        return;
      }
      
      try {
        // 1. 先验证验证码
        const verifyRes = await request.post('/user/email/verifyCode', {
          account: this.forgotPasswordForm.account,
          email: this.forgotPasswordForm.email,
          code: this.forgotPasswordForm.verificationCode
        });
        
        if (verifyRes.data.code == 1) {
          this.$message.error(verifyRes.data.msg || '验证码验证失败');
          return;
        }
        
        // 2. 验证通过后修改密码
        const resetRes = await request.post('/user/forget/password', {
          account: this.forgotPasswordForm.account,
          email: this.forgotPasswordForm.email,
          newPassword: this.forgotPasswordForm.newPassword
        });
        
        if (resetRes.data.code == 0) {
          this.$message.success('密码重置成功');
          this.forgotPasswordDialogVisible = false;
        } else {
          this.$message.error(resetRes.data.msg || '密码重置失败');
        }
      } catch (error) {
        this.$message.error('密码重置失败，请稍后再试');
      }
    }
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
  background-image: url('@/assets/picture/picture01.jpg');
  background-size: cover;
  background-position: center;
}

.login-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.9);
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

.links {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.link {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
}

.link:hover {
  text-decoration: underline;
}

.error-message {
  color: red;
  font-size: 14px;
  margin-top: 5px;
}

.logo {
  display: block;
  margin: 0 auto 10px auto;
  width: 50px;
  height: auto;
}

.verification-code-container {
  display: flex;
  align-items: center;
}

.send-code-btn {
  margin-left: 10px;
  width: 120px;
}
</style>