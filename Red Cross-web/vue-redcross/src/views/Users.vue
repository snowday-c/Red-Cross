<template>
  <div>
    <el-table :data="paginatedUserList" style="width: 100%" border>
      <el-table-column prop="userName" label="用户名" width="150"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="230"></el-table-column>
      <el-table-column prop="account" label="账号" width="210"></el-table-column>
      <el-table-column label="用户类型" width="160">
        <template slot-scope="scope">
          {{ getUserTypeText(scope.row.userType) }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <span v-if="scope.row.userType === 2" class="disabled-text">无法修改</span>
          <template v-else>
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">修改用户信息</el-button>
            <el-button type="info" size="small" @click="handleEditUserType(scope.row)">修改用户类型</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="userList.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
      style="margin-top: 20px;" 
    >
    </el-pagination>

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
            <el-option label="用户" :value="0"></el-option>
            <el-option label="管理员" :value="1"></el-option>
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
import request from '@/utils/request';

export default {
  data() {
    return {
      userList: [], // 用户列表数据
      dialogVisible: false, // 修改用户对话框是否显示
      userTypeDialogVisible: false, // 修改用户类型对话框是否显示
      currentUser: {}, // 当前修改的用户
      currentPage: 1, // 当前页码
      pageSize: 6 // 每页显示的数据条数
    };
  },
  computed: {
    // 计算当前页显示的数据
    paginatedUserList() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.userList.slice(start, end);
    }
  },
  created() {
    this.fetchUsers(); // 页面加载时获取用户数据
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await request.get('/user/all'); 
        this.userList = response.data.data; // 直接使用原始数据
        console.log('用户列表:', this.userList); // 打印用户列表
      } catch (error) {
        console.error('获取用户数据失败:', error);
      }
    },
    getUserTypeText(userType) {
      switch (userType) {
        case 0:
          return '用户';
        case 1:
          return '管理员';
        case 2:
          return '超级管理员';
        default:
          return '未知';
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
        let res = await request.post('/user/update/userInfo', this.currentUser);
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
        let res = await request.post('/user/update/userType', { 
          userId: localStorage.getItem('userId'),       // 当前登录管理员id
          changedUserId: this.currentUser.userId,      // 被修改者ID
          userType: Number(this.currentUser.userType)  // 确保是数字类型
        });
        if (res.data.code == 0) {
          this.userTypeDialogVisible = false; // 关闭对话框
          this.fetchUsers(); // 重新获取用户数据
        } else {
          this.$message.error('保存用户类型失败:' + res.data.message);
        }
      } catch (error) {
        console.error('保存用户类型失败:', error);
      }
    },
    // 处理页码变化
    handlePageChange(page) {
      this.currentPage = page;
    }
  }
};
</script>

<style scoped>
.disabled-text {
  color: #999; /* 灰色文字 */
  font-style: italic; /* 斜体 */
  cursor: not-allowed; /* 禁用鼠标指针 */
}
</style>