const app = getApp();

Page({
  data: {
    account: '',          // 账号
    oldPassword: '',      // 旧密码
    newPassword: '',      // 新密码
    confirmPassword: ''   // 确认新密码
  },

  // 账号输入
  onAccountInput(e) {
    this.setData({
      account: e.detail.value.trim()
    });
  },

  // 旧密码输入
  onOldPasswordInput(e) {
    this.setData({
      oldPassword: e.detail.value
    });
  },

  // 新密码输入
  onNewPasswordInput(e) {
    this.setData({
      newPassword: e.detail.value
    });
  },

  // 确认新密码输入
  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value
    });
  },

  // 提交修改密码
  submitChangePassword() {
    // 表单验证
    if (!this.validateForm()) {
      return;
    }

    // 显示加载中
    wx.showLoading({
      title: '修改中...',
      mask: true
    });

    // 发送请求到后端
    app.request({
      url: '/user/update/password',
      method: 'POST',
      data: {
        account: this.data.account,
        oldPassword: this.data.oldPassword,
        newPassword: this.data.newPassword
      },
      success: (res) => {
        wx.hideLoading();
        if (res.data.code === '0') {
          // 密码修改成功后清除本地存储的用户信息
          wx.removeStorageSync('userInfo');
          
          wx.showToast({
            title: '密码修改成功，请重新登录',
            icon: 'success',
            duration: 2000,
            success: () => {
              // 修改成功后跳转到首页并清除页面栈
              setTimeout(() => {
                wx.reLaunch({
                  url: '/pages/index/index',
                });
              }, 2000);
            }
          });
        } else {
          wx.showToast({
            title: res.data.message || '密码修改失败',
            icon: 'none',
            duration: 2000
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none',
          duration: 2000
        });
      }
    });
  },

  // 表单验证
  validateForm() {
    const { account, oldPassword, newPassword, confirmPassword } = this.data;

    if (!account) {
      wx.showToast({
        title: '请输入账号',
        icon: 'none'
      });
      return false;
    }

    if (!oldPassword) {
      wx.showToast({
        title: '请输入旧密码',
        icon: 'none'
      });
      return false;
    }

    if (!newPassword) {
      wx.showToast({
        title: '请输入新密码',
        icon: 'none'
      });
      return false;
    }

    if (newPassword.length < 6 || newPassword.length > 20) {
      wx.showToast({
        title: '新密码长度需为6-20位',
        icon: 'none'
      });
      return false;
    }

    if (newPassword === oldPassword) {
      wx.showToast({
        title: '新密码不能与旧密码相同',
        icon: 'none'
      });
      return false;
    }

    if (newPassword !== confirmPassword) {
      wx.showToast({
        title: '两次输入的新密码不一致',
        icon: 'none'
      });
      return false;
    }

    return true;
  }
});