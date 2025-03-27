const app = getApp();

Page({
  data: {
    currentSwiperIndex: 0,    // 当前轮播图索引
    currentNotice: {},        // 当前显示的通知
    allNotices: [],           // 所有通知列表
    isLoggedIn: false,        // 登录状态标识
    userInfo: null            // 用户信息存储
  },

  onLoad: function() {
    this.checkLoginStatus();
    this.fetchNotices();
  },

  onShow: function() {
    // 每次页面显示时检查登录状态
    this.checkLoginStatus();
  },

  // 检查登录状态（从本地存储获取）
  checkLoginStatus: function() {
    const userInfo = wx.getStorageSync('userInfo');
    
    if (userInfo && userInfo.token) {
      this.setData({
        isLoggedIn: true,
        userInfo: userInfo
      });
    } else {
      this.setData({
        isLoggedIn: false,
        userInfo: null
      });
    }
  },

  // 轮播图切换事件
  handleSwiperChange: function(e) {
    this.setData({
      currentSwiperIndex: e.detail.current
    });
  },

  // 获取通知数据
  fetchNotices: function() {
    app.request({
      url: '/message/public',
      method: 'GET',
      success: (res) => {
        if (res.data.code === '0' && res.data.data && res.data.data.length > 0) {
          const sortedNotices = res.data.data.sort((a, b) => b.messageId - a.messageId);
          const notices = sortedNotices.map(item => ({
            ...item,
            displayTime: item.time.split(' ')[0]
          }));
          
          this.setData({
            allNotices: notices,
            currentNotice: notices[0]
          });
          getApp().globalData.notices = notices;
        } else {
          this.setData({
            currentNotice: { title: '暂无公告' }
          });
        }
      },
      fail: (err) => {
        console.error('获取通知失败:', err);
        wx.showToast({
          title: '获取通知失败',
          icon: 'none'
        });
        this.setData({
          currentNotice: { title: '获取公告失败' }
        });
      }
    });
  },

  // 跳转到通知页面
  navigateToNotices: function() {
    if (this.data.allNotices.length > 0) {
      wx.navigateTo({
        url: '/pages/notices/notices'
      });
    }
  }
});