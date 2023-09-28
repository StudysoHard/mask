// {{page}}.js
//index.js
//获取应用实例
const app = getApp()
 let username=''
 let password=''
 export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};

Page({
  onLoad(){
    var that=this
    wx.getSystemInfo({ 
      success: function (res) { 
        console.log(res.windowHeight)
          that.setData({ 
              clientHeight:res.windowHeight
        }); 
      } 
    }) 
  },
  //协议
  goxieyi(){
   wx.navigateTo({
     url: '/pages/map/index',
   })
  },
  //获取输入款内容
  content(e){
    username=e.detail.value
  },
  password(e){
    password=e.detail.value
  },
  //登录事件
  goadmin(){
    let flag = false  //表示账户是否存在,false为初始值
    if(username=='')
    {
      wx.showToast({
        icon:'none',
        title: '账号不能为空',
      })
    }else if(password==''){
      wx.showToast({
        icon:'none',
        title: '密码不能为空',
      })
    }else{

      wx.request({
        url: `${devConfig.url}/login`,//MaterialEntryAdd 后端接口名称
        method: "POST",
        data: {
          "userName": username,
          "password" : password
        },// 向后端传参
        success:(res)=>{
          if(res.data.code == 200){
            wx.showToast({  //显示登录成功信息
              title: '登陆成功！！',
              icon: 'success',
              duration: 2500
            })
            wx.setStorageSync('userName', username)
            //判断登录状态
            if(res.data.result == 0){
              //未登录跳转到fine进行上班人脸拍摄
              wx.redirectTo({
                url: '../fine/index',
              })
            } else {
              //已经登录直接跳转首页
              wx.switchTab({
                url: '../map/index',
              })
            }
          } else {
            wx.showToast({  //显示登录成功信息
              title: res.message,
              icon: 'error',
              duration: 2500
            })
          }
        },
        fail: (err) => {
            wx.showToast({  //显示登录失败信息
            title: '登陆失败！！',
            icon: 'error',
            duration: 2500
          })
          console.log(err);
        }
      });
    }
  },
})
 
