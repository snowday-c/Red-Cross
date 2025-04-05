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
      <!-- 添加试卷类型按钮 -->
      <el-button type="success" @click="openExamTypeDialog" style="margin-left: 10px;">选择试卷类型</el-button>
    </div>

    <!-- 题目表格 -->
    <el-table :data="paginatedQuestions" style="width: 100%" border :row-style="{height: '80px'}" :cell-style="{padding: '5px'}">
      <!-- 移除题目ID列 -->
      <el-table-column prop="questionType" label="题型" width="120">
        <template slot-scope="scope">
          {{ getQuestionTypeName(scope.row.questionType) }}
        </template>
      </el-table-column>
      <el-table-column prop="question" label="题目内容">
        <template slot-scope="scope">
          <div class="question-content">
            {{ scope.row.question }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="answer" label="答案">
        <template slot-scope="scope">
          <div class="answer-content">
            {{ scope.row.answer }}
          </div>
        </template>
      </el-table-column>
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
    
    <!-- 试卷类型对话框 -->
    <el-dialog title="选择试卷类型" :visible.sync="examTypeDialogVisible" width="55%" :close-on-click-modal="false">
      <div style="margin-bottom: 15px; text-align: right;">
        <el-button type="primary" @click="openAddExamTypeDialog">新增试卷类型</el-button>
      </div>
      <el-table :data="paginatedExamTypes" style="width: 100%" border>
        <el-table-column label="选择题数量" width="95">
          <template slot-scope="scope">
            {{ scope.row.choice }}
          </template>
        </el-table-column>
        <el-table-column label="判断题数量" width="95">
          <template slot-scope="scope">
            {{ scope.row.truefalse }}
          </template>
        </el-table-column>
        <el-table-column label="填空题数量" width="95">
          <template slot-scope="scope">
            {{ scope.row.blank }}
          </template>
        </el-table-column>
        <el-table-column label="每题分数" width="80">
          <template slot-scope="scope">
            {{ scope.row.score }}
          </template>
        </el-table-column>
        <el-table-column label="考试时长(分钟)" width="120">
          <template slot-scope="scope">
            {{ scope.row.time }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.current === 1 ? 'success' : 'info'">
              {{ scope.row.current === 1 ? '使用中' : '未使用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="setCurrentExamType(scope.row.examtypeId)"
              :disabled="scope.row.current === 1">
              设为当前
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 添加试卷类型分页组件 -->
      <el-pagination
        background
        layout="prev, pager, next"
        :total="examTypes.length"
        :page-size="examTypePageSize"
        :current-page="examTypeCurrentPage"
        @current-change="handleExamTypePageChange"
        style="margin-top: 20px; text-align: center;"
      >
      </el-pagination>
      
      <span slot="footer" class="dialog-footer">
        <el-button @click="examTypeDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
    
    <!-- 新增试卷类型对话框 -->
    <el-dialog title="新增试卷类型" :visible.sync="addExamTypeDialogVisible" width="30%" :close-on-click-modal="false">
      <el-form :model="newExamType" label-width="100px">
        <el-form-item label="选择题数量">
          <el-input-number v-model="newExamType.choice" :min="0" :max="50"></el-input-number>
        </el-form-item>
        <el-form-item label="判断题数量">
          <el-input-number v-model="newExamType.truefalse" :min="0" :max="50"></el-input-number>
        </el-form-item>
        <el-form-item label="填空题数量">
          <el-input-number v-model="newExamType.blank" :min="0" :max="50"></el-input-number>
        </el-form-item>
        <el-form-item label="每题分数">
          <el-input-number v-model="newExamType.score" :min="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="考试时长(分钟)">
          <el-input-number v-model="newExamType.time" :min="1" :max="180"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addExamTypeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addExamType">确定</el-button>
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
      // 新增数据
      examTypes: [], // 试卷类型列表
      examTypeDialogVisible: false, // 试卷类型对话框显示状态
      addExamTypeDialogVisible: false, // 新增试卷类型对话框显示状态
      newExamType: { // 新增试卷类型数据
        choice: 0,
        truefalse: 0,
        blank: 0,
        score: 1,
        time: 60 // 默认考试时长为60分钟
      },
      // 试卷类型分页数据
      examTypePageSize: 5, // 试卷类型每页显示的数据条数
      examTypeCurrentPage: 1, // 试卷类型当前页码
    };
  },
  computed: {
    // 分页后的题目数据
    paginatedQuestions() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.questions.slice(start, end);
    },
    // 分页后的试卷类型数据
    paginatedExamTypes() {
      const start = (this.examTypeCurrentPage - 1) * this.examTypePageSize;
      const end = start + this.examTypePageSize;
      return this.examTypes.slice(start, end);
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

    // 打开试卷类型对话框
    async openExamTypeDialog() {
      await this.fetchExamTypes();
      this.examTypeCurrentPage = 1; // 重置页码
      this.examTypeDialogVisible = true;
    },
    
    // 获取试卷类型列表
    async fetchExamTypes() {
      try {
        const response = await request.get('/question/selectAllExam');
        this.examTypes = response.data.data;
      } catch (error) {
        console.error('获取试卷类型失败:', error);
        this.$message({
          type: 'error',
          message: '获取试卷类型失败'
        });
      }
    },
    
    // 设置当前使用的试卷类型
    async setCurrentExamType(examtypeId) {
      try {
        await MessageBox.confirm('确定要将此类型设为当前使用的试卷类型吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        
        await request.post('/question/selectExam', { examtypeId });
        
        // 刷新试卷类型列表
        await this.fetchExamTypes();
        
        this.$message({
          type: 'success',
          message: '设置成功！'
        });
      } catch (error) {
        if (error !== 'cancel') { // 用户点击了取消
          console.error('设置试卷类型失败:', error);
          this.$message({
            type: 'error',
            message: '设置失败'
          });
        }
      }
    },

    // 打开新增试卷类型对话框
    openAddExamTypeDialog() {
      this.newExamType = {
        choice: 0,
        truefalse: 0,
        blank: 0,
        score: 1,
        time: 60 // 默认考试时长为60分钟
      };
      this.addExamTypeDialogVisible = true;
    },
    
    // 新增试卷类型
    async addExamType() {
      try {
        // 验证输入
        if (this.newExamType.choice === 0 
        && this.newExamType.truefalse === 0 
        && this.newExamType.blank === 0) {
          this.$message({
            type: 'warning',
            message: '至少需要一种题型的数量大于0'
          });
          return;
        }
        
        const response = await request.post('/question/insertExamType', this.newExamType);
        
        // 检查返回的状态码
        if (response.data.code === '-1') {
          // 操作失败
          this.$message({
            type: 'error',
            message:  '新增试卷类型失败'
          });
          return; // 不关闭窗口，让用户修改后重试
        }

        // 操作成功才关闭窗口
        this.addExamTypeDialogVisible = false;
        
        // 刷新试卷类型列表
        await this.fetchExamTypes();
        
        this.$message({
          type: 'success',
          message: '新增试卷类型成功！'
        });
      } catch (error) {
        console.error('新增试卷类型失败:', error);
        this.$message({
          type: 'error',
          message: '新增试卷类型失败'
        });
        // 发生异常时不关闭窗口
      }
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
    
    // 处理试卷类型页码变化
    handleExamTypePageChange(page) {
      this.examTypeCurrentPage = page;
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

.question-content, .answer-content {
  max-height: 70px;
  overflow-y: auto;
  word-break: break-word;
  line-height: 1.5;
}

/* 自定义滚动条样式 */
.question-content::-webkit-scrollbar,
.answer-content::-webkit-scrollbar {
  width: 6px;
}

.question-content::-webkit-scrollbar-thumb,
.answer-content::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

.question-content::-webkit-scrollbar-track,
.answer-content::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>