// {{page}}.js
export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  data: {
    imageList: []
  },
  retrunHome(){
    wx.redirectTo({
      url: '../../pages/map/index',
    })
  },
  onLoad: function () {
    // 发送请求，获取图片地址列表
    wx.request({
      url: `${devConfig.url}/img/list?userName=` + wx.getStorageSync('userName'),//MaterialEntryAdd 后端接口名称
      method: 'GET',
      success: (res) => {
        // 将图片地址列表保存在 imageList 数组中
        this.setData({
          imageList: res.data.result
        });
      },
    });
    
  },
  returnMap(){
    console.log("点击了");
    wx.switchTab({
      url: '../map/index',
    })
  }

})
