Page({
  data: {
    email: '',
    password: '',
    confirmPassword: ''
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

  // 获取确认密码输入
  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value
    });
  },

  // 注册按钮点击
  onRegister() {
    const { email, password, confirmPassword } = this.data;

    if (!email || !password || !confirmPassword) {
      wx.showToast({
        title: '邮箱和密码不能为空',
        icon: 'none'
      });
      return;
    }

    if (password !== confirmPassword) {
      wx.showToast({
        title: '密码不一致',
        icon: 'none'
      });
      return;
    }

    // 假设有一个API进行注册
    wx.request({
      url: 'https://yourapi.com/register', // 替换为你的API接口
      method: 'POST',
      data: {
        email,
        password
      },
      success(res) {
        if (res.data.success) {
          wx.showToast({
            title: '注册成功！',
            icon: 'success'
          });
          wx.navigateTo({
            url: '/pages/login/login' // 注册成功后跳转到登录页
          });
        } else {
          wx.showToast({
            title: '注册失败，请稍后再试',
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
