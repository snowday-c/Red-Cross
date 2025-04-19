<template>
  <div>
    <!-- 标题和处理须知 -->
    <div style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
      <h1>用户反馈管理</h1>
      <el-tooltip placement="bottom">
        <div slot="content" style="font-size: 14px; line-height: 1.5;">
          <h4 style="font-size: 16px; margin-top: 0;">反馈处理规则：</h4>
          <ol style="padding-left: 20px;">
            <li>请认真阅读用户反馈内容，理解用户的问题或建议。</li>
            <li>对于紧急问题（如系统错误、数据丢失等），请优先处理。</li>
            <li>处理反馈时，应确保问题已得到解决或已有明确的解决方案。</li>
            <li>如需与用户进一步沟通，请使用消息系统发送个人消息。</li>
            <li>处理完成后，点击"处理"按钮将状态更改为"已处理"。</li>
            <li>对于无法解决的问题，请咨询技术支持团队。</li>
          </ol>
        </div>
        <span style="color: #909399; cursor: help; font-size: 16px; font-weight: 500;">
          <i class="el-icon-info"></i> 处理须知
        </span>
      </el-tooltip>
    </div>
    
    <!-- 反馈列表 -->
    <el-table
      :data="paginatedFeedbacks"
      style="width: 100%"
      border
      :row-style="getRowStyle"
      :cell-style="{padding: '5px'}"
    >
      <el-table-column prop="adviceSender" label="用户名" width="120">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.adviceSender }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="adviceContent" label="反馈内容">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.adviceContent }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="adviceTime" label="提交时间" width="180">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.adviceTime }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="adviceType" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.adviceType === '未处理' ? 'danger' : 'success'">
            {{ scope.row.adviceType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="adviceHandler" label="处理人" width="120">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.adviceHandler || '未处理' }}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.adviceType === '未处理'"
            type="primary"
            size="small"
            @click="handleFeedback(scope.row)"
          >
            处理
          </el-button>
          <span v-else>已处理</span>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页组件 -->
    <el-pagination
      style="margin-top: 20px;"
      background
      layout="prev, pager, next"
      :total="feedbacks.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
    ></el-pagination>
    
    <!-- 处理确认对话框 -->
    <el-dialog
      title="确认处理反馈"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <p>确定将此反馈标记为已处理吗？</p>
      <p>反馈内容：{{ currentFeedback.adviceContent }}</p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmHandle">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import { MessageBox } from 'element-ui';

export default {
  data() {
    return {
      feedbacks: [], // 所有反馈列表
      currentPage: 1, // 当前页码
      pageSize: 5, // 每页显示的数据条数
      dialogVisible: false, // 处理确认对话框显示状态
      currentFeedback: {}, // 当前处理的反馈
      user: null, // 当前用户
    };
  },
  computed: {
    // 分页后的反馈数据
    paginatedFeedbacks() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.feedbacks.slice(start, end);
    },
  },
  created() {
    this.getCurrentUser();
    this.fetchFeedbacks();
  },
  methods: {
    // 获取当前用户信息
    getCurrentUser() {
      const userString = localStorage.getItem('CurrentUser');
      if (userString) {
        this.user = JSON.parse(userString);
      }
    },
    
    // 获取所有反馈
    async fetchFeedbacks() {
      try {
        const response = await request.get('/advice/all');
        if (response.data.code === '0') {
          // 对反馈进行排序，未处理的在前面
          this.feedbacks = response.data.data.sort((a, b) => {
            // 首先按状态排序（未处理在前）
            if (a.adviceType !== b.adviceType) {
              return a.adviceType === '未处理' ? -1 : 1;
            }
            // 其次按提交时间排序（新的在前）
            return new Date(b.adviceTime) - new Date(a.adviceTime);
          });
        } else {
          this.$message.error('获取反馈列表失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('获取反馈列表失败:', error);
        this.$message.error('获取反馈列表失败: ' + error.message);
      }
    },
    
    // 处理反馈
    handleFeedback(feedback) {
      this.currentFeedback = feedback;
      this.dialogVisible = true;
    },
    
    // 确认处理反馈
    async confirmHandle() {
      try {
        const requestData = {
          adviceId: this.currentFeedback.adviceId,
          adviceType: '已处理',
          adviceHandler: this.user.userName,
        };
        
        const response = await request.post('/advice/updateType', requestData);
        if (response.data.code === '0') {
          this.$message.success('反馈已处理');
          this.dialogVisible = false;
          this.fetchFeedbacks(); // 重新加载反馈列表
        } else {
          this.$message.error('处理反馈失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('处理反馈失败:', error);
        this.$message.error('处理反馈失败: ' + error.message);
      }
    },
    
    // 处理页码变化
    handlePageChange(page) {
      this.currentPage = page;
    },
    
    // 获取行样式
    getRowStyle(row) {
      if (row.row.adviceType === '未处理') {
        return {
          backgroundColor: '#fef0f0',
          height: '80px'
        };
      }
      return {
        height: '80px'
      };
    }
  }
};
</script>

<style scoped>
h1 {
  margin-bottom: 20px;
  margin-top: 0;
}

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

.el-pagination {
  margin-top: 20px;
}
</style>