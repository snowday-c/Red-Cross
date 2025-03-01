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

  // 修改头像
  changeAvatar() {
    wx.chooseMedia({
      count: 1, // 只能选择一张图片
      success(res) {
        const tempFilePath = res.tempFiles[0].tempFilePath;
        const fs = wx.getFileSystemManager();
        
        // 将图片文件转为二进制数据
        fs.readFile({
          filePath: tempFilePath,
          encoding: 'base64', // 将文件编码为base64
          success: (fileRes) => {
            const base64Data = fileRes.data;

            // 上传二进制数据到服务器
            wx.request({
              url: 'http://localhost:8090/user/update/userInfo',
              method: 'POST',
              header: {
                'Content-Type': 'application/json'
              },
              data: {
                userId: wx.getStorageSync('userInfo').userId,
                userName: wx.getStorageSync('userInfo').userName,
                pictureUrl: base64Data, // 将图片的二进制数据发送到后端
                account: wx.getStorageSync('userInfo').account,
                email: wx.getStorageSync('userInfo').email
              },
              success(res) {
                const data = res.data;
                if (data.code === '0') {
                  wx.showToast({
                    title: '头像修改成功',
                    icon: 'success'
                  });
                  // 更新本地缓存中的用户信息
                  const userInfo = wx.getStorageSync('userInfo');
                  userInfo.pictureUrl = data.data.pictureUrl; // 假设服务器返回的是新头像的URL
                  wx.setStorageSync('userInfo', userInfo);
                } else {
                  wx.showToast({
                    title: data.message || '头像修改失败',
                    icon: 'none'
                  });
                }
              },
              fail() {
                wx.showToast({
                  title: '上传失败',
                  icon: 'none'
                });
              }
            });
          },
          fail() {
            wx.showToast({
              title: '读取文件失败',
              icon: 'none'
            });
          }
        });
      }
    });
    this.hideEditOptions();
  },

  // 修改用户名
  changeUsername() {
    const that = this; // 保存当前页面的 this 上下文
    wx.showModal({
      title: '修改用户名',
      content: '请输入新的用户名',
      editable: true,
      success(res) {
        if (res.confirm && res.content) {
          const newUsername = res.content;
          wx.request({
            url: 'http://localhost:8090/user/update/userInfo',
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
