const app = getApp();
Page({
  data: {
    notices: []
  },

  onLoad: function() {
    // 直接从全局获取已排序的通知数据
    const notices = getApp().globalData.notices || [];
    this.setData({
      notices: notices
    });
    
    // 如果没有全局数据，则重新请求
    if (notices.length === 0) {
      this.fetchNotices();
    }
  },

  fetchNotices: function() {
    app.request({
      url: '/message/public',
      method: 'GET',
      success: (res) => {
        if (res.data.code === '0' && res.data.data) {
          // 按messageId从大到小排序
          const sortedNotices = res.data.data.sort((a, b) => b.messageId - a.messageId)
            .map(item => ({
              ...item,
              displayTime: item.time.split(' ')[0]
            }));
            
          this.setData({
            notices: sortedNotices
          });
          getApp().globalData.notices = sortedNotices;
        }
      },
      fail: (err) => {
        console.error('获取通知失败:', err);
        wx.showToast({
          title: '获取通知失败',
          icon: 'none'
        });
      }
    });
  }
});