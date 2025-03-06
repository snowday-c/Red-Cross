Page({
  data: {
    account: '',
    password: ''
  },

  // 获取账号输入
  onAccountInput(e) {
    this.setData({
      account: e.detail.value
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
    const { account, password } = this.data;

    if (!account || !password) {
      wx.showToast({
        title: '账号和密码不能为空',
        icon: 'none'
      });
      return;
    }

    // 登录验证
    wx.request({
      url: 'http://localhost:8090/api/user/login',
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      data: {
        account,
        password
      },
      success(res) {
        if (res.data.code === '0') {
          wx.setStorageSync('userInfo', res.data.data);
          wx.switchTab({
            url: '/pages/myself/myself' // 登录成功后跳转到个人中心
          });
        } else {
          wx.showToast({
            title: res.data.message || '登录失败，请检查账号和密码',
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
  },

  // 跳转到忘记密码页面
  navigateToForgetPassword() {
    wx.redirectTo({
      url: '/pages/forgetPassword/forgetPassword'
    });
  },

  // 跳转到注册页面
  navigateToRegister() {
    wx.redirectTo({
      url: '/pages/register/register'
    });
  }
});