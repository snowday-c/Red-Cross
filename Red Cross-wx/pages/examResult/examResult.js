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
    wx.showToast({
      title: '荣誉证书生成中...',
      icon: 'none',
    });

    // 模拟生成荣誉证书的逻辑
    setTimeout(() => {
      wx.showToast({
        title: '荣誉证书生成成功',
        icon: 'success',
      });
      // 跳转到荣誉证书页面
      wx.redirectTo({
        url: '/pages/certificate/certificate',
      });
    }, 2000);
  },

  /**
   * 回到学习页面
   */
  goToStudyPage() {
    wx.switchTab({
      url: '/pages/study/study',
    });
  },
});