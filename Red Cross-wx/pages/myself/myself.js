const app = getApp();

Page({
  data: {
    userInfo: {}, // 用户信息
    showEditModal: false, // 是否显示修改弹窗
    showAvatarPicker: false, // 是否显示头像选择弹窗
    avatarList: [
      '/assets/avatar/avatar1.jpg',
      '/assets/avatar/avatar2.jpg',
      '/assets/avatar/avatar3.jpg',
      '/assets/avatar/avatar4.jpg',
      '/assets/avatar/avatar5.jpg',
      '/assets/avatar/avatar6.jpg',
      '/assets/avatar/avatar7.jpg',
      '/assets/avatar/avatar8.jpg',
      '/assets/avatar/avatar9.jpg',
      '/assets/avatar/avatar10.jpg',
    ]
  },

  onLoad() {
    // 从本地缓存中获取用户信息
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo
    });
  },

  onShow() {
    // 每次页面显示时，从本地缓存中获取用户信息并更新页面数据
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo
    });
  },
  
  // 退出登录
  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除本地缓存中的用户信息
          wx.removeStorageSync('userInfo');
          // 更新页面数据
          this.setData({
            userInfo: {}
          });
          wx.showToast({
            title: '已退出登录',
            icon: 'none'
          });
          wx.switchTab({
            url: '/pages/index/index',
          });
        }
      }
    });
  },

  // 显示修改选项弹窗
  showEditOptions() {
    if (!this.checkLogin()) return;
    this.setData({
      showEditModal: true
    });
  },

  // 隐藏修改选项弹窗
  hideEditOptions() {
    this.setData({
      showEditModal: false
    });
  },

  // 检查用户是否登录
  checkLogin() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.userId) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return false;
    }
    return true;
  },

  // 跳转到个人历史成绩页面
  navigateToHistoryScores() {
    if (!this.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/historyScores/historyScores'
    });
  },

  // 跳转到个人证书页面
  navigateToCertificates() {
    if (!this.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/certificates/certificates'
    });
  },

  // 跳转到历史场地预约页面
  navigateToHistoryTrains() {
    if (!this.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/historyTrains/historyTrains'
    });
  },

  // 跳转到消息通知页面
  navigateToMessages() {
    if (!this.checkLogin()) return;
    wx.navigateTo({
      url: '/pages/messages/messages'
    });
  },

  // 显示头像选择弹窗
  changeAvatar() {
    if (!this.checkLogin()) return;
    this.setData({
      showAvatarPicker: true
    });
    this.hideEditOptions();
  },

  // 隐藏头像选择弹窗
  hideAvatarPicker() {
    this.setData({
      showAvatarPicker: false
    });
  },

  // 选择头像
  selectAvatar(e) {
    const index = e.currentTarget.dataset.index;
    const selectedAvatar = this.data.avatarList[index];

    // 上传选择的头像名称到后端
    app.request({
      url: '/user/update/userInfo',
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      data: {
        userId: wx.getStorageSync('userInfo').userId,
        userName: wx.getStorageSync('userInfo').userName,
        pictureUrl: selectedAvatar, // 发送选择的头像路径
        account: wx.getStorageSync('userInfo').account,
        email: wx.getStorageSync('userInfo').email
      },
      success: (res) => {
        const data = res.data;
        if (data.code === '0') {
          wx.showToast({
            title: '头像修改成功',
            icon: 'success'
          });
          // 更新本地缓存中的用户信息
          const userInfo = wx.getStorageSync('userInfo');
          userInfo.pictureUrl = selectedAvatar; // 更新头像路径
          wx.setStorageSync('userInfo', userInfo);

          // 更新页面数据，触发重新渲染
          this.setData({
            userInfo: userInfo
          });
        } else {
          wx.showToast({
            title: data.message || '头像修改失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
      }
    });

    this.hideAvatarPicker();
  },

  // 修改用户名
  changeUsername() {
    if (!this.checkLogin()) return;
    const that = this; // 保存当前页面的 this 上下文
    wx.showModal({
      title: '修改用户名',
      content: '请输入新的用户名',
      editable: true,
      success(res) {
        if (res.confirm && res.content) {
          const newUsername = res.content;
          app.request({
            url: '/user/update/userInfo',
            method: 'POST',
            header: {
              'Content-Type': 'application/json'
            },
            data: {
              userId: wx.getStorageSync('userInfo').userId,
              pictureUrl: wx.getStorageSync('userInfo').pictureUrl,
              userName: newUsername,
              account: wx.getStorageSync('userInfo').account,
              email: wx.getStorageSync('userInfo').email
            },
            success(res) {
              if (res.data.code === '0') {
                wx.showToast({
                  title: '用户名修改成功',
                  icon: 'success'
                });
                // 更新本地缓存中的用户信息
                const userInfo = wx.getStorageSync('userInfo');
                userInfo.userName = newUsername;
                wx.setStorageSync('userInfo', userInfo);

                // 更新页面数据，触发重新渲染
                that.setData({
                  userInfo: userInfo
                });
              } else {
                wx.showToast({
                  title: res.data.message || '用户名修改失败',
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
      }
    });
    this.hideEditOptions();
  }
});