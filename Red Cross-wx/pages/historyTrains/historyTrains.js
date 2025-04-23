const app = getApp();

Page({
  data: {
    trainRecords: [], // 用户培训记录
    showTip: false, // 是否显示提示
    tipContent: '' // 提示内容
  },

  onLoad() {
    this.fetchTrainRecords();
  },

  // 获取用户培训记录
  fetchTrainRecords() {
    const userId = wx.getStorageSync('userInfo').userId; // 获取用户 ID
    app.request({
      url: '/train/historyTrain', // 替换为实际接口地址
      method: 'POST',
      data: {
        userId,
      },
      success: (res) => {
        if (res.data.code === '0') {
          // 是否可以取消报名
          const trainRecords = res.data.data.map(item => {
            return {
              ...item,
              canCancel: this.isBeforeTrainTime(item.trainTime), // 判断是否可以取消报名
              participateTypeText: item.participateType === '0' ? '未签到' : '已签到' // 转换状态为文字
            };
          });
          this.setData({
            trainRecords,
          });
        } else {
          wx.showToast({
            title: res.data.message || '获取培训记录失败',
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

  // 判断当前时间是否在培训时间之前
  isBeforeTrainTime(trainTime) {
    const currentTime = new Date().getTime(); // 当前时间戳
    const trainTimeStamp = new Date(trainTime).getTime(); // 培训时间戳
    return currentTime < trainTimeStamp; // 当前时间早于培训时间
  },

  // 处理取消报名
  handleCancel(e) {
    const trainId = e.currentTarget.dataset.trainId;
    const userId = wx.getStorageSync('userInfo').userId; // 获取用户 ID

    // 弹出确认弹窗
    wx.showModal({
      title: '确认取消',
      content: '您确定要取消报名吗？',
      success: (res) => {
        if (res.confirm) {
          // 用户点击确认
          app.request({
            url: '/train/cancel',
            method: 'POST',
            data: {
              userId,
              trainId,
            },
            success: (res) => {
              if (res.data.code === '0') {
                wx.showToast({
                  title: '取消报名成功',
                  icon: 'success',
                });
                // 刷新培训记录
                this.fetchTrainRecords();
              } else {
                wx.showToast({
                  title: res.data.message || '取消报名失败',
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
        }
      },
    });
  },
  
  // 显示/隐藏提示
  toggleTip() {
    this.setData({
      showTip: !this.data.showTip,
      tipContent: '培训签到需要在个人中心扫码签到，请在复训截止时间前进行再次培训'
    });
  },
  
  // 点击页面其他位置关闭提示
  closeTipIfOpen() {
    if (this.data.showTip) {
      this.setData({
        showTip: false
      });
    }
  }
});