const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    score: 0, // 考试成绩
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    // 从本地缓存中获取考试成绩
    const score = wx.getStorageSync('score');
    this.setData({
      score,
    });
  },

  /**
   * 生成荣誉证书
   */
  generateCertificate() {
    // 弹出确认弹窗
    wx.showModal({
      title: '确认申请证书',
      content: '请确认您已完成线下技能培训，否则证书无法审核通过。',
      confirmText: '确认申请',
      cancelText: '取消申请',
      success: (res) => {
        if (res.confirm) {
          // 用户点击确认生成
          this.requestCertificate();
        }
      },
    });
  },

  /**
   * 向后端发送生成证书请求
   */
  requestCertificate() {
    const userId = wx.getStorageSync('userInfo').userId; // 获取用户 ID

    // 发送请求
    app.request({
      url: '/certificate/apply',
      method: 'POST',
      data: {
        userId
      },
      success: (res) => {
        if (res.data.code === '0') {
          wx.showToast({
            title: '证书申请成功',
            icon: 'success',
          });
          wx.removeStorageSync('score');
          wx.switchTab({
            url: '/pages/study/study',
          });
        } else {
          wx.showToast({
            title: res.data.message || '申请证书失败',
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
   * 回到学习页面
   */
  goToStudyPage() {
    wx.switchTab({
      url: '/pages/study/study',
    });
    wx.removeStorageSync('score');
  },
});