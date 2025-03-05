<template>
  <div>
    <el-tabs v-model="activeTab" type="card">
      <!-- 公共消息 -->
      <el-tab-pane label="公共消息" name="public">
        <el-row type="flex" justify="space-between" align="middle">
          <el-col>
            <span>公共消息</span>
          </el-col>
          <el-col>
            <el-button type="primary" @click="handleAddMessage('public')">新增公共消息</el-button>
          </el-col>
        </el-row>
        <el-table :data="publicMessages" style="width: 100%" border>
          <el-table-column prop="title" label="标题" width="150"></el-table-column>
          <el-table-column prop="content" label="内容"></el-table-column>
          <el-table-column prop="sender" label="发送者" width="120"></el-table-column>
          <el-table-column prop="time" label="时间" width="180"></el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button type="text" @click="handleEditMessage(scope.row)">修改</el-button>
              <el-button type="text" @click="handleDeleteMessage(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 个人消息 -->
      <el-tab-pane label="个人消息" name="private">
        <el-row type="flex" justify="space-between" align="middle">
          <el-col>
            <span>个人消息</span>
          </el-col>
          <el-col>
            <el-button type="primary" @click="handleAddMessage('private')">新增个人消息</el-button>
          </el-col>
        </el-row>
        <el-table :data="privateMessages" style="width: 100%" border>
          <el-table-column prop="title" label="标题" width="150"></el-table-column>
          <el-table-column prop="content" label="内容"></el-table-column>
          <el-table-column prop="sender" label="发送者" width="120"></el-table-column>
          <el-table-column prop="receiver" label="接收者" width="120"></el-table-column>
          <el-table-column prop="time" label="时间" width="180"></el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button type="text" @click="handleEditMessage(scope.row)">修改</el-button>
              <el-button type="text" @click="handleDeleteMessage(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 新增/修改消息对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
      <el-form :model="currentMessage" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="currentMessage.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="currentMessage.content" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="接收者" v-if="currentMessage.messageType === 1">
          <el-input v-model="currentMessage.receiver"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMessage">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import moment from 'moment'; // 用于格式化时间
import { MessageBox } from 'element-ui'; // 引入 MessageBox 组件

export default {
  data() {
    return {
      activeTab: 'public', // 当前选中的选项卡
      publicMessages: [], // 公共消息列表
      privateMessages: [], // 个人消息列表
      dialogVisible: false, // 对话框是否显示
      dialogTitle: '', // 对话框标题
      currentMessage: { messageId: null, title: '', content: '', receiver: '', messageType: null, sender: '', time: '' }, // 当前操作的消息
    };
  },
  created() {
    this.fetchPublicMessages();
    this.fetchPrivateMessages();
  },
  methods: {
    // 获取公共消息
    async fetchPublicMessages() {
      try {
        const response = await request.get('/message/public');
        this.publicMessages = response.data.data;
      } catch (error) {
        console.error('获取公共消息失败:', error);
      }
    },
    // 获取个人消息
    async fetchPrivateMessages() {
      try {
        const response = await request.get('/message/private');
        this.privateMessages = response.data.data;
      } catch (error) {
        console.error('获取个人消息失败:', error);
      }
    },
    // 新增消息
    handleAddMessage(type) {
      const currentUser = JSON.parse(localStorage.getItem('CurrentUser')); // 从本地存储获取当前用户
      const currentTime = moment().format('YYYY-MM-DD HH:mm:ss'); // 获取当前时间

      this.currentMessage = {
        messageId: null,
        title: '',
        content: '',
        receiver: '',
        messageType: type === 'public' ? 0 : 1, // 设置消息类型
        sender: currentUser.userName, // 设置发送者
        time: currentTime, // 设置当前时间
      };

      this.dialogTitle = type === 'public' ? '新增公共消息' : '新增个人消息';
      this.dialogVisible = true;
    },
    // 修改消息
    handleEditMessage(message) {
      this.currentMessage = { ...message }; // 复制当前消息
      this.dialogTitle = message.messageType === 0 ? '修改公共消息' : '修改个人消息';
      this.dialogVisible = true;
    },
    // 保存消息（新增或修改）
    async saveMessage() {
      try {
        const url = this.currentMessage.messageId ? '/message/update' : '/message/add';
        const response = await request.post(url, this.currentMessage);
        if (response.data.code == 0) {
          this.dialogVisible = false;
          if (this.currentMessage.messageType === 0) {
            this.fetchPublicMessages(); // 刷新公共消息
          } else {
            this.fetchPrivateMessages(); // 刷新个人消息
          }
        } else {
          this.$message.error('操作失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('操作失败:', error);
        this.$message.error('操作失败: ' + error.message);
      }
    },
    // 删除消息
    async handleDeleteMessage(message) {
      try {
        // 弹出确认对话框
        await MessageBox.confirm('确定要删除该消息吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        // 用户点击确定后，发送删除请求
        const response = await request.post('/message/delete', { 
          messageId: message.messageId 
        });

        if (response.data.code == 0) {
          // 刷新消息列表
          if (message.messageType === 0) {
            this.fetchPublicMessages(); // 刷新公共消息
          } else {
            this.fetchPrivateMessages(); // 刷新个人消息
          }
          this.$message({
            type: 'success',
            message: '删除成功！'
          });
        } else {
          this.$message.error('删除失败: ' + response.data.message);
        }
      } catch (error) {
        if (error !== 'cancel') { // 用户点击了取消
          console.error('删除失败:', error);
          this.$message({
            type: 'error',
            message: '删除失败'
          });
        }
      }
    },
  },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 20px; /* 调整按钮与表格之间的间距 */
}

.el-col {
  display: flex;
  align-items: center;
}
</style>