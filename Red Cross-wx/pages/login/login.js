const app = getApp();

Page({
  data: {
    account: '',
    password: '',
    isChecking: false // 添加加载状态
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
  async onLogin() {
    const { account, password } = this.data;

    if (!account || !password) {
      wx.showToast({
        title: '账号和密码不能为空',
        icon: 'none'
      });
      return;
    }

    if (account.length < 6) {
      wx.showToast({ title: '账号至少需要6位', icon: 'none' });
      return;
    }
    if (password.length < 6) {
      wx.showToast({ title: '密码至少需要6位', icon: 'none' });
      return;
    }

    this.setData({ isChecking: true });
    
    try {
      // 1. 先检查用户是否已登录
      const checkRes = await this.checkUserLoginStatus(account);
      if (checkRes.code === '-1' ) {
        if(checkRes.message ==='用户已登录'){
        // 用户已登录，询问是否继续登录
        wx.showModal({
          title: '提示',
          content: '该账号已在其他地方登录，是否继续登录？继续登录将使之前的登录无效',
          confirmText: '继续登录',
          cancelText: '取消',
          success: async (res) => {
            if (res.confirm) {
              await this.performLogin(account, password);
            } else {
              wx.showToast({
                title: '登录已取消',
                icon: 'none'
              });
            }
          }
        });
        } else{
          wx.showToast({
            title: '用户不存在',
            icon: 'none'
          });
        }

      } else if (checkRes.code === '0') {
        // 用户未登录，直接登录
        await this.performLogin(account, password);
      } else {
        wx.showToast({
          title: checkRes.message || '登录检查失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.showToast({
        title: '网络请求失败',
        icon: 'none'
      });
    } finally {
      this.setData({ isChecking: false });
    }
  },

  // 检查用户登录状态
  checkUserLoginStatus(account) {
    return new Promise((resolve, reject) => {
      app.request({
        url: '/user/checkLogin',
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: { account },
        success: (res) => {
          resolve(res.data);
        },
        fail: (err) => {
          reject(err);
        }
      });
    });
  },

  // 执行实际的登录操作
  performLogin(account, password) {
    return new Promise((resolve, reject) => {
      app.request({
        url: '/user/admin',
        method: 'POST',
        header: {
          'Content-Type': 'application/json'
        },
        data: { account, password },
        success: (res) => {
          if (res.data.code === '0') {
            wx.setStorageSync('userInfo', res.data.data);
            wx.switchTab({
              url: '/pages/myself/myself'
            });
            resolve();
          } else {
            wx.showToast({
              title: res.data.message || '登录失败，请检查账号和密码',
              icon: 'none'
            });
            resolve(); // 这里改为resolve，因为这不是Promise的失败，只是业务逻辑的失败
          }
        },
        fail: (err) => {
          wx.showToast({
            title: '网络请求失败',
            icon: 'none'
          });
          reject(err);
        }
      });
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