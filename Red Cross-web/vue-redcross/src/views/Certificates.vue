<template>
    <div>
      <h1>证书管理</h1>
  
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
    </div>
  </template>
  
  <script>
  import request from '@/utils/request';
  import { MessageBox } from 'element-ui'; // 引入 MessageBox 组件
  
  export default {
    data() {
      return {
        certificateList: [], // 等待审核的证书列表
        approvedCertificates: [], // 已发放的证书列表
        searchUserId: '', // 用户ID筛选条件
        reviewDialogVisible: false, // 审核对话框是否显示
        reviewDialogTitle: '审核证书', // 审核对话框标题
        currentCertificate: { // 当前操作的证书
          certificateId: null,
          userId: null,
          certificateTitle: '',
          certificateContent: '',
          approver: '' // 当前管理员为审核人
        },
        pageSize: 5, // 每页显示的数据条数
        waitCurrentPage: 1, // 等待审核证书的当前页码
        approvedCurrentPage: 1 // 已发放证书的当前页码
      };
    },
    created() {
      this.fetchWaitCertificates(); // 页面加载时获取等待审核的证书列表
      this.fetchApprovedCertificates(); // 页面加载时获取已发放的证书列表
    },
    computed: {
      // 根据用户ID筛选已发放证书
      filteredApprovedCertificates() {
        if (!this.searchUserId) {
          return this.approvedCertificates; // 如果没有输入用户ID，返回全部数据
        }
        return this.approvedCertificates.filter(
          certificate => certificate.userId == this.searchUserId
        );
      },
  
      // 等待审核证书的分页数据
      paginatedWaitCertificates() {
        const start = (this.waitCurrentPage - 1) * this.pageSize;
        const end = start + this.pageSize;
        return this.certificateList.slice(start, end);
      },
  
      // 已发放证书的分页数据
      paginatedApprovedCertificates() {
        const start = (this.approvedCurrentPage - 1) * this.pageSize;
        const end = start + this.pageSize;
        return this.filteredApprovedCertificates.slice(start, end);
      }
    },
    methods: {
      // 获取等待审核的证书列表
      async fetchWaitCertificates() {
        try {
          const response = await request.get('/certificate/list/wait');
          this.certificateList = response.data.data;
        } catch (error) {
          console.error('获取等待审核的证书列表失败:', error);
        }
      },
  
      // 获取已发放的证书列表
      async fetchApprovedCertificates() {
        try {
          const response = await request.get('/certificate/list/approved');
          this.approvedCertificates = response.data.data;
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
          approver: localStorage.getItem('username')
        };
        this.reviewDialogVisible = true;
      },
  
      // 审核通过
      async approveCertificate() {
        try {
          // 发送审核请求
          const response = await request.post('/certificate/approve', this.currentCertificate);
  
          if (response.data.code == 0) {
            this.$message({
              type: 'success',
              message: '审核通过！'
            });
            this.reviewDialogVisible = false; // 关闭审核窗口
            this.fetchWaitCertificates(); // 重新加载等待审核的证书列表
            this.fetchApprovedCertificates(); // 重新加载已发放的证书列表
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
            // 用户点击确定后，执行拒绝审核
            this.rejectCertificate(certificate);
          })
          .catch(() => {
            // 用户点击取消，不执行任何操作
            this.$message({
              type: 'info',
              message: '已取消拒绝操作'
            });
          });
      },
  
      // 拒绝审核
      async rejectCertificate(certificate) {
        try {
          // 设置当前操作的证书
          this.currentCertificate = {
            ...certificate,
            certificateTitle: '',
            certificateContent: '',
            approver: localStorage.getItem('username')
          };
  
          // 发送拒绝审核请求
          const response = await request.post('/certificate/reject', this.currentCertificate);
  
          if (response.data.code == 0) {
            this.$message({
              type: 'success',
              message: '拒绝审核成功！'
            });
            this.fetchWaitCertificates(); // 重新加载等待审核的证书列表
          } else {
            this.$message.error('拒绝审核失败: ' + response.data.message);
          }
        } catch (error) {
          console.error('拒绝审核失败:', error);
          this.$message.error('拒绝审核失败: ' + error.message);
        }
      },
  
      // 等待审核证书分页切换
      handleWaitPageChange(page) {
        this.waitCurrentPage = page;
      },
  
      // 已发放证书分页切换
      handleApprovedPageChange(page) {
        this.approvedCurrentPage = page;
      }
    }
  };
  </script>
  
  <style scoped>
  h1 {
    margin-bottom: 20px;
  }
  h2 {
    margin-top: 30px;
    margin-bottom: 20px;
  }
  </style>