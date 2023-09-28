// {{page}}.js
export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};

Page({

  /**
   * 页面的初始数据
   */
  data: {
    streamUrl: "",
    code: "",
    time: 3,  
    timeId: null,  
    hiddenmodalput:true,
    isShowConfirm:false,
    isShowConfirm_info: false,
    isShowConfirm_stream:false,
    newName: "",
    newPassword: "",
    newTelephone: "",
    originalPassword: "",
    newPassword: "",
    latitude: "",
    longitude: "",
    tencent: "向管理员发送升级权限申请",
    cameraList: [],
    cameraIndex: 0
  },

  onReady: function () {
    wx.request({
      url: `${devConfig.url}/camera/list?userName=`+wx.getStorageSync('userName'),//MaterialEntryAdd 后端接口名称
      method: "POST",// 向后端传参
      success:(res)=>{
        if(res.data.code == 200){
            // 收到返回的数据
            this.setData({
              cameraList: res.data.result
            })
        } else {
          // 收到返回的数据
          wx.showToast({  //显示发送图片失败的信息
            title: res.data.message,
            icon: 'error',
            duration: 5000
          })
        }
      },
      fail: (err) => {
          wx.showToast({  //显示发送图片失败的信息
          title: '获取申请列表失败',
          icon: 'error',
          duration: 5000
        })
        console.log(err);
      }
    });
  },
//输入框中的值
setValue: function (e) {
  this.setData({
    walletPsd: e.detail.value
  })
},
//点击取消按钮
cancels:function () {
  var that = this
  that.setData({
    isShowConfirm: false,
  })
},
//接入摄像头获取输入框中的值
streamLongitude: function (e) {
  this.setData({
    longitude: e.detail.value
  })
},
//接入摄像头获取输入框中的值
streamLatitude: function (e) {
  this.setData({
    latitude: e.detail.value
  })
},
//接入摄像头获取输入框中的值
streamUrl: function (e) {
  this.setData({
    streamUrl: e.detail.value
  })
},

// 输入的名称
newName: function (e) {
  this.setData({
    newName: e.detail.value
  })
},
// 输入的密码
newPassword: function (e) {
  this.setData({
    newPassword: e.detail.value
  })
},
// 输入的电话号码
newTelephone: function (e) {
  this.setData({
    newTelephone: e.detail.value
  })
},
// 原密码
originalPassword: function (e) {
  this.setData({
    originalPassword: e.detail.value
  })
},
newPassword: function(e){
  this.setData({
    newPassword: e.detail.value
  })
},

//点击取消按钮
cancels_stream:function () {
  var that = this
  that.setData({
    isShowConfirm_stream: false,
  })
},
//点击取消按钮
cancels_info:function () {
  var that = this
  that.setData({
    isShowConfirm_info: false,
  })
},
//点击确认按钮
confirmAcceptance:function(){


},
//接入摄像头确认按钮
confirmAcceptance_stream:function(){
  wx.request({
    url: `${devConfig.url}/camera/add`,//MaterialEntryAdd 后端接口名称
    method: "POST",// 向后端传参
    data: {
      "longitude" : this.data.longitude,
      "latitude" : this.data.latitude,
      "streamUrl" : this.data.streamUrl,
      "userName" : wx.getStorageSync('userName')
    },
    success:(res)=>{
      // 收到返回的数据
        wx.showToast({  //显示发送图片失败的信息
          title: '摄像头点位添加成功',
          icon: 'success',
          duration: 2500
        })

    },
    fail: (err) => {
        wx.showToast({  //显示发送图片失败的信息
        title: '申请权限失败',
        icon: 'error',
        duration: 5000
      })
      console.log(err);
    }
  });

  var that = this
  that.setData({
    isShowConfirm_stream: false,
  })
},
// 弹窗方法 
showTips(n) {
  // 调用 uni.showToast() 方法，展示提示消息
  wx.showToast({
    icon:'none',   // 不展示任何图标
    duration:1500,  // 1.5 秒后自动消失
    mask:true,   // 为页面添加透明遮罩，防止点击穿透
    title:'申请权限成功' + n + ' 秒后自动跳转到登录页',
  })
} ,
//用户修改信息的确认按钮
confirmAcceptance_info:function(){
  wx.request({
    url: `${devConfig.url}/user/update`,//MaterialEntryAdd 后端接口名称
    method: "POST",// 向后端传参
    data: {
      "originalName" : wx.getStorageSync('userName'),
      "changeName" : this.data.newName,
      "telephone" : this.data.newTelephone,
      "originalPassword" : this.data.originalPassword,
      "newPassword": this.data.newPassword

    },
    success:(res)=>{
      console.log(res);
      if(res.data.code == 200){
        // 更新数据成功 输出提示信息
        wx.showToast({ 
          title: res.data.message,
          icon: 'success',
          duration: 2500
        })
      // 3秒自动跳转到登录界面
        this.delayTime()
      }
      var that = this
      that.setData({
        isShowConfirm_info: false,
      })
      // 错误提示信息
      wx.showToast({ 
        title: res.data.message,
        icon: 'error',
        duration: 5000
      })
      
    },
    fail: (err) => {
      wx.showToast({  //显示发送图片失败的信息
        title: 'http请求发送失败',
        icon: 'error',
        duration: 5000
      })
      var that = this
      that.setData({
        isShowConfirm_info: false,
      })
    }
  });
},

// 专门用来展示倒计时以及页面跳转的方法
delayTime() {
  // 1.每次点击结算,都需要将倒计时秒数还原
  this.time = 3
  // 2.展示倒计时弹框
  this.showTips(this.time)
  // 3.使用setInterval 实现倒计时
  this.timeId = setInterval(() => {
       // 3.1 秒数每次减1
    this.time -= 1
       // 3.3 做一个判断,如果秒数为0的话清除定时器
    if(this.time <= 0) {
      clearInterval(this.timeId)
       // 3.4 进行页面跳转
      wx.redirectTo({
        url:'/pages/login/index'
      })
      return
    }
    // 3.2  定时器每循环一次调一次弹窗
    this.showTips(this.time)
  },1000)
},

//拒绝
close(e) {
  this.setData({
    isShowConfirm: true,
  }) 
},
//拒绝
close_stream(e) {
  this.setData({
    isShowConfirm_stream: true,
  }) 
},
//拒绝
close_info(e) {
  this.setData({
    isShowConfirm_info: true,
  }) 
},
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  onLineChat: function(){
    wx.redirectTo({
      url: '../onLineChat/index',
    })
  },
  logout: function(){
    wx.redirectTo({
      url:'/pages/login/index'
    })
  },
  toClock(e){
    var that=this
       that.setData({
         hiddenmodalput: !this.data.hiddenmodalput
      })
  },
    cancel: function(){
      this.setData({
          hiddenmodalput: true
      });
    },
    //确认
    confirm: function(e){
      this.setData({
        hiddenmodalput: true
    });
      wx.request({
        url: `${devConfig.url}/request/up?userName=`+ wx.getStorageSync('userName'),//MaterialEntryAdd 后端接口名称
        method: "POST",// 向后端传参
        success:(res)=>{
          console.log(res);
          if(res.data.code != 200){
            // 收到返回的数据
            wx.showToast({  //显示发送图片失败的信息
              title: res.data.message,
              icon: 'error',
              duration: 5000
            })
          } else{
            // 收到返回的数据
            wx.showToast({  //显示发送图片失败的信息
              title: '申请权限成功',
              icon: 'success',
              duration: 2500
            })
          }
        },
        fail: (err) => {
            wx.showToast({  //显示发送图片失败的信息
            title: '申请权限失败',
            icon: 'error',
            duration: 5000
          })
          console.log(err);
        }
      });
    },
    bindPickerChange: function (e) {
      wx.request({
        url: `${devConfig.url}/request/camera`,//MaterialEntryAdd 后端接口名称
        method: "POST",// 向后端传参
        data: {
          "userName" : wx.getStorageSync('userName'),
          "cameraId" : this.data.cameraList[e.detail.value]
        },
        success:(res)=>{
          console.log(res.data);
          if(res.data.code != 200){
              // 收到返回的数据
              wx.showToast({ 
                title: '申请摄像头成功',
                icon: 'success',
                duration: 2500
              })
          } else {
            // 收到返回的数据
            wx.showToast({  
              title: res.data.message,
              icon: 'error',
              duration: 5000
            })
          }
        },
        fail: (err) => {
            wx.showToast({  
            title: '申请权限失败',
            icon: 'error',
            duration: 5000
          })
          console.log(err);
        }
      });
      this.setData({
        cameraIndex: e.detail.value
      })
    },
    refList: function(){
      // 跳转到图表页面
      wx.redirectTo({
        url: '../../template/requestList/index',
      })  
    }
})