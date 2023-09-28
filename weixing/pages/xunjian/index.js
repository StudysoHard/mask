// camera.js
var imgId = ""
var numbers = 0
var newDateTime = ""
export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  data : {
    imgResole : "",
    nomask: 0,
    mask: 0,
    latitude: 0,
    longitude: 0
  },
  onLoad(){
    newDateTime = (new Date()).valueOf();
  },
  takePhoto(e) {
    var that = this
    const ctx = wx.createCameraContext()
    ctx.takePhoto({
      quality: 'high',
      success: (res) => {

        wx.getLocation({
          type: "gcj02",
          success: function (result) {
            let latitude = result.latitude
            let longitude = result.longitude
            that.setData({
              latitude , longitude
            })
            console.log("打印位置信息");
            console.log(longitude);
            console.log(latitude);
        // 拍摄成功 将图片转化为base64发送到python服务器中
        wx.getFileSystemManager().readFile({
          filePath: res.tempImagePath,// 图片地址 本地的临时图片
          encoding: "base64",
          success: base => {
            
            // 将转化完成的base64发送到小程序后端
            wx.request({
              url: `${devConfig.url}/img/base64Img`,//MaterialEntryAdd 后端接口名称
              method: "POST",
              data: {
                "img": base.data, // base.data就是转化之后的图片
                "name" : wx.getStorageSync('userName'),
                "index" : numbers++,
                "xunJianId" : wx.getStorageSync('xunJianId'),
                "longitude": longitude,
                "latitude": latitude

              },// 向后端传参
              success:(res)=>{
                console.log(res.data);
                // 收到返回的数据
                that.setData({
                  nomask: that.data.nomask + res.data.result.nomask_human,
                  mask: that.data.mask + res.data.result.mask_human
                })  
                imgId = res.data.result.id
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
          fail: base => {
            console.log("base64 转化失败");
          }
        })
      }
    })



        
        // this.setData({
        //   src: res.tempImagePath
        // })
      }
    })
  },
  getPrePhoto(){
    // 请求上一次请求识别的图片
    wx.request({
      url: `${devConfig.url}/img/getPreImg?id=` + imgId ,//MaterialEntryAdd 后端接口名称
      method: "GET",
      success:(res)=>{
        // 设置图片解析地址
        this.setData({
          imgResole : res.data.result
        })
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
  },
  error(e) {
    console.log(e.detail)
  },
  exitXunJian(){
    wx.request({
      url: `${devConfig.url}/xunjian/end` ,//MaterialEntryAdd 后端接口名称
      method: "POST",
      data:{
        "xunJianId" : wx.getStorageSync('xunJianId'),
        "noMask" : this.data.nomask,
        "mask" : this.data.mask
      },
      success:(res)=>{
        // 退出巡检 弹出框显示本次的巡检结果
        wx.showModal({
          title: '退出巡检',
          content: '未佩戴口罩总人数:' + this.data.nomask + ' \n 佩戴口罩人数:' + this.data.mask,
          success (res) {
            if (res.confirm) {
              console.log('用户点击确定')
              wx.switchTab({
                url: '../map/index',
              })
            } else if (res.cancel) {
              console.log('用户点击取消')
            }
          }
        })
      },
      fail: (err) => {
          wx.showToast({  //显示获取上一张图片失败的信息
          title: '结束巡检失败',
          icon: 'error',
          duration: 2500
        })
        console.log(err);
      }
    });



  }
})