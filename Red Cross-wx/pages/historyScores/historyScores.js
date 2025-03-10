const app = getApp();

Page({
  /**
   * 页面的初始数据
   */
  data: {
    historyScores: [], // 历史成绩列表
    expandedExamId: null, // 当前展开的考试 ID
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    this.fetchHistoryScores();
  },

  /**
   * 获取历史成绩
   */
  fetchHistoryScores() {
    const userId = wx.getStorageSync('userInfo').userId; // 获取用户 ID
    app.request({
      url: '/question/getUserExam',
      method: 'POST',
      data: {
        userId,
      },
      success: (res) => {
        if (res.data.code === '0') {
          const historyScores = res.data.data;

          // 根据 examId 从大到小排序
          historyScores.sort((a, b) => b.examId - a.examId);

          this.setData({
            historyScores,
          });
        } else {
          wx.showToast({
            title: res.data.message || '获取历史成绩失败',
            icon: 'none',
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
        });
      },
    });
  },

  /**
   * 展开或收起考试详情
   */
  toggleExamDetail(e) {
    const examId = e.currentTarget.dataset.examId;
    const { expandedExamId } = this.data;

    // 如果当前点击的考试 ID 已经展开，则收起；否则展开
    this.setData({
      expandedExamId: expandedExamId === examId ? null : examId,
    });
  },
});