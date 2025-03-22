<template>
  <div>
    <el-button type="primary" @click="handlePublish">发布培训</el-button>
    <!-- 培训列表表格 -->
    <el-table :data="paginatedTrainList" style="width: 100%" border>
      <!-- 移除培训ID列 -->
      <el-table-column prop="trainTime" label="培训时间" width="150"></el-table-column>
      <el-table-column prop="trainPlace" label="培训地点" width="150"></el-table-column>
      <el-table-column prop="trainType" label="培训状态" width="100" :formatter="formatTrainType"></el-table-column>
      <el-table-column prop="trainPeople" label="培训人数" width="100"></el-table-column>
      <el-table-column prop="currentPeople" label="当前人数" width="100"></el-table-column>
      <el-table-column prop="userIds" label="参与用户" width="200"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">修改</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="trainList.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
      style="margin-top: 20px;"
    >
    </el-pagination>

    <!-- 发布培训对话框 -->
    <el-dialog :visible.sync="publishDialogVisible" title="发布培训" width="30%">
      <el-form :model="newTrain" label-width="100px">
        <el-form-item label="培训时间">
          <el-date-picker v-model="newTrain.trainTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="培训地点">
          <el-input
            v-model="newTrain.trainPlaces"
            placeholder="请输入多个城市名称，用逗号分隔"
          ></el-input>
        </el-form-item>
        <el-form-item label="培训人数">
          <el-input v-model="newTrain.trainPeople" type="number"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="publishDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPublish">发布</el-button>
      </span>
    </el-dialog>

    <!-- 修改培训对话框 -->
    <el-dialog :visible.sync="editDialogVisible" title="修改培训" width="30%">
      <el-form :model="currentTrain" label-width="100px">
        <el-form-item label="培训时间">
          <el-date-picker v-model="currentTrain.trainTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="培训地点">
          <el-input v-model="currentTrain.trainPlace"></el-input>
        </el-form-item>
        <el-form-item label="培训状态">
          <el-select v-model="currentTrain.trainType" placeholder="请选择培训状态">
            <el-option label="已结束" :value="-1"></el-option>
            <el-option label="报名中" :value="0"></el-option>
            <el-option label="进行中" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="培训人数">
          <el-input v-model="currentTrain.trainPeople" type="number"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import dayjs from 'dayjs';
import { MessageBox } from 'element-ui'; // 引入 MessageBox 组件

export default {
  data() {
    return {
      trainList: [], // 培训列表数据
      publishDialogVisible: false, // 发布培训对话框是否显示
      editDialogVisible: false, // 修改培训对话框是否显示
      newTrain: { // 新培训信息
        trainTime: '',
        trainPlaces: '', // 用户输入的多个城市名称（字符串）
        trainPeople: 0
      },
      currentTrain: {}, // 当前修改的培训信息
      pageSize: 5, // 每页显示的数据条数
      currentPage: 1, // 当前页码
    };
  },
  computed: {
    // 分页后的培训列表数据
    paginatedTrainList() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.trainList.slice(start, end);
    },
  },
  created() {
    this.fetchTrainList(); // 页面加载时获取培训列表
  },
  methods: {
    // 获取培训列表
    async fetchTrainList() {
      try {
        const response = await request.get('/train/all');
        // 按照 trainId 从大到小排序
        this.trainList = response.data.data.sort((a, b) => b.trainId - a.trainId);
      } catch (error) {
        console.error('获取培训列表失败:', error);
      }
    },
    // 格式化培训状态
    formatTrainType(row, column, cellValue) {
      switch (cellValue) {
        case -1:
          return '已结束';
        case 0:
          return '报名中';
        case 1:
          return '进行中';
        default:
          return '未知状态';
      }
    },
    // 打开发布培训对话框
    handlePublish() {
      this.newTrain = {
        trainTime: '',
        trainPlaces: '', // 清空输入
        trainPeople: 0
      };
      this.publishDialogVisible = true;
    },
    // 确认发布培训
    async confirmPublish() {
      try {
        // 使用 dayjs 格式化时间为 YYYY-MM-DD HH:mm:ss
        const trainTime = dayjs(this.newTrain.trainTime).format('YYYY-MM-DD HH:mm:ss');

        // 将用户输入的多个城市名称拆分为数组
        const trainPlaces = this.newTrain.trainPlaces
          .split(/[，,]/) // 同时支持中文逗号和英文逗号
          .map(place => place.trim()) // 去除空格
          .filter(place => place.length > 0); // 过滤空值

        // 构造请求数据
        const requestData = {
          trainTime: trainTime, // 格式化时间
          trainPeople: this.newTrain.trainPeople,
          trainPlaces: trainPlaces // 转换为数组
        };

        // 发送请求
        await request.post('/train/publish', requestData);

        // 关闭对话框并刷新培训列表
        this.publishDialogVisible = false;
        this.fetchTrainList();
      } catch (error) {
        console.error('发布培训失败:', error);
      }
    },
    // 打开修改培训对话框
    handleEdit(train) {
      this.currentTrain = { ...train }; // 复制培训数据
      this.editDialogVisible = true;
    },
    // 确认修改培训
    async confirmEdit() {
      try {
        await request.post('/train/update', this.currentTrain);
        this.editDialogVisible = false;
        this.fetchTrainList(); // 重新获取培训列表
      } catch (error) {
        console.error('修改培训失败:', error);
      }
    },
    // 删除培训
    async handleDelete(train) {
      try {
        // 弹出确认对话框
        await MessageBox.confirm('确定要删除该培训吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        // 用户点击确定后，发送删除请求
        await request.post('/train/delete', { trainId: train.trainId });

        // 刷新培训列表
        this.fetchTrainList();
        this.$message({
          type: 'success',
          message: '删除成功！'
        });
      } catch (error) {
        if (error !== 'cancel') { // 用户点击了取消
          console.error('删除培训失败:', error);
          this.$message({
            type: 'error',
            message: '删除失败'
          });
        }
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
  text-align: center;
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 20px; /* 分页组件与表格之间的间距 */
}
</style>