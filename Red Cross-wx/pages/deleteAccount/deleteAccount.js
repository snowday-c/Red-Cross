const app = getApp();

Page({
  data: {
    account: '',          // 账号
    password: '',        // 密码
    email: '',           // 邮箱
    verificationCode: '', // 验证码
    isCountingDown: false, // 是否在倒计时
    countdownText: '发送验证码', // 倒计时文字
    countdown: 60         // 倒计时秒数
  },

  // 账号输入
  onAccountInput(e) {
    this.setData({
      account: e.detail.value.trim()
    });
  },

  // 密码输入
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  // 邮箱输入
  onEmailInput(e) {
    this.setData({
      email: e.detail.value.trim()
    });
  },

  // 验证码输入
  onVerificationCodeInput(e) {
    this.setData({
      verificationCode: e.detail.value.trim()
    });
  },

  // 发送验证码
  sendVerificationCode() {
    const { email } = this.data;

    // 验证邮箱格式
    if (!this.validateEmail(email)) {
      wx.showToast({
        title: '请输入正确的邮箱格式',
        icon: 'none'
      });
      return;
    }

    // 显示加载中
    wx.showLoading({
      title: '发送中...',
      mask: true
    });

    // 发送请求到后端
    app.request({
      url: '/user/email/sendCode',
      method: 'POST',
      data: {
        email: email,
        type: 'delete_account' // 指定验证码类型为注销账号
      },
      success: (res) => {
        wx.hideLoading();
        if (res.data.code === '0') {
          wx.showToast({
            title: '验证码已发送',
            icon: 'success'
          });
          // 开始倒计时
          this.startCountdown();
        } else {
          wx.showToast({
            title: res.data.message || '验证码发送失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 开始倒计时
  startCountdown() {
    this.setData({
      isCountingDown: true,
      countdown: 60,
      countdownText: '60秒后重试'
    });

    const timer = setInterval(() => {
      let countdown = this.data.countdown - 1;
      if (countdown <= 0) {
        clearInterval(timer);
        this.setData({
          isCountingDown: false,
          countdownText: '发送验证码'
        });
      } else {
        this.setData({
          countdown: countdown,
          countdownText: `${countdown}秒后重试`
        });
      }
    }, 1000);
  },

  // 提交注销账号
  submitDeleteAccount() {
    // 表单验证
    if (!this.validateForm()) {
      return;
    }

    // 显示加载中
    wx.showLoading({
      title: '处理中...',
      mask: true
    });

    // 先验证验证码
    app.request({
      url: '/user/email/verifyCode',
      method: 'POST',
      data: {
        email: this.data.email,
        code: this.data.verificationCode,
        type: 'delete_account'
      },
      success: (verifyRes) => {
        if (verifyRes.data.code === '0') {
          // 验证码验证通过，执行注销操作
          this.executeDeleteAccount();
        } else {
          wx.hideLoading();
          wx.showToast({
            title: verifyRes.data.message || '验证码错误',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 执行注销账号操作
  executeDeleteAccount() {
    const { account, password, email } = this.data;

    app.request({
      url: '/user/logout',
      method: 'POST',
      data: {
        account: account,
        password: password,
        email: email
      },
      success: (res) => {
        wx.hideLoading();
        if (res.data.code === '0') {
          // 注销成功，清除本地存储
          wx.removeStorageSync('userInfo');
          
          wx.showToast({
            title: '账号已注销',
            icon: 'success',
            duration: 2000,
            success: () => {
              // 注销成功后跳转到首页
              setTimeout(() => {
                wx.reLaunch({
                  url: '/pages/index/index',
                });
              }, 2000);
            }
          });
        } else {
          wx.showToast({
            title: res.data.message || '注销失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 验证邮箱格式
  validateEmail(email) {
    const reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    return reg.test(email);
  },

  // 表单验证
  validateForm() {
    const { account, password, email, verificationCode } = this.data;

    if (!account) {
      wx.showToast({
        title: '请输入账号',
        icon: 'none'
      });
      return false;
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return false;
    }

    if (!email) {
      wx.showToast({
        title: '请输入邮箱',
        icon: 'none'
      });
      return false;
    }

    if (!this.validateEmail(email)) {
      wx.showToast({
        title: '请输入正确的邮箱格式',
        icon: 'none'
      });
      return false;
    }

    if (!verificationCode) {
      wx.showToast({
        title: '请输入验证码',
        icon: 'none'
      });
      return false;
    }

    return true;
  }
});