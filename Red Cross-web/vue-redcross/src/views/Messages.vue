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
        <!-- 公共消息表格 -->
        <el-table :data="paginatedPublicMessages" style="width: 100%" border :row-style="{height: '80px'}" :cell-style="{padding: '5px'}">
          <el-table-column prop="title" label="标题" width="150">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.title }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="内容">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.content }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="sender" label="发送者" width="120">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.sender }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="时间" width="180">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.time }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="handleEditMessage(scope.row)">修改</el-button>
              <el-button type="danger" size="small" @click="handleDeleteMessage(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <el-pagination
          background
          layout="prev, pager, next"
          :total="publicMessages.length"
          :page-size="pageSize"
          :current-page="publicCurrentPage"
          @current-change="handlePublicPageChange"
          style="margin-top: 20px;"
        >
        </el-pagination>
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
        <el-table :data="paginatedPrivateMessages" style="width: 100%" border :row-style="{height: '80px'}" :cell-style="{padding: '5px'}">
          <el-table-column prop="title" label="标题" width="150">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.title }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="内容">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.content }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="sender" label="发送者" width="120">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.sender }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="receiver" label="接收者" width="120">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.receiver }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="时间" width="180">
            <template slot-scope="scope">
              <div class="cell-content">{{ scope.row.time }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template slot-scope="scope">
              <el-button type="text" @click="handleEditMessage(scope.row)">修改</el-button>
              <el-button type="text" @click="handleDeleteMessage(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <el-pagination
          background
          layout="prev, pager, next"
          :total="privateMessages.length"
          :page-size="pageSize"
          :current-page="privateCurrentPage"
          @current-change="handlePrivatePageChange"
          style="margin-top: 20px;"
        >
        </el-pagination>
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
      pageSize: 5, // 每页显示的数据条数
      publicCurrentPage: 1, // 公共消息当前页码
      privateCurrentPage: 1, // 个人消息当前页码
    };
  },
  computed: {
    // 公共消息分页数据
    paginatedPublicMessages() {
      const start = (this.publicCurrentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.publicMessages.slice(start, end);
    },
    // 个人消息分页数据
    paginatedPrivateMessages() {
      const start = (this.privateCurrentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.privateMessages.slice(start, end);
    },
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
        // 按照 messageId 从大到小排序
        this.publicMessages = response.data.data.sort((a, b) => b.messageId - a.messageId);
      } catch (error) {
        console.error('获取公共消息失败:', error);
      }
    },
    // 获取个人消息
    async fetchPrivateMessages() {
      try {
        const response = await request.get('/message/private');
        // 按照 messageId 从大到小排序
        this.privateMessages = response.data.data.sort((a, b) => b.messageId - a.messageId);
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
    // 公共消息页码变化
    handlePublicPageChange(page) {
      this.publicCurrentPage = page;
    },
    // 个人消息页码变化
    handlePrivatePageChange(page) {
      this.privateCurrentPage = page;
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

.el-pagination {
  margin-top: 20px; /* 分页组件与表格之间的间距 */
}

/* 添加单元格内容样式 */
.cell-content {
  max-height: 70px;
  overflow-y: auto;
  word-break: break-word;
  line-height: 1.5;
}

/* 自定义滚动条样式 */
.cell-content::-webkit-scrollbar {
  width: 6px;
}

.cell-content::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

.cell-content::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>