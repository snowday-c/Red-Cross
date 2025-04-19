const app = getApp();

Page({
  data: {
    feedbackContent: '', // 反馈内容
    isAnonymous: false,  // 是否匿名
    submitting: false    // 是否正在提交
  },

  // 监听反馈内容输入
  onContentInput(e) {
    this.setData({
      feedbackContent: e.detail.value
    });
  },

  // 切换匿名状态
  toggleAnonymous() {
    this.setData({
      isAnonymous: !this.data.isAnonymous
    });
  },

  // 提交反馈
  submitFeedback() {
    const { feedbackContent, isAnonymous } = this.data;
    
    // 验证输入
    if (!feedbackContent.trim()) {
      wx.showToast({
        title: '请输入反馈内容',
        icon: 'none'
      });
      return;
    }

    // 防止重复提交
    if (this.data.submitting) return;
    
    this.setData({ submitting: true });
    
    // 获取用户信息
    const userInfo = wx.getStorageSync('userInfo');
    
    // 准备提交数据
    const submitData = {
      adviceContent: feedbackContent,
      userId: userInfo.userId,
      // 如果匿名，发送者为"匿名用户"，否则为用户名
      adviceSender: isAnonymous ? '匿名用户' : userInfo.userName
    };
    
    // 发送请求
    app.request({
      url: '/advice/insert',
      method: 'POST',
      data: submitData,
      success: (res) => {
        if (res.data.code === '0') {
          wx.showToast({
            title: '反馈提交成功',
            icon: 'success'
          });
          
          // 成功后返回上一页
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({
            title: res.data.message || '提交失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      },
      complete: () => {
        this.setData({ submitting: false });
      }
    });
  }
});