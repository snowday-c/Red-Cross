const app = getApp();

Page({
  data: {
    account: '',
    email: '',
    newPassword: '',
    verificationCode: '',
    isCodeSent: false,
    codeButtonText: '发送验证码',
    countdown: 60,
    emailError: '' // 新增：用于显示邮箱错误信息
  },

  // 更严格的邮箱验证正则表达式
  validateEmail(email) {
    const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return reg.test(email);
  },

  inputAccount: function(e) {
    this.setData({ account: e.detail.value });
  },

  inputEmail: function(e) {
    const email = e.detail.value;
    let emailError = '';
    
    if (email && !this.validateEmail(email)) {
      emailError = '邮箱格式不正确';
    }
    
    this.setData({ 
      email,
      emailError 
    });
  },

  inputNewPassword: function(e) {
    this.setData({ newPassword: e.detail.value });
  },

  inputVerificationCode: function(e) {
    this.setData({ verificationCode: e.detail.value });
  },

  sendVerificationCode: function() {
    const { email } = this.data;
    
    // 验证邮箱是否为空
    if (!email) {
      wx.showToast({ title: '请输入邮箱', icon: 'none' });
      this.setData({ emailError: '请输入邮箱' });
      return;
    }
    
    // 验证邮箱格式
    if (!this.validateEmail(email)) {
      wx.showToast({ title: '邮箱格式不正确', icon: 'none' });
      this.setData({ emailError: '邮箱格式不正确' });
      return;
    }
    
    // 清除错误状态
    this.setData({ emailError: '' });

    app.request({
      url: '/user/email/sendCode',
      method: 'POST',
      data: { email },
      success: (res) => {
        if (res.data.code === '0') {
          wx.showToast({ title: '验证码已发送', icon: 'none' });
          this.startCountdown();
        } else {
          wx.showToast({ 
            title: res.data.message || '发送失败', 
            icon: 'none' 
          });
        }
      },
      fail: () => {
        wx.showToast({ title: '网络请求失败', icon: 'none' });
      }
    });
  },

  startCountdown: function() {
    this.setData({ 
      isCodeSent: true, 
      codeButtonText: `60秒后重发` 
    });

    const timer = setInterval(() => {
      let countdown = this.data.countdown - 1;
      this.setData({ 
        countdown, 
        codeButtonText: `${countdown}秒后重发` 
      });

      if (countdown <= 0) {
        clearInterval(timer);
        this.setData({ 
          isCodeSent: false, 
          codeButtonText: '发送验证码', 
          countdown: 60 
        });
      }
    }, 1000);
  },

  submitNewPassword: function() {
    const { account, email, newPassword, verificationCode } = this.data;
    
    // 验证所有字段是否填写
    if (!account || !email || !newPassword || !verificationCode) {
      wx.showToast({ title: '请填写完整信息', icon: 'none' });
      return;
    }
    
    // 再次验证邮箱格式
    if (!this.validateEmail(email)) {
      wx.showToast({ title: '邮箱格式不正确', icon: 'none' });
      this.setData({ emailError: '邮箱格式不正确' });
      return;
    }
    
    // 验证账号密码长度
    if (account.length < 6) {
      wx.showToast({ title: '账号至少需要6位', icon: 'none' });
      return;
    }
    if (newPassword.length < 6) {
      wx.showToast({ title: '密码至少需要6位', icon: 'none' });
      return;
    }


    app.request({
      url: '/user/email/verifyCode',
      method: 'POST',
      data: { email, code: verificationCode },
      success: (res) => {
        if (res.data.code === '0') {
          app.request({
            url: '/user/forget/password',
            method: 'POST',
            data: { account, email, newPassword },
            success: (res) => {
              if (res.data.code === '0') {
                wx.showToast({
                  title: '密码重置成功',
                  icon: 'success',
                  duration: 2000
                });

                setTimeout(() => {
                  wx.redirectTo({
                    url: '/pages/login/login'
                  });
                }, 2000);
              } else {
                wx.showToast({ 
                  title: res.data.message || '密码重置失败', 
                  icon: 'none' 
                });
              }
            },
            fail: () => {
              wx.showToast({ title: '网络请求失败', icon: 'none' });
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
        wx.showToast({ title: '网络请求失败', icon: 'none' });
      }
    });
  },

  navigateToLogin() {
    wx.redirectTo({
      url: '/pages/login/login'
    });
  }
});