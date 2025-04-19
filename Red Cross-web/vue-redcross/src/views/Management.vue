<template>
  <el-container style="height: 100vh;">
    <!-- 头部 -->
    <el-header class="header" style="color: black; line-height: 60px; display: flex; justify-content: space-between; align-items: center; padding: 0 20px;">
      <!-- 项目名称 -->
      <div style="font-size: 30px; font-weight: bold; font-family: 'Microsoft YaHei', sans-serif;">
        红十字救生员培训管理
      </div>

      <!-- 用户信息和退出按钮 -->
      <div style="display: flex; align-items: center;">
        <span style="margin-right: 20px;">欢迎，{{ currentUser.userName }}</span>
        <el-button 
          type="primary" 
          size="small" 
          @click="showChangePasswordDialog"
          style="margin-right: 10px;"
        >
          修改密码
        </el-button>
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>

    <el-container>
      <!-- 侧边栏 -->
      <el-aside class="aside" width="190px">
        <el-menu
          default-active="/management/dashboard"
          class="el-menu-vertical-demo"
          background-color="transparent"
          text-color="black"
          active-text-color="green"
          router
        >
          <el-menu-item index="/management/users">
            <i class="el-icon-user"></i>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/management/messages">
            <i class="el-icon-info"></i>
            <span>通知管理</span>
          </el-menu-item>
          <el-menu-item index="/management/questions">
            <i class="el-icon-reading"></i>
            <span>试题管理</span>
          </el-menu-item>
          <el-menu-item index="/management/trains">
            <i class="el-icon-s-opportunity"></i>
            <span>培训管理</span>
          </el-menu-item>
          <el-menu-item index="/management/certificates">
            <i class="el-icon-s-order"></i>
            <span>证书管理</span>
          </el-menu-item>
          <el-menu-item index="/management/feedback">
            <i class="el-icon-chat-line-square"></i>
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="/score-analysis">
            <i class="el-icon-data-analysis"></i>
            <span slot="title">成绩分析</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>

    <!-- 修改密码对话框 -->
    <el-dialog 
      title="修改密码" 
      :visible.sync="changePasswordDialogVisible" 
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="changePasswordForm" 
        :rules="changePasswordRules" 
        ref="changePasswordForm"
        label-width="100px"
      >
        <el-form-item label="账号" prop="account">
          <el-input 
            v-model="changePasswordForm.account" 
            placeholder="请输入您的账号"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input 
            v-model="changePasswordForm.oldPassword" 
            type="password" 
            placeholder="请输入旧密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            v-model="changePasswordForm.newPassword" 
            type="password" 
            placeholder="请输入新密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="changePasswordForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入新密码"
            show-password
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="changePasswordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleChangePassword">确 定</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'Management',
  data() {
    // 确认密码验证规则
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.changePasswordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    return {
      currentUser: {}, // 当前用户信息
      changePasswordDialogVisible: false, // 修改密码对话框显示状态
      changePasswordForm: {
        account: '',
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      changePasswordRules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度应为6-20位', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度应为6-20位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.getCurrentUser(); // 获取当前用户信息
  },
  methods: {
    // 获取当前用户信息
    getCurrentUser() {
      const user = JSON.parse(localStorage.getItem('CurrentUser'));
      if (user) {
        this.currentUser = user;
        this.changePasswordForm.account = user.account; // 设置账号字段
      } else {
        this.$message.warning('未检测到登录信息，请重新登录');
        this.$router.push('/login');
      }
    },
    
    // 退出登录
    async handleLogout() {
      try {
        //获取当前用户ID
        const userId = localStorage.getItem('userId');
        //发送请求到后端清除token
        if (userId) {
          await request.post('/user/forceLogout', {
            userId: userId
          });
        }
        //清除本地存储
        localStorage.removeItem('CurrentUser');
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        //跳转到登录页
        this.$router.push('/login');
        this.$message.success('退出登录成功');
        
      } catch (error) {
        console.error('退出登录失败:', error);
        
        //即使后端请求失败，也要清除本地存储
        localStorage.removeItem('CurrentUser');
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        
        this.$router.push('/login');
        this.$message.success('已退出本地登录');
      }
    },
    
    // 显示修改密码对话框
    showChangePasswordDialog() {
      this.changePasswordDialogVisible = true;
      // 重置表单，但保留账号
      this.$nextTick(() => {
        if (this.$refs.changePasswordForm) {
          this.changePasswordForm.oldPassword = '';
          this.changePasswordForm.newPassword = '';
          this.changePasswordForm.confirmPassword = '';
          this.$refs.changePasswordForm.clearValidate();
        }
      });
    },
    
    // 处理修改密码
    handleChangePassword() {
      this.$refs.changePasswordForm.validate(async (valid) => {
        if (valid) {
          try {
            const res = await request.post('/user/update/password', {
              account: this.changePasswordForm.account,
              oldPassword: this.changePasswordForm.oldPassword,
              newPassword: this.changePasswordForm.newPassword
            });
            
            if (res.data.code == 0) {
              this.$message.success('密码修改成功，请重新登录');
              this.changePasswordDialogVisible = false;
              
              // 4秒后跳转到登录页面
              setTimeout(() => {
                this.handleLogout();
              }, 3000);
            } else {
              this.$message.error(res.data.message || '密码修改失败');
            }
          } catch (error) {
            this.$message.error('密码修改失败，请稍后再试');
          }
        }
      });
    }
  }
};
</script>

<style>
.el-container {
  margin: 0;
  padding: 0;
}

.header {
  background-image: url('@/assets/picture/picture03.jpg'); 
  background-size: fill;
  background-position: center;
  background-color: #304156;
}

.aside {
  background-color: #b2d6df;
}

.el-menu {
  border-right: none;
  background-color: transparent !important;
}

.el-menu-item {
  background-color: transparent !important;
}

.el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

.el-main {
  padding: 20px;
  margin: 0;
}
</style>