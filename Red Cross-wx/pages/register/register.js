Page({
  data: {
    username: '',
    account: '',
    password: '',
    confirmPassword: '',
    email: '',
    captcha: ''
  },

  // 获取用户名输入
  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    });
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

  // 获取确认密码输入
  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value
    });
  },

  // 获取邮箱输入
  onEmailInput(e) {
    this.setData({
      email: e.detail.value
    });
  },

  // 获取验证码输入
  onCaptchaInput(e) {
    this.setData({
      captcha: e.detail.value
    });
  },

  // 发送验证码
  sendCaptcha() {
    wx.showToast({
      title: '验证码已发送',
      icon: 'none'
    });
  },

  // 注册按钮点击
  onRegister() {
    const { username, account, password, confirmPassword, email, captcha } = this.data;

    if (!username || !account || !password || !confirmPassword || !email || !captcha) {
      wx.showToast({
        title: '请填写完整信息',
        icon: 'none'
      });
      return;
    }

    if (password !== confirmPassword) {
      wx.showToast({
        title: '两次输入的密码不一致',
        icon: 'none'
      });
      return;
    }

    // 注册请求
    wx.request({
      url: 'http://localhost:8090/user/register',
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      data: {
        username,
        account,
        password,
        email,
        captcha
      },
      success(res) {
        if (res.data.code === '0') {
          wx.showToast({
            title: '注册成功',
            icon: 'success'
          });
          wx.navigateTo({
            url: '/pages/login/login' // 注册成功后跳转到登录页面
          });
        } else {
          wx.showToast({
            title: res.data.message || '注册失败',
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

  // 跳转到登录页面
  navigateToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  }
});