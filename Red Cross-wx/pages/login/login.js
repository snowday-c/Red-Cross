Page({
  data: {
    email: '',
    password: ''
  },

  // 获取邮箱输入
  onEmailInput(e) {
    this.setData({
      email: e.detail.value
    });
  },

  // 获取密码输入
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  // 登录按钮点击
  onLogin() {
    const { email, password } = this.data;

    if (!email || !password) {
      wx.showToast({
        title: '邮箱和密码不能为空',
        icon: 'none'
      });
      return;
    }

    // 登录验证
    wx.request({
      url: 'https://yourapi.com/login', // API接口
      method: 'POST',
      data: {
        email,
        password
      },
      success(res) {
        if (res.data.success) {
          wx.navigateTo({
            url: '/pages/myself/myself' // 登录成功后跳转到个人中心
          });
        } else {
          wx.showToast({
            title: '登录失败，请检查邮箱和密码',
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
  }
});
