<template>
  <div class="score-analysis-container">
    <h1>成绩分析</h1>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-container">
          <h3>成绩分布饼图</h3>
          <div ref="pieChart" class="chart"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-container">
          <h3>成绩分布柱状图</h3>
          <div ref="barChart" class="chart"></div>
        </div>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <div class="statistics-container">
          <h3>统计数据</h3>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card shadow="hover">
                <div slot="header">
                  <span>平均分</span>
                </div>
                <div class="statistic-value">{{ averageScore.toFixed(2) }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <div slot="header">
                  <span>最高分</span>
                </div>
                <div class="statistic-value">{{ maxScore }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <div slot="header">
                  <span>最低分</span>
                  <el-tooltip content="不包括0分" placement="top">
                    <i class="el-icon-info" style="color: #909399; font-size: 16px; margin-left: 5px;"></i>
                  </el-tooltip>
                </div>
                <div class="statistic-value">
                  {{ minScore }}
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <div slot="header">
                  <span>合格率</span>
                </div>
                <div class="statistic-value">{{ passRate.toFixed(2) }}%</div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    
    <el-row style="margin-top: 20px;">
      <el-col :span="24">
        <div class="table-container">
          <h3>成绩详情</h3>
          
          <div style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <el-input
                v-model="searchUserName"
                placeholder="请输入用户名筛选"
                style="width: 200px; margin-right: 10px;"
                clearable
                @clear="resetFilter"
              ></el-input>
              <el-button type="primary" @click="filterScores">筛选</el-button>
            </div>
          </div>
          
          <el-table :data="paginatedScores" border style="width: 100%">
            <el-table-column prop="exam_id" label="考试ID" width="80"></el-table-column>
            <el-table-column prop="user_name" label="用户名" width="120"></el-table-column>
            <el-table-column prop="score" label="分数" width="80">
              <template slot-scope="scope">
                <span :style="{ color: getScoreColor(scope.row.score) }">{{ scope.row.score }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="exam_time" label="考试时间" width="180">
              <template slot-scope="scope">
                {{ formatDateTime(scope.row.exam_time) }}
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.score >= 80 ? 'success' : 'danger'">
                  {{ scope.row.score >= 80 ? '合格' : '不合格' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            style="margin-top: 20px; text-align: center;"
            background
            layout="prev, pager, next"
            :total="filteredScores.length"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
          ></el-pagination>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '@/utils/request';
import * as echarts from 'echarts';
import moment from 'moment';

export default {
  data() {
    return {
      scores: [], // 成绩列表
      filteredScores: [], // 筛选后的成绩列表
      searchUserName: '', // 用户名搜索
      pageSize: 5, // 每页显示的数据条数
      currentPage: 1, // 当前页码
      pieChart: null,
      barChart: null
    };
  },
  computed: {
    // 分页后的成绩数据
    paginatedScores() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredScores.slice(start, end);
    },
    // 计算平均分
    averageScore() {
      if (this.scores.length === 0) return 0;
      const sum = this.scores.reduce((acc, curr) => acc + curr.score, 0);
      return sum / this.scores.length;
    },
    // 计算最高分
    maxScore() {
      if (this.scores.length === 0) return 0;
      return Math.max(...this.scores.map(item => item.score));
    },
    // 计算最低分
    minScore() {
      if (this.scores.length === 0) return 0;
      const nonZeroScores = this.scores.filter(item => item.score > 0);
      return nonZeroScores.length > 0 ? Math.min(...nonZeroScores.map(item => item.score)) : 0;
    },
    // 计算合格率 (80分以上为合格)
    passRate() {
      if (this.scores.length === 0) return 0;
      const passCount = this.scores.filter(item => item.score >= 80).length;
      return (passCount / this.scores.length) * 100;
    },
    // 成绩分布数据 - 按照新的分类标准
    scoreDistribution() {
      // 定义分数段
      const ranges = [
        { name: '60分以下', min: 0, max: 59 },
        { name: '60-80分', min: 60, max: 79 },
        { name: '80-100分', min: 80, max: 100 },
        { name: '100-120分', min: 101, max: 120 }
      ];
      
      // 统计各分数段的人数
      const distribution = ranges.map(range => {
        const count = this.scores.filter(item => 
          item.score >= range.min && item.score <= range.max
        ).length;
        
        return {
          name: range.name,
          value: count
        };
      });
      
      return distribution;
    }
  },
  created() {
    this.fetchScores();
  },
  mounted() {
    // 在组件挂载后初始化图表
    this.$nextTick(() => {
      this.initCharts();
    });
  },
  methods: {
    // 获取成绩列表
    async fetchScores() {
      try {
        const response = await request.get('/question/getAllGrade');
        if (response.data.code === '0') {
          this.scores = response.data.data;
          this.filteredScores = [...this.scores];
          // 更新图表
          this.updateCharts();
        } else {
          this.$message.error('获取成绩列表失败: ' + response.data.message);
        }
      } catch (error) {
        console.error('获取成绩列表失败:', error);
        this.$message.error('获取成绩列表失败: ' + error.message);
      }
    },
    
    // 初始化图表
    initCharts() {
      // 初始化饼图
      this.pieChart = echarts.init(this.$refs.pieChart);
      
      // 初始化柱状图
      this.barChart = echarts.init(this.$refs.barChart);
      
      // 窗口大小变化时，重新调整图表大小
      window.addEventListener('resize', () => {
        this.pieChart.resize();
        this.barChart.resize();
      });
      
      // 更新图表数据
      this.updateCharts();
    },
    
    // 更新图表数据
    updateCharts() {
      if (!this.pieChart || !this.barChart) return;
      
      // 饼图-分类标准
      this.pieChart.setOption({
        title: {
          text: '成绩分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}人次 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: this.scoreDistribution.map(item => item.name)
        },
        series: [
          {
            name: '成绩分布',
            type: 'pie',
            radius: '60%',
            center: ['50%', '50%'],
            data: this.scoreDistribution,
            emphasis: {
              scale: false,  // 取消放大效果
              itemStyle: {
                shadowBlur: 5,  // 减小阴影模糊半径
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.3)'  // 减小阴影不透明度
              }
            },
            itemStyle: {
              color: function(params) {
                // 根据分数段设置不同的颜色
                const colors = ['#FF5252', '#FFA726', '#66BB6A', '#42A5F5'];
                return colors[params.dataIndex];
              }
            },
            label: {
              show: true,
              position: 'inside',
              formatter: '{d}%',
              fontSize: 14,
              fontWeight: 'bold',
              color: '#fff'
            }
          }
        ]
      });
      
      // 柱状图-分类标准
      this.barChart.setOption({
        title: {
          text: '成绩分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: '{b}: {c}人次'
        },
        xAxis: {
          type: 'category',
          data: this.scoreDistribution.map(item => item.name)
        },
        yAxis: {
          type: 'value',
          name: '人次'
        },
        series: [
          {
            name: '人次',
            type: 'bar',
            data: this.scoreDistribution.map(item => item.value),
            itemStyle: {
              color: function(params) {
                // 根据分数段设置不同的颜色
                const colors = ['#FF5252', '#FFA726', '#66BB6A', '#42A5F5'];
                return colors[params.dataIndex];
              }
            },
            label: {
              show: true,
              position: 'top',
              formatter: '{c}人次'
            }
          }
        ]
      });
    },
    
    // 处理页码变化
    handlePageChange(page) {
      this.currentPage = page;
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      return moment(dateTime).format('YYYY-MM-DD HH:mm:ss');
    },
    
    // 合格标准
    getScoreColor(score) {
      if (score >= 100) return '#42A5F5'; // 蓝色
      if (score >= 80) return '#66BB6A';  // 绿色
      if (score >= 60) return '#FFA726';  // 橙色
      return '#FF5252';                   // 红色
    },
    
    // 筛选成绩
    filterScores() {
      if (!this.searchUserName) {
        this.filteredScores = [...this.scores];
      } else {
        this.filteredScores = this.scores.filter(
          score => score.user_name.includes(this.searchUserName)
        );
      }
      this.currentPage = 1;
    },
    
    // 重置筛选
    resetFilter() {
      this.searchUserName = '';
      this.filteredScores = [...this.scores];
      this.currentPage = 1;
    }
  }
};
</script>

<style scoped>
.score-analysis-container {
  padding: 20px;
}

.chart-container, .statistics-container, .table-container {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.chart {
  height: 400px;
  width: 100%;
}

h1 {
  margin-bottom: 20px;
}

h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 500;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
}

.el-card {
  margin-bottom: 20px;
}
</style>