<template>
  <div>
    <!-- 等待审核证书 -->
    <div style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
      <h2>等待审核证书</h2>
      <el-tooltip placement="bottom">
        <div slot="content" style="font-size: 14px; line-height: 1.5;">
          <strong style="font-size: 16px;">红十字救生员证书审核标准：</strong><br/>
          1.检查申请人是否已经具有未过期限(1年)的证书<br/>
          2.核对申请人是否具有最近一年内的培训签到记录
        </div>
        <span style="color: #909399; cursor: help; font-size: 16px; font-weight: 500;">
          <i class="el-icon-info"></i> 审核标准
        </span>
      </el-tooltip>
    </div>
    <el-table :data="paginatedWaitCertificates" style="width: 100%" border :row-style="{height: '80px'}" :cell-style="{padding: '5px'}">
      <el-table-column prop="userId" label="用户ID" width="100">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.userId }}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="confirmApprove(scope.row)">通过审核</el-button>
          <el-button type="danger" size="small" @click="confirmReject(scope.row)">拒绝审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 等待审核证书分页 -->
    <el-pagination
      style="margin-top: 20px;"
      background
      layout="prev, pager, next"
      :total="certificateList.length"
      :page-size="pageSize"
      :current-page="waitCurrentPage"
      @current-change="handleWaitPageChange"
    ></el-pagination>

    <!-- 已发放证书 -->
    <div style="margin-top: 30px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
      <h2>已发放证书</h2>
      <div>
        <el-input
          v-model="searchUserId"
          placeholder="请输入用户ID筛选"
          style="width: 200px; margin-right: 10px;"
          clearable
          @clear="fetchApprovedCertificates"
        ></el-input>
        <el-button type="primary" @click="fetchApprovedCertificates">筛选</el-button>
      </div>
    </div>

    <el-table :data="paginatedApprovedCertificates" style="width: 100%" border :row-style="{height: '80px'}" :cell-style="{padding: '5px'}">
      <el-table-column prop="userId" label="用户ID" width="100">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.userId }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="certificateTitle" label="证书标题" width="150">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.certificateTitle }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="certificateContent" label="证书内容">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.certificateContent }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="certificateTime" label="发放时间" width="180">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.certificateTime }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="approver" label="审核人" width="120">
        <template slot-scope="scope">
          <div class="cell-content">{{ scope.row.approver }}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" v-if="isSuperAdmin">
        <template slot-scope="scope">
          <el-button 
            type="danger" 
            size="small" 
            @click="confirmDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 已发放证书分页 -->
    <el-pagination
      style="margin-top: 20px;"
      background
      layout="prev, pager, next"
      :total="filteredApprovedCertificates.length"
      :page-size="pageSize"
      :current-page="approvedCurrentPage"
      @current-change="handleApprovedPageChange"
    ></el-pagination>
    
  </div>
</template>

<script>
import request from '@/utils/request';
import { MessageBox } from 'element-ui';

