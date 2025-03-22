<template>
  <div>
    <!-- 等待审核证书 -->
    <h2>等待审核证书</h2>
    <el-table :data="paginatedWaitCertificates" style="width: 100%" border>
      <el-table-column prop="certificateId" label="证书ID" width="100"></el-table-column>
      <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>

      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="openReviewDialog(scope.row)">通过审核</el-button>
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
    <h2>已发放证书</h2>
    <el-input
      v-model="searchUserId"
      placeholder="请输入用户ID筛选"
      style="width: 200px; margin-bottom: 20px;"
      clearable
      @clear="fetchApprovedCertificates"
    ></el-input>
    <el-button type="primary" @click="fetchApprovedCertificates">筛选</el-button>

    <el-table :data="paginatedApprovedCertificates" style="width: 100%" border>
      <el-table-column prop="certificateId" label="证书ID" width="100"></el-table-column>
      <el-table-column prop="userId" label="用户ID" width="100"></el-table-column>
      <el-table-column prop="certificateTitle" label="证书标题" width="150"></el-table-column>
      <el-table-column prop="certificateContent" label="证书内容"></el-table-column>
      <el-table-column prop="certificateTime" label="发放时间" width="180"></el-table-column>
      <el-table-column prop="approver" label="审核人" width="120"></el-table-column>
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

    <!-- 审核对话框 -->
    <el-dialog :visible.sync="reviewDialogVisible" :title="reviewDialogTitle" width="30%">
      <el-form :model="currentCertificate" label-width="100px">
        <el-form-item label="证书标题">
          <el-input v-model="currentCertificate.certificateTitle"></el-input>
        </el-form-item>
        <el-form-item label="证书内容">
          <el-input v-model="currentCertificate.certificateContent" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="approveCertificate">确定</el-button>
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
      user: null,
      certificateList: [], // 等待审核证书列表
      approvedCertificates: [], // 已发放证书列表
      searchUserId: '', // 用户ID筛选
      reviewDialogVisible: false, // 审核对话框显示状态
      reviewDialogTitle: '审核证书', // 审核对话框标题
      currentCertificate: {
        certificateId: null,
        userId: null,
        certificateTitle: '',
        certificateContent: '',
        approver: ''
      },
      pageSize: 5, // 每页显示的数据条数
      waitCurrentPage: 1, // 等待审核证书当前页码
      approvedCurrentPage: 1, // 已发放证书当前页码
    };
  },
  created() {
    this.fetchWaitCertificates();
    this.fetchApprovedCertificates();

    const userString = localStorage.getItem('CurrentUser');
    if (userString) {
      this.user = JSON.parse(userString);
    }
  },
  computed: {
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
  methods: {
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
    // 打开审核对话框
    openReviewDialog(certificate) {
      this.currentCertificate = {
        ...certificate,
        certificateTitle: '',
        certificateContent: '',
        approver: this.user.userName
      };
      this.reviewDialogVisible = true;
    },
    // 通过审核
    async approveCertificate() {
      try {
        const response = await request.post('/certificate/approve', this.currentCertificate);
        if (response.data.code == 0) {
          this.$message({
            type: 'success',
            message: '审核通过！'
          });
          this.reviewDialogVisible = false;
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
        this.currentCertificate = {
          ...certificate,
          certificateTitle: '',
          certificateContent: '',
          approver: this.user.userName
        };

        const response = await request.post('/certificate/reject', this.currentCertificate);

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
  margin-top: 30px;
  margin-bottom: 20px;
}
</style>