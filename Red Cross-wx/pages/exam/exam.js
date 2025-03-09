const app = getApp();

Page({
  /**
   * 开始考试
   */
  startExam() {
    wx.showModal({
      title: '考试提醒',
      content: '开始考试后不能中断，确认开始考试吗？',
      confirmText: '确认',
      cancelText: '取消',
      success: (res) => {
        if (res.confirm) {
          // 用户点击确认，跳转到考试页面
          app.request({
            url: '/question/exam',
            method: 'POST',
            header: {
              'Content-Type': 'application/json'
            },
            data: {
              userId: wx.getStorageSync('userInfo').userId
            },
            success(res) {
              if (res.data.code === '0') {
                wx.setStorageSync('questions', res.data.data);
                wx.redirectTo({
                  url: '/pages/examStart/examStart',
                });
              } else {
                wx.showToast({
                  title: res.data.message || '考试加载失败，请稍后重试',
                  icon: 'none'
                });
              }
            },
            fail() {
              wx.showToast({
                title: '网络请求失败',
                icon: 'none'
              });
            }
          });

        } else if (res.cancel) {
          // 用户点击取消，不执行任何操作
          console.log('用户取消考试');
        }
      },
    });
  },

  /**
   * 取消考试
   */
  cancelExam() {
    wx.switchTab({
      url: '/pages/study/study', // 回到原页面
    });
  },
});