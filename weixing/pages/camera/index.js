// {{page}}.js
const app = getApp()
export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  toMsgDetail(e) {
    wx.navigateTo({
      url: '',
    })
  },
  /**
   * 页面的初始数据
   */
  data: {
    msgList: [
      { url: 'url', title: '公告：ChatGPT-4的出现标志着人工智能进入新阶段' },
      { url: 'url', title: '公告：震惊一男子竟然一周不洗袜子，原因感人' },
      { url: 'url', title: '公告：强人工智能，万物互联，深度潜行都将不是梦想' },
    ],
    msgSet: {
      vertical: true, //滑动方向是否为纵向
      autoplay: true, //是否自动切换
      interval: 2000, //自动切换时间间隔
      duration: 500, //滑动动画时长
      circular: true, //是否采用衔接滑动
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  beginXunjian: function(){

    wx.getLocation({
      type: "gcj02",
      success: function (res) {
        let latitude = res.latitude
        let longitude = res.longitude
        wx.request({
          url: `${devConfig.url}/xunjian/begin` ,
          method: "POST",
          data: {
            "userName": wx.getStorageSync('userName'),
            "latitude" : latitude,
            "longitude" : longitude
          },
          success:(res)=>{
            // 设置图片解析地址
            console.log(res.data.result);
            wx.setStorageSync('xunJianId', res.data.result)
          },
          fail: (err) => {
              wx.showToast({  //显示获取上一张图片失败的信息
              title: '获取图片失败！！',
              icon: 'error',
              duration: 2500
            })
            console.log(err);
          }
        });

  }
    })



    // 跳转到xunjian页面
    wx.redirectTo({
      url: '../xunjian/index',
    })
  },
  historyImg : function(){
    // 跳转到historyImg页面
    wx.redirectTo({
      url: '../historyImg/index',
    })
  },
  redirectCharts: function(){
    // 跳转到图表页面
    wx.redirectTo({
      url: '../../template/ucharts/index',
    })
  }
})