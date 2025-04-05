<template>
  <div>
    <div style="margin-bottom: 20px; display: flex; justify-content: space-between;">
      <el-button type="primary" @click="handlePublish">发布培训</el-button>
      
      <!-- 用户ID筛选 -->
      <div style="display: flex;">
        <el-input
          v-model="searchUserId"
          placeholder="输入报名用户ID筛选"
          style="width: 200px; margin-right: 10px;"
          clearable
          @clear="clearUserFilter"
        ></el-input>
        <el-button type="primary" @click="filterByUserId">筛选</el-button>
      </div>
    </div>

    <!-- 培训列表表格 -->
    <el-table :data="paginatedTrainList" style="width: 100%" border :row-style="{height: '80px'}" :cell-style="{padding: '5px'}">
      <el-table-column prop="trainTime" label="培训时间" width="160">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.trainTime }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="trainPlace" label="培训地点" width="100">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.trainPlace }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="trainType" label="培训状态" width="100" :formatter="formatTrainType"></el-table-column>
      <el-table-column prop="trainPeople" label="培训人数" width="80"></el-table-column>
      <el-table-column prop="currentPeople" label="当前人数" width="80"></el-table-column>
      <el-table-column label="报名用户" width="200">
        <template slot-scope="scope">
          <div class="cell-content">
            <span v-if="!scope.row.userIds || JSON.parse(scope.row.userIds).length === 0">无</span>
            <span v-else>
              {{ formatUserIds(scope.row.userIds) }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="签到用户" width="200">
        <template slot-scope="scope">
          <div class="cell-content">
            <span v-if="!scope.row.participateIds || JSON.parse(scope.row.participateIds).length === 0">无</span>
            <span v-else>
              {{ formatUserIds(scope.row.participateIds) }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170">
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
      :total="filteredTrainList.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
      style="margin-top: 20px;"
    ></el-pagination>

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
import { MessageBox } from 'element-ui';

export default {
  data() {
    return {
      trainList: [],
      filteredTrainList: [],
      searchUserId: '',
      publishDialogVisible: false,
      editDialogVisible: false,
      newTrain: {
        trainTime: '',
        trainPlaces: '',
        trainPeople: 0
      },
      currentTrain: {},
      pageSize: 5,
      currentPage: 1,
    };
  },
  created() {
    this.fetchTrainList();
  },
  computed: {
    paginatedTrainList() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredTrainList.slice(start, end);
    },
  },
  methods: {

    formatUserIds(idsString) {
      try {
        const idsArray = JSON.parse(idsString);
        if (idsArray.length === 0) return '';
        const sortedIds = idsArray.map(id => Number(id)).sort((a, b) => a - b);
        return sortedIds.join(', ');
      } catch (e) {
        return idsString;
      }
    },

    async fetchTrainList() {
      try {
        const response = await request.get('/train/all');
        this.trainList = response.data.data.sort((a, b) => b.trainId - a.trainId);
        this.filteredTrainList = [...this.trainList];
      } catch (error) {
        console.error('获取培训列表失败:', error);
      }
    },
    
    filterByUserId() {
      if (!this.searchUserId) {
        this.filteredTrainList = [...this.trainList];
        return;
      }
      
      this.filteredTrainList = this.trainList.filter(train => {
        try {
          const userIds = train.userIds ? JSON.parse(train.userIds) : [];
          return userIds.includes(Number(this.searchUserId));
        } catch (e) {
          console.error('解析userIds失败:', e);
          return false;
        }
      });
      
      this.currentPage = 1;
    },
    
    clearUserFilter() {
      this.filteredTrainList = [...this.trainList];
      this.currentPage = 1;
      this.searchUserId = '';
    },
    
    formatTrainType(row, column, cellValue) {
      switch (cellValue) {
        case -1: return '已结束';
        case 0: return '报名中';
        case 1: return '进行中';
        default: return '未知状态';
      }
    },
    
    handlePublish() {
      this.newTrain = {
        trainTime: '',
        trainPlaces: '',
        trainPeople: 0
      };
      this.publishDialogVisible = true;
    },
    
    async confirmPublish() {
      try {
        const trainTime = dayjs(this.newTrain.trainTime).format('YYYY-MM-DD HH:mm:ss');
        const trainPlaces = this.newTrain.trainPlaces
          .split(/[，,]/)
          .map(place => place.trim())
          .filter(place => place.length > 0);

        await request.post('/train/publish', {
          trainTime: trainTime,
          trainPeople: this.newTrain.trainPeople,
          trainPlaces: trainPlaces
        });

        this.publishDialogVisible = false;
        this.fetchTrainList();
      } catch (error) {
        console.error('发布培训失败:', error);
      }
    },
    
    handleEdit(train) {
      this.currentTrain = { ...train };
      this.editDialogVisible = true;
    },
    
    async confirmEdit() {
      try {
        this.currentTrain.trainTime = dayjs(this.currentTrain.trainTime).format('YYYY-MM-DD HH:mm:ss');
        await request.post('/train/update', this.currentTrain);
        this.editDialogVisible = false;
        this.fetchTrainList();
      } catch (error) {
        console.error('修改培训失败:', error);
      }
    },
    
    async handleDelete(train) {
      try {
        await MessageBox.confirm('确定要删除该培训吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        await request.post('/train/delete', { trainId: train.trainId });
        this.fetchTrainList();
        this.$message.success('删除成功！');
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除培训失败:', error);
          this.$message.error('删除失败');
        }
      }
    },
    
    handlePageChange(page) {
      this.currentPage = page;
    },
  }
};
</script>

<style scoped>
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
  margin-top: 20px; /* 分页组件与表格之间的间距 */
}
</style>