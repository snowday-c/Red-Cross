Page({
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: null, // 用户信息
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 从本地存储中获取用户信息
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo,
    });
  },

  /**
   * 检查用户是否登录
   */
  checkLogin() {
    if (!this.data.userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none',
      });
      return false;
    }
    return true;
  },

  /**
   * 跳转到考试页面
   */
  navigateToExam() {
    if (!this.checkLogin()) return; // 检查用户是否登录
    wx.navigateTo({
      url: '/pages/exam/exam',
    });
  },

  /**
   * 跳转到培训页面
   */
  navigateToTrain() {
    if (!this.checkLogin()) return; // 检查用户是否登录
    wx.navigateTo({
      url: '/pages/train/train',
    });
  },
});