// app.js
App({
  globalData: {
    baseUrl: 'http://localhost:8090/api' // 全局请求地址
  },

  /**
   * 封装的请求方法
   * @param {Object} options 请求配置
   * @param {string} options.url 请求路径（相对路径）
   * @param {string} [options.method='GET'] 请求方法，默认为 GET
   * @param {Object} [options.data={}] 请求参数
   * @param {Object} [options.header={}] 请求头
   * @param {Function} [options.success] 请求成功回调
   * @param {Function} [options.fail] 请求失败回调
   * @param {Function} [options.complete] 请求完成回调
   */
  request(options) {
    const { url, method = 'GET', data = {}, header = {} } = options;

    // 请求拦截器：添加全局请求头（如 token）
    const token = wx.getStorageSync('token');
    if (token) {
      header['token'] = token;
    }

    // 发起请求
    wx.request({
      url: this.globalData.baseUrl + url, // 拼接完整请求地址
      method: method.toUpperCase(), // 统一转为大写
      data: data,
      header: header,
      success: (res) => {
        // 响应拦截器：统一处理响应
        if (res.statusCode === 401) {
          // 未授权，跳转到登录页
          wx.navigateTo({ url: '/pages/login/login' });
        } else if (options.success) {
          options.success(res); // 调用成功回调
        }
      },
      fail: (err) => {
        if (options.fail) {
          options.fail(err); // 调用失败回调
        }
      },
      complete: (res) => {
        if (options.complete) {
          options.complete(res); // 调用完成回调
        }
      }
    });
  }
});