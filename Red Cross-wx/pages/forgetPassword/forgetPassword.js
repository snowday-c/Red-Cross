const app = getApp();

Page({
  data: {
      account: '',
      email: '',
      newPassword: '',
      verificationCode: '',
      isCodeSent: false,
      codeButtonText: '发送验证码',
      countdown: 60
  },

  inputAccount: function(e) {
      this.setData({ account: e.detail.value });
  },

  inputEmail: function(e) {
      this.setData({ email: e.detail.value });
  },

  inputNewPassword: function(e) {
      this.setData({ newPassword: e.detail.value });
  },

  inputVerificationCode: function(e) {
      this.setData({ verificationCode: e.detail.value });
  },

  sendVerificationCode: function() {
      const { email } = this.data;
      if (!email) {
          wx.showToast({ title: '请输入邮箱', icon: 'none' });
          return;
      }

      app.request({
          url: '/user/email/sendCode',
          method: 'POST',
          data: { email },
          success: (res) => {
              if (res.data.code === '0') {
                  wx.showToast({ title: '验证码已发送' });
                  this.startCountdown();
              } else {
                  wx.showToast({ title: '发送失败', icon: 'none' });
              }
          }
      });
  },

  startCountdown: function() {
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

  submitNewPassword: function() {
    const { account, email, newPassword, verificationCode } = this.data;
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
                                duration: 2000 // 显示 2 秒
                            });

                            // 2 秒后跳转到登录页面
                            setTimeout(() => {
                              wx.redirectTo({
                                    url: '/pages/login/login' // 跳转登录页
                                });
                            }, 2000); // 延迟 2 秒
                        } else {
                            wx.showToast({ title: '密码重置失败', icon: 'none' });
                        }
                    }
                });
            } else {
                wx.showToast({ title: '验证码错误', icon: 'none' });
            }
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