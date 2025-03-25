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
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-main>
        <router-view></router-view> <!-- 动态渲染子路由组件 -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Management',
  data() {
    return {
      currentUser: {}, // 当前用户信息
    };
  },
  created() {
    this.getCurrentUser(); // 获取当前用户信息
  },
  methods: {
    // 获取当前用户信息
    getCurrentUser() {
      const user = JSON.parse(localStorage.getItem('CurrentUser')); // 从本地存储获取用户信息
      if (user) {
        this.currentUser = user;
      } else {
        this.$message.warning('未检测到登录信息，请重新登录');
        this.$router.push('/login'); // 跳转到登录页面
      }
    },
    // 退出登录
    handleLogout() {
      localStorage.removeItem('CurrentUser'); // 清除用户信息
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      this.$router.push('/login'); // 跳转到登录页面
      this.$message.success('退出登录成功');
    },
  },
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
  background-color: transparent !important; /* 设置菜单背景为透明 */
}

.el-menu-item {
  background-color: transparent !important; /* 设置菜单项背景为透明 */
}

.el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important; /* 鼠标悬停时的背景颜色 */
}

.el-main {
  padding: 20px;
  margin: 0;
}
</style>