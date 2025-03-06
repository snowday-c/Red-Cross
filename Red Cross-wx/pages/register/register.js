Page({
  data: {
    username: '',
    account: '',
    password: '',
    confirmPassword: '',
    email: '',
    code: '',
    isCodeSent: false, // 控制验证码按钮状态
    codeButtonText: '发送验证码', // 验证码按钮文本
    countdown: 60 // 倒计时
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
  onCodeInput(e) {
    this.setData({
      code: e.detail.value
    });
  },

  // 发送验证码
  sendCode() {
    const { email } = this.data;

    if (!email) {
      wx.showToast({
        title: '请输入邮箱',
        icon: 'none'
      });
      return;
    }

    wx.request({
      url: 'http://localhost:8090/api/user/email/sendCode', // 发送验证码的接口
      method: 'POST',
      data: { email },
      success: (res) => {
        if (res.data.code === '0') {
          wx.showToast({
            title: '验证码已发送',
            icon: 'none'
          });
          this.startCountdown(); // 开始倒计时
        } else {
          wx.showToast({
            title: res.data.message || '验证码发送失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
    });
  },

  // 开始倒计时
  startCountdown() {
    this.setData({ isCodeSent: true, codeButtonText: `60秒后重发` });

    const timer = setInterval(() => {
      let countdown = this.data.countdown - 1;
      this.setData({ countdown, codeButtonText: `${countdown}秒后重发` });

      if (countdown <= 0) {
        clearInterval(timer);
        this.setData({ isCodeSent: false, codeButtonText: '发送验证码', countdown: 60 });
      }
    }, 1000);
  },

  // 注册按钮点击
  onRegister() {
    const { username, account, password, confirmPassword, email, code } = this.data;

    if (!username || !account || !password || !confirmPassword || !email || !code) {
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

    // 先验证验证码
    wx.request({
      url: 'http://localhost:8090/api/user/email/verifyCode', // 验证验证码的接口
      method: 'POST',
      data: { email, code },
      success: (res) => {
        if (res.data.code === '0') {
          // 验证码正确，发送注册请求
          wx.request({
            url: 'http://localhost:8090/api/user/register',
            method: 'POST',
            header: {
              'Content-Type': 'application/json'
            },
            data: {
              userName: username,
              account,
              password,
              email
            },
            success: (res) => {
              if (res.data.code === '0') {
                wx.showToast({
                  title: '注册成功',
                  icon: 'success'
                });
                setTimeout(() => {
                  wx.redirectTo({
                    url: '/pages/login/login' // 注册成功后跳转到登录页面
                  });
                }, 2000); // 2秒后跳转
              } else {
                wx.showToast({
                  title: res.data.message || '注册失败',
                  icon: 'none'
                });
              }
            },
            fail: () => {
              wx.showToast({
                title: '网络请求失败',
                icon: 'none'
              });
            }
          });
        } else {
          wx.showToast({
            title: res.data.message || '验证码错误',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
    });
  },

  // 跳转到登录页面
  navigateToLogin() {
    wx.redirectTo({
      url: '/pages/login/login'
    });
  }
});