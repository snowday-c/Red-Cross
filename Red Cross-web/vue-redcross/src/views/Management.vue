<template>
  <el-container style="height: 100vh;">
    <!-- 项目名称 -->
    <el-header style="background-color: #304156; color: #fff; line-height: 60px; text-align: left; padding-left: 20px;">
      红十字救生员培训管理
    </el-header>

    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="190px" style="background-color: #476080;">
        <el-menu
          default-active="/management/dashboard"
          class="el-menu-vertical-demo"
          background-color="#476080"
          text-color="#fff"
          active-text-color="#ffd04b"
          router
        >
          <el-menu-item index="/management/dashboard">
            <i class="el-icon-s-home"></i>
            <span>系统首页</span>
          </el-menu-item>
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
};
</script>

<style>
.el-container {
  margin: 0;
  padding: 0;
}

.el-header {
  background-color: #304156;
  color: #fff;
  line-height: 60px;
  font-size: 20px;
  text-align: left;
  padding-left: 20px;
}

.el-aside {
  background-color: #476080;
  color: #fff;
}

.el-menu {
  border-right: none;
}

.el-main {
  padding: 20px;
  margin: 0;
}
</style>