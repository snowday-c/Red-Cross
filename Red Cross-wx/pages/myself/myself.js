// pages/myself/myself.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    avatarUrl: '/assets/default-avatar.png', // 默认头像路径
    username: '默认用户名',
    isEditingUsername: false // 控制用户名编辑状态
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 可以在这里加载用户的头像和用户名
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  changeAvatar() {
    wx.showActionSheet({
      itemList: ['修改头像', '修改用户名'],
      success: (res) => {
        if (res.tapIndex === 0) {
          this.chooseNewAvatar();
        } else if (res.tapIndex === 1) {
          this.modifyUsername();
        }
      },
      fail(res) {
        console.log(res.errMsg);
      }
    });
  },

  chooseNewAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePaths = res.tempFilePaths;
        this.setData({
          avatarUrl: tempFilePaths[0]
        });
      }
    });
  },

  modifyUsername() {
    this.setData({
      isEditingUsername: true
    });
  },

  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    });
  },

  saveUsername() {
    this.setData({
      isEditingUsername: false
    });
    // 在这里保存用户名到服务器或本地存储
    wx.showToast({
      title: '用户名已保存',
      icon: 'success'
    });
  },

  navigateToHistoryScores() {
    wx.navigateTo({
      url: ''
    });
  },

  navigateToCertificates() {
    wx.navigateTo({
      url: ''
    });
  },

  navigateToVenueBookings() {
    wx.navigateTo({
      url: ''
    });
  },

  navigateToNotifications() {
    wx.navigateTo({
      url: ''
    });
  }
})