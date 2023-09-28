// template/requestList/index.js

export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  data: {
    contentList: [[]],
    isShowAll:false
  },
  onLoad: function(e){
    wx.request({
      url: `${devConfig.url}/request/list?userName=`+wx.getStorageSync('userName'),//MaterialEntryAdd 后端接口名称
      method: "POST",
      success:(res)=>{
        console.log(res);
        this.setData({
          contentList: res.data.result
        })
      },
      fail: (err) => {
          wx.showToast({  //显示发送图片失败的信息
          title: '发送图片失败！！',
          icon: 'error',
          duration: 2500
        })
        console.log(err);
      }
    });
  },
  showAll(){
    if(this.data.isShowAll){
      this.setData({
        isShowAll:false
      })
    }else{
      this.setData({
        isShowAll:true
      })
    }
  },
  returnMap(){
    wx.switchTab({
      url: '../../pages/map/index',
    })
  }
})
