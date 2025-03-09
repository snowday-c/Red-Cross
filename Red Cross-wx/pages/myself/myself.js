const app = getApp();

Page({
  data: {
    userInfo: {}, // 用户信息
    showEditModal: false // 是否显示修改弹窗
  },

  onLoad() {
    // 从本地缓存中获取用户信息
    const userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo
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

  // 修改头像
  changeAvatar() {
    if (!this.checkLogin()) return;
  
    // 显示头像选择弹窗
    wx.showActionSheet({
      itemList: ['头像1', '头像2', '头像3', '头像4'], // 可供选择的头像名称
      success: (res) => {
        const selectedIndex = res.tapIndex;
        const avatarNames = ['avatar1', 'avatar2', 'avatar3', 'avatar4']; // 对应的图片文件名
        const selectedAvatar = avatarNames[selectedIndex];
  
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
            pictureUrl: selectedAvatar, // 发送选择的头像名称
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
              userInfo.pictureUrl = selectedAvatar; // 更新头像名称
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
      },
      fail: () => {
        wx.showToast({
          title: '选择取消',
          icon: 'none'
        });
      }
    });
  
    this.hideEditOptions();
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