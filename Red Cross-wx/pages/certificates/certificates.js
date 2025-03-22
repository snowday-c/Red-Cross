const app = getApp();

Page({
  data: {
    certificates: [], // 证书列表
  },

  onLoad() {
    this.fetchCertificates();
  },

  // 获取证书数据
  fetchCertificates() {
    const userId = wx.getStorageSync('userInfo').userId;
    app.request({
      url: '/certificate/list/user',
      method: 'POST',
      data: {
        userId,
      },
      success: (res) => {
        if (res.data.code === '0') {
          this.setData({
            certificates: res.data.data,
          });
        } else {
          wx.showToast({
            title: res.data.message || '获取证书失败',
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
});