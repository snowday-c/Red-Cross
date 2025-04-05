const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    messageList: [], // 消息列表
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    this.fetchMessageList();
  },

  /**
   * 获取消息列表
   */
  fetchMessageList() {
    // 模拟向后端发送请求
    const userInfo = wx.getStorageSync('userInfo');
    app.request({
      url: '/message/private/receiver',
      method: 'GET',
      data:{
        receiver:userInfo.userName
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.code === '0') {
          const messageList = res.data.data;
          // 根据消息 ID 从大到小排序
          messageList.sort((a, b) => b.messageId - a.messageId);
          // 初始化 expanded 字段，用于控制消息内容的展开和收起
          messageList.forEach((item) => {
            item.expanded = false;
          });
          this.setData({
            messageList,
          });
        } else {
          wx.showToast({
            title: '获取消息失败',
            icon: 'none',
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
        });
      },
    });
  },

  /**
   * 切换消息的展开和收起状态
   */
  toggleMessage(e) {
    const index = e.currentTarget.dataset.index; // 获取点击的消息索引
    const messageList = this.data.messageList;
    
    // 遍历消息列表，将所有消息的 expanded 设为 false
    messageList.forEach((item, i) => {
      if (i !== index) {
        item.expanded = false;
      }
    });
    
    // 切换当前点击消息的 expanded 状态
    messageList[index].expanded = !messageList[index].expanded;
    
    this.setData({
      messageList,
    });
  },
});