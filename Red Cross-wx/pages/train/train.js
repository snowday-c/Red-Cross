const app = getApp();

Page({
  data: {
    trainList: [], // 培训列表
  },

  onLoad() {
    this.fetchTrainList();
  },

  // 获取培训列表
  fetchTrainList() {
    app.request({
      url: '/train/canJoin',
      method: 'GET',
      success: (res) => {
        if (res.data.code === '0') {
          this.setData({
            trainList: res.data.data,
          });
        } else {
          wx.showToast({
            title: res.data.message || '获取培训列表失败',
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

  // 处理报名
  handleSignup(e) {
    const { trainId, trainPlace, trainTime } = e.currentTarget.dataset;
    const userId = wx.getStorageSync('userInfo').userId; // 获取用户 ID

    // 弹出确认弹窗
    wx.showModal({
      title: '确认报名',
      content: `您确定要报名 ${trainPlace} 的培训吗？`,
      success: (res) => {
        if (res.confirm) {
          // 用户点击确认
          app.request({
            url: '/train/join',
            method: 'POST',
            data: {
              userId,
              trainId,
              trainPlace,
              trainTime,
            },
            success: (res) => {
              if (res.data.code === '0') {
                wx.showToast({
                  title: '报名成功',
                  icon: 'success',
                });
                // 更新当前报名人数
                this.fetchTrainList();
              } else {
                wx.showToast({
                  title: res.data.message || '报名失败',
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
        } else if (res.cancel) {
          // 用户点击取消
          console.log('用户取消报名');
        }
      },
    });
  },

  // 返回学习页面
  handleBack() {
    wx.navigateBack({
      delta: 1, // 返回上一页
    });
  },
});