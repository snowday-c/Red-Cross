const app = getApp();

Page({
  /**
   * 页面的初始数据
   */
  data: {
    questions: [], // 题目列表
    userAnswers: [], // 用户答案
    countdown: 60 * 60, // 60分钟的倒计时，单位为秒
    timer: null, // 定时器 ID
    formattedTime: '60:00', // 格式化后的时间
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    // 从本地缓存中获取题目
    const questions = wx.getStorageSync('questions');
    this.setData({
      questions,
      userAnswers: new Array(questions.length).fill(''), // 初始化用户答案数组
      formattedTime: this.formatTime(60 * 60), // 初始化格式化后的时间
    });

    // 启动倒计时
    this.startCountdown();
  },

  /**
   * 启动倒计时
   */
  startCountdown() {
    const timer = setInterval(() => {
      let countdown = this.data.countdown;
      if (countdown <= 0) {
        clearInterval(timer); // 倒计时结束，清除定时器
        this.submitAnswers(); // 自动提交答案
        return;
      }
      countdown -= 1;
      this.setData({
        countdown,
        formattedTime: this.formatTime(countdown), // 更新格式化后的时间
      });
    }, 1000);

    this.setData({
      timer,
    });
  },

  /**
   * 格式化时间为 MM:SS 格式
   */
  formatTime(seconds) {
    if (seconds === undefined || seconds === null) {
      return '00:00'; // 默认值
    }
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
    if (this.data.timer) {
      clearInterval(this.data.timer); // 清除定时器
    }
  },

  /**
   * 处理选择题和判断题的选择
   */
  handleRadioChange(e) {
    const index = e.currentTarget.dataset.index; // 题目索引
    const value = e.detail.value; // 用户选择的答案
    const userAnswers = this.data.userAnswers;
    userAnswers[index] = value; // 更新用户答案
    this.setData({
      userAnswers,
    });
  },

  /**
   * 处理填空题的输入
   */
  handleInputChange(e) {
    const index = e.currentTarget.dataset.index; // 题目索引
    const value = e.detail.value; // 用户输入的答案
    const userAnswers = this.data.userAnswers;
    userAnswers[index] = value; // 更新用户答案
    this.setData({
      userAnswers,
    });
  },

  /**
   * 提交答案
   */
  submitAnswers() {
    const { userAnswers } = this.data;

    // 将答案数组转换为 JSON 字符串
    const answersJson = JSON.stringify(userAnswers);

    // 发送答案到后端
    app.request({
      url: '/question/submit',
      method: 'POST',
      header: {
        'Content-Type': 'application/json',
      },
      data: {
        userId: wx.getStorageSync('userInfo').userId,
        answers: answersJson, // 发送 JSON 字符串
      },
      success: (res) => {
        if (res.data.code === '0') {
          wx.showToast({
            title: '提交成功',
            icon: 'success',
          });
          wx.setStorageSync('score', res.data.data);
          // 跳转到成绩页面或其他页面
          wx.redirectTo({
            url: '/pages/examResult/examResult',
          });
          wx.removeStorageSync('questions');
        } else {
          wx.showToast({
            title: res.data.message || '提交失败',
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
});