export default {
  data() {
    return {
      user: null,
      certificateList: [], // 等待审核证书列表
      approvedCertificates: [], // 已发放证书列表
      searchUserId: '', // 用户ID筛选
      pageSize: 5, // 每页显示的数据条数
      waitCurrentPage: 1, // 等待审核证书当前页码
      approvedCurrentPage: 1, // 已发放证书当前页码
      ruleDialogVisible: false // 审核规则对话框显示状态
    };
  },
  computed: {
    // 判断当前用户是否是超级管理员
    isSuperAdmin() {
      return this.user && this.user.userType === 2;
    },
    // 筛选后的已发放证书列表
    filteredApprovedCertificates() {
      if (!this.searchUserId) {
        return this.approvedCertificates;
      }
      return this.approvedCertificates.filter(
        certificate => certificate.userId == this.searchUserId
      );
    },
    // 分页后的等待审核证书列表
    paginatedWaitCertificates() {
      const start = (this.waitCurrentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.certificateList.slice(start, end);
    },
    // 分页后的已发放证书列表
    paginatedApprovedCertificates() {
      const start = (this.approvedCurrentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredApprovedCertificates.slice(start, end);
    }
  },
  created() {
    this.fetchWaitCertificates();
    this.fetchApprovedCertificates();

    const userString = localStorage.getItem('CurrentUser');
    if (userString) {
      this.user = JSON.parse(userString);
    }
  },
  methods: {
    // 显示审核规则对话框
    showRuleDialog() {
      this.ruleDialogVisible = true;
    },
    // 获取等待审核证书列表
    async fetchWaitCertificates() {
      try {
        const response = await request.get('/certificate/list/wait');
        // 按照 certificateId 从大到小排序
        this.certificateList = response.data.data.sort((a, b) => b.certificateId - a.certificateId);
      } catch (error) {
        console.error('获取等待审核的证书列表失败:', error);
      }
    },
    // 获取已发放证书列表
    async fetchApprovedCertificates() {
      try {
        const response = await request.get('/certificate/list/approved');
        // 按照 certificateId 从大到小排序
        this.approvedCertificates = response.data.data.sort((a, b) => b.certificateId - a.certificateId);
      } catch (error) {
        console.error('获取已发放的证书列表失败:', error);
      }
    },
    // 确认通过审核
    confirmApprove(certificate) {
      MessageBox.confirm('确定要通过该证书申请吗？', '确认审批', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.approveCertificate(certificate);
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消审批操作'
          });
        });
    },
    // 通过审核
    async approveCertificate(certificate) {
      try {
        const requestData = {
          certificateId: certificate.certificateId,
          userId: certificate.userId,
          approver: this.user.userName
        };

        const response = await request.post('/certificate/approve', requestData);
        if (response.data.code == 0) {
          this.$message({
            type: 'success',
            message: '审核通过！'
          });
          this.fetchWaitCertificates();
          this.fetchApprovedCertificates();
        } else {
          this.$message.error('审核失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('审核失败:', error);
        this.$message.error('审核失败: ' + error.message);
      }
    },
    // 确认拒绝审核
    confirmReject(certificate) {
      MessageBox.confirm('确定要拒绝该证书申请吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.rejectCertificate(certificate);
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消拒绝操作'
          });
        });
    },
    // 拒绝审核
    async rejectCertificate(certificate) {
      try {
        const requestData = {
          certificateId: certificate.certificateId,
          userId: certificate.userId,
          approver: this.user.userName
        };

        const response = await request.post('/certificate/reject', requestData);

        if (response.data.code == 0) {
          this.$message({
            type: 'success',
            message: '拒绝审核成功！'
          });
          this.fetchWaitCertificates();
        } else {
          this.$message.error('拒绝审核失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('拒绝审核失败:', error);
        this.$message.error('拒绝审核失败: ' + error.message);
      }
    },
    // 确认删除证书
    confirmDelete(certificate) {
      MessageBox.confirm('确定要删除该证书吗？此操作不可恢复！', '警告', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button--danger'
      })
        .then(() => {
          this.deleteCertificate(certificate);
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除操作'
          });
        });
    },
    // 删除证书
    async deleteCertificate(certificate) {
      try {
        const response = await request.post('/certificate/delete', {
          certificateId: certificate.certificateId
        });
        if (response.data.code == 0) {
          this.$message({
            type: 'success',
            message: '证书删除成功！'
          });
          this.fetchApprovedCertificates();
        } else {
          this.$message.error('删除失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('删除证书失败:', error);
        this.$message.error('删除证书失败: ' + error.message);
      }
    },
    // 处理等待审核证书分页变化
    handleWaitPageChange(page) {
      this.waitCurrentPage = page;
    },
    // 处理已发放证书分页变化
    handleApprovedPageChange(page) {
      this.approvedCurrentPage = page;
    }
  }
};
</script>

<style scoped>
h1 {
  margin-top: 0px;
}
h2 {
  margin-top: 0;
  margin-bottom: 0;
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
</style>