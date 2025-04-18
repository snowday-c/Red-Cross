const app = getApp();

Page({
  data: {
    userInfo: {}, // 用户信息
    showEditModal: false, // 是否显示修改弹窗
    showAvatarPicker: false, // 是否显示头像选择弹窗
    showSettingsMenu: false, // 是否显示设置菜单
    showDeleteAccountModal: false, // 是否显示注销账号弹窗
    avatarList: [
      'https://120.27.161.155/files/avatar/avatar1.jpg',
      'https://120.27.161.155/files/avatar/avatar2.jpg',
      'https://120.27.161.155/files/avatar/avatar3.jpg',
      'https://120.27.161.155/files/avatar/avatar4.jpg',
      'https://120.27.161.155/files/avatar/avatar5.jpg',
      'https://120.27.161.155/files/avatar/avatar6.jpg',
      'https://120.27.161.155/files/avatar/avatar7.jpg',
      'https://120.27.161.155/files/avatar/avatar8.jpg',
      'https://120.27.161.155/files/avatar/avatar9.jpg',
      'https://120.27.161.155/files/avatar/avatar10.jpg',
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
      userInfo,
      showSettingsMenu: false // 隐藏设置菜单
    });
  },
  
// 退出登录
// 退出登录
logout() {
  wx.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          //  先获取用户信息
          const userInfo = wx.getStorageSync('userInfo');
          if (!userInfo || !userInfo.userId) {
            throw new Error('用户信息不存在');
          }
          //  调用后端接口清除token
          const result = await new Promise((resolve, reject) => {
            app.request({
              url: '/user/forceLogout',
              method: 'POST',
              header: {
                'Content-Type': 'application/json'
              },
              data: {
                userId: userInfo.userId,
              },
              success: (res) => {
                resolve(res.data);
              },
              fail: (err) => {
                reject(err);
              }
            });
          });
          //  无论后端是否成功，都执行前端退出逻辑
          wx.removeStorageSync('userInfo');
          this.setData({
            userInfo: {}
          });
          //  根据后端响应显示不同提示
          if (result && result.code === '0') {
            wx.showToast({
              title: '已退出登录',
              icon: 'success'
            });
          } else {
            wx.showToast({
              title: '已退出本地登录' + (result ? `(${result.message})` : ''),
              icon: 'none'
            });
          }
          //  更新首页登录状态
          const pages = getCurrentPages();
          const indexPage = pages.find(page => page.route === 'pages/index/index');
          if (indexPage) {
            indexPage.setData({
              isLoggedIn: false,
              userInfo: null
            });
          }
          // 跳转到首页
          wx.switchTab({
            url: '/pages/index/index',
          });
        } catch (error) {
          // 网络请求失败时的处理
          console.error('退出登录失败:', error);
          wx.removeStorageSync('userInfo');
          this.setData({
            userInfo: {}
          });
          wx.showToast({
            title: '已退出本地登录(网络异常)',
            icon: 'none'
          });
          wx.switchTab({
            url: '/pages/index/index',
          });
        }
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
        pictureUrl: selectedAvatar,
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
          userInfo.pictureUrl = selectedAvatar;
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
    const that = this;
    wx.showModal({
      title: '修改用户名',
      content: '',
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
              oldName: wx.getStorageSync('userInfo').userName,
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
  },

  // 添加用户反馈功能
  

  // 1. 首先修改 myself.js 文件，添加反馈功能入口
  // 切换设置菜单显示状态
  toggleSettingsMenu() {
    this.setData({
      showSettingsMenu: !this.data.showSettingsMenu
    });
  },

  // 跳转到修改密码页面
  navigateToChangePassword() {
    if (!this.checkLogin()) return;
    this.setData({ showSettingsMenu: false });
    wx.navigateTo({
      url: '/pages/changePassword/changePassword'
    });
  },
  
  // 跳转到用户反馈页面
  navigateToFeedback() {
    if (!this.checkLogin()) return;
    this.setData({ showSettingsMenu: false });
    wx.navigateTo({
      url: '/pages/feedback/feedback'
    });
  },

  // 显示注销账号确认弹窗
  showDeleteAccountModal() {
    this.setData({
      showSettingsMenu: false,
      showDeleteAccountModal: true
    });
  },

  // 隐藏注销账号确认弹窗
  hideDeleteAccountModal() {
    this.setData({
      showDeleteAccountModal: false
    });
  },

  // 注销账号
  deleteAccount() {
    this.setData({
      showDeleteAccountModal: false
    });
    wx.navigateTo({
      url: '/pages/deleteAccount/deleteAccount'
    });
  },

  // 选择并上传自定义头像
  chooseAndUploadImage() {
    if (!this.checkLogin()) return;
    const that = this;
    const userId = wx.getStorageSync('userInfo').userId;
    
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success(res) {
        const tempFilePath = res.tempFiles[0].tempFilePath;
        
        // 显示上传中提示
        wx.showLoading({
          title: '上传中...',
        });
        
        // 上传图片到服务器
        wx.uploadFile({
          url: "https://120.27.161.155/files/userAvatar",
          filePath: tempFilePath,
          name: 'file',
          formData: {
            'userId': userId
          },
          success(res) {
            wx.hideLoading();
            try {
              const data = JSON.parse(res.data);
              if (data.code === '0') {
                // 更新用户信息
                app.request({
                  url: '/user/update/userInfo',
                  method: 'POST',
                  header: {
                    'Content-Type': 'application/json'
                  },
                  data: {
                    userId: userId,
                    userName: wx.getStorageSync('userInfo').userName,
                    pictureUrl: data.data, // 服务器返回的图片URL
                    account: wx.getStorageSync('userInfo').account,
                    email: wx.getStorageSync('userInfo').email
                  },
                  success: (updateRes) => {
                    if (updateRes.data.code === '0') {
                      // 更新本地存储和页面显示
                      const userInfo = wx.getStorageSync('userInfo');
                      userInfo.pictureUrl = data.data;
                      wx.setStorageSync('userInfo', userInfo);
                      that.setData({
                        userInfo: userInfo,
                        showAvatarPicker: false
                      });
                      wx.showToast({
                        title: '头像更新成功',
                        icon: 'success'
                      });
                    } else {
                      wx.showToast({
                        title: updateRes.data.message || '更新失败',
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
              } else {
                wx.showToast({
                  title: data.message || '上传失败',
                  icon: 'none'
                });
              }
            } catch (e) {
              wx.showToast({
                title: '服务器响应异常',
                icon: 'none'
              });
            }
          },
          fail() {
            wx.hideLoading();
            wx.showToast({
              title: '上传失败',
              icon: 'none'
            });
          }
        });
      }
    });
  }
});