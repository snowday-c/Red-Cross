<template>
  <div>
    <h1>用户管理</h1>
    <el-table :data="userList" style="width: 100%" border>
      <el-table-column prop="userName" label="用户名" width="180"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="account" label="账号"></el-table-column>
      <el-table-column prop="userType" label="用户类型"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">修改</el-button>
          <el-button type="info" size="small" @click="handleEditUserType(scope.row)">修改用户类型</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改用户对话框 -->
    <el-dialog :visible.sync="dialogVisible" title="修改用户" width="30%">
      <el-form :model="currentUser" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="currentUser.userName"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="currentUser.email"></el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="currentUser.account"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </span>
    </el-dialog>

    <!-- 修改用户类型对话框 -->
    <el-dialog :visible.sync="userTypeDialogVisible" title="修改用户类型" width="30%">
      <el-form :model="currentUser" label-width="80px">
        <el-form-item label="用户类型">
          <el-select v-model="currentUser.userType" placeholder="请选择用户类型">
            <el-option label="用户" value="0"></el-option>
            <el-option label="管理员" value="1"></el-option>
            <el-option label="超级管理员" value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="userTypeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUserType">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      userList: [], // 用户列表数据
      dialogVisible: false, // 修改用户对话框是否显示
      userTypeDialogVisible: false, // 修改用户类型对话框是否显示
      currentUser: {} // 当前修改的用户
    };
  },
  created() {
    this.fetchUsers(); // 页面加载时获取用户数据
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get('http://localhost:8090/user/all'); // 替换为你的API地址
        let users = response.data.data;

        // 转换用户类型数字为文字
        this.userList = users.map(user => {
          switch (user.userType) {
            case 0:
              user.userType = '用户';
              break;
            case 1:
              user.userType = '管理员';
              break;
            case 2:
              user.userType = '超级管理员';
              break;
            default:
              user.userType = '未知';
          }
          return user;
        });
        console.log('转换后的用户列表:', this.userList); // 打印转换后的用户列表
      } catch (error) {
        console.error('获取用户数据失败:', error);
      }
    },
    handleEdit(user) {
      this.currentUser = { ...user }; // 复制用户数据
      this.dialogVisible = true; // 显示修改用户对话框
      console.log('当前用户:', this.currentUser); // 打印当前用户
    },
    handleEditUserType(user) {
      this.currentUser = { ...user }; // 复制用户数据
      this.userTypeDialogVisible = true; // 显示修改用户类型对话框
      console.log('当前用户:', this.currentUser); // 打印当前用户
    },
    async saveUser() {
      try {
        let res = await axios.post('http://localhost:8090/user/update/userInfo', this.currentUser);
        if (res.data.code == 0) {
          this.dialogVisible = false; // 关闭对话框
          this.fetchUsers(); // 重新获取用户数据
        } else {
          this.$message.error('保存用户数据失败:' + res.data.message);
        }
      } catch (error) {
        console.error('保存用户数据失败:', error);
      }
    },
    async saveUserType() {
      try {
        // 这里发送的 currentUser.userType 是数字类型（0 或 1）
        let res = await axios.post('http://localhost:8090/user/update/userType', { 
          userId: this.currentUser.userId,          //管理员id
          changedUserId: this.currentUser.userId,   //被修改者id
          userType: this.currentUser.userType });   //修改后的用户类型
        if (res.data.code == 0) {
          this.userTypeDialogVisible = false; // 关闭修改用户类型对话框
          this.fetchUsers(); // 重新获取用户数据
        } else {
          this.$message.error('保存用户类型失败:' + res.data.message);
        }
      } catch (error) {
        console.error('保存用户类型失败:', error);
      }
    }
  }
};
</script>

<style scoped>
h1 {
  text-align: center;
  margin-bottom: 20px;
}
</style>
