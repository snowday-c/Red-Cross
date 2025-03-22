<template>
  <div>
    <!-- 添加题目按钮和题型筛选 -->
    <div style="margin-bottom: 20px;">
      <el-button type="primary" @click="openAddDialog">添加题目</el-button>
      <el-select v-model="selectedQuestionType" placeholder="请选择题型" @change="handleTypeChange" style="margin-left: 10px;">
        <el-option label="全部" :value="null"></el-option>
        <el-option label="选择题" :value="1"></el-option>
        <el-option label="判断题" :value="2"></el-option>
        <el-option label="填空题" :value="3"></el-option>
      </el-select>
    </div>

    <!-- 题目表格 -->
    <el-table :data="paginatedQuestions" style="width: 100%" border>
      <!-- 移除题目ID列 -->
      <el-table-column prop="questionType" label="题型" width="120">
        <template slot-scope="scope">
          {{ getQuestionTypeName(scope.row.questionType) }}
        </template>
      </el-table-column>
      <el-table-column prop="question" label="题目内容"></el-table-column>
      <el-table-column prop="answer" label="答案"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteQuestion(scope.row.questionId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="questions.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
      style="margin-top: 20px;"
    >
    </el-pagination>

    <!-- 添加题目对话框 -->
    <el-dialog title="添加题目" :visible.sync="addDialogVisible" width="30%">
      <el-form :model="newQuestion" label-width="80px">
        <el-form-item label="题型">
          <el-select v-model="newQuestion.questionType" placeholder="请选择题型">
            <el-option label="选择题" :value="1"></el-option>
            <el-option label="判断题" :value="2"></el-option>
            <el-option label="填空题" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目">
          <el-input v-model="newQuestion.question" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="答案">
          <el-input v-model="newQuestion.answer"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addQuestion">确定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑题目对话框 -->
    <el-dialog title="编辑题目" :visible.sync="editDialogVisible" width="30%">
      <el-form :model="currentQuestion" label-width="80px">
        <el-form-item label="题型">
          <el-select v-model="currentQuestion.questionType" placeholder="请选择题型">
            <el-option label="选择题" :value="1"></el-option>
            <el-option label="判断题" :value="2"></el-option>
            <el-option label="填空题" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目">
          <el-input v-model="currentQuestion.question" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="答案">
          <el-input v-model="currentQuestion.answer"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateQuestion">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import { MessageBox } from 'element-ui'; // 引入 MessageBox 组件

export default {
  data() {
    return {
      questions: [], // 题目列表
      newQuestion: { // 新增题目数据
        questionType: null,
        question: '',
        answer: ''
      },
      currentQuestion: {}, // 当前编辑的题目
      addDialogVisible: false, // 添加题目对话框显示状态
      editDialogVisible: false, // 编辑题目对话框显示状态
      selectedQuestionType: null, // 用户选择的题型
      pageSize: 5, // 每页显示的数据条数
      currentPage: 1, // 当前页码
    };
  },
  computed: {
    // 分页后的题目数据
    paginatedQuestions() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.questions.slice(start, end);
    },
  },
  created() {
    this.fetchQuestions();
  },
  methods: {
    // 获取所有题目
    async fetchQuestions() {
      try {
        const response = await request.get('/question/all');
        this.questions = response.data.data;
      } catch (error) {
        console.error('获取题目失败:', error);
      }
    },

    // 根据题型获取题目
    async fetchQuestionsByType(questionType) {
      try {
        const response = await request.post('/question/type', { questionType });
        this.questions = response.data.data;
      } catch (error) {
        console.error('获取题型题目失败:', error);
      }
    },

    // 处理题型筛选
    handleTypeChange(value) {
      if (value === null) {
        // 如果选择“全部”，获取所有题目
        this.fetchQuestions();
      } else {
        // 否则获取指定题型的题目
        this.fetchQuestionsByType(value);
      }
    },

    // 打开添加题目对话框
    openAddDialog() {
      this.newQuestion = { questionType: null, question: '', answer: '' };
      this.addDialogVisible = true;
    },

    // 添加题目
    async addQuestion() {
      try {
        await request.post('/question/add', this.newQuestion);
        this.addDialogVisible = false;
        this.fetchQuestions(); // 重新加载题目列表
      } catch (error) {
        console.error('添加题目失败:', error);
      }
    },

    // 打开编辑题目对话框
    openEditDialog(question) {
      this.currentQuestion = { ...question };
      this.editDialogVisible = true;
    },

    // 更新题目
    async updateQuestion() {
      try {
        await request.post('/question/update', this.currentQuestion);
        this.editDialogVisible = false;
        this.fetchQuestions(); // 重新加载题目列表
      } catch (error) {
        console.error('更新题目失败:', error);
      }
    },

    // 删除题目
    async deleteQuestion(questionId) {
      try {
        // 弹出确认对话框
        await MessageBox.confirm('确定要删除该题目吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        // 用户点击确定后，发送删除请求
        await request.post('/question/delete', { questionId });

        // 刷新题目列表
        this.fetchQuestions();
        this.$message({
          type: 'success',
          message: '删除成功！'
        });
      } catch (error) {
        if (error !== 'cancel') { // 用户点击了取消
          console.error('删除题目失败:', error);
          this.$message({
            type: 'error',
            message: '删除失败'
          });
        }
      }
    },

    // 获取题型名称
    getQuestionTypeName(questionType) {
      switch (questionType) {
        case 1:
          return '选择题';
        case 2:
          return '判断题';
        case 3:
          return '填空题';
        default:
          return '未知题型';
      }
    },

    // 处理页码变化
    handlePageChange(page) {
      this.currentPage = page;
    },
  }
};
</script>

<style scoped>
h1 {
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 20px; /* 分页组件与表格之间的间距 */
}
</style>