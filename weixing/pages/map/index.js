
const app = getApp()
export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  data: {
    longitude: 119.01041700380165, //地图界面中心的经度
    latitude: 25.443096540677832, //地图界面中心的纬度
    stream: "",
    markers: [ //标志点的位置

    ],
    content_money : "1000",
    customCallout: {
      anchorX: 0,
      anchorY: 0,
      display: "ALWAYS"
    },
    circles : [

    ],
    liveUrl: "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4"
  },
  onLoad: function () {
    var that = this
    wx.getLocation({
      type: "gcj02",
      success: function (res) {
        let latitude = res.latitude
        let longitude = res.longitude
        that.setData({
          latitude , longitude
        })
        console.log("打印位置信息");
        console.log(longitude);
        console.log(latitude);


    // 上传位置信息搜索附近摄像头以及搜索范围
      wx.request({
        url: `${devConfig.url}/map/getInfo`,//MaterialEntryAdd 后端接口名称
        method: "POST",
        data: {
          "longitude": longitude,
          "latitude" : latitude,
          "userName" : wx.getStorageSync('userName')
        },// 向后端传参
        success:(res)=>{
          console.log(res.data)
          wx.showToast({  //成功获取点位信息
            title: '获取成功',
            icon: 'success',
            duration: 2500
          })
          // 遮罩
          var circles = [];
          // 标记点位
          var markers = [];
          // 设置摄像头点位
          var i = 0
          res.data.result.cameraList.forEach(camera => {
              const marker = {
                id: i++,
                iconPath: "../../images/1.png",
                latitude: camera.latitude,
                longitude: camera.longitude,
                streamUrl: camera.streamUrl,
                srcUser: camera.srcUser,
                width: 28,
                height: 32,
                callout: {
                  bgColor: '#F65C43',
                  borderRadius: '10',
                  color: '#ffffff',
                  content: '点位' + i,
                  display: 'ALWAYS',
                  fontSize: '14',
                  padding: '5',
                  textAlign: 'center'
                }
              }
              markers.push(marker)
          });
          console.log(markers);
          const circle = {
            // 这里设置当前用户位置
            longitude: longitude,
            latitude : latitude,
            color: '#FF0000DD',
            fillColor: '#7cb5ec88',
            radius: res.data.result.circle,
            strokeWidth: 1
          }
          circles.push(circle)
          that.setData(
            {
              markers,
              circles
            }
          )
        },
        fail: (err) => {
            wx.showToast({  //显示获取失败信息
            title: '获取失败！！',
            icon: 'error',
            duration: 2500
          })
          console.log(err);
        }
      });
  }
    })
  },
  onReady: function () {
 //获得popup组件
  this.popup = this.selectComponent("#popup");
  },

  /**
   * 地图放大缩小事件触发
   * @param {*} e 
   */
  bindregionchange(e) {
    console.log('=bindregiοnchange=', e)
  },

  /**
   * 点击点位事件，播放本地视频流模拟监控摄像头
   * @param {*} e 
   */
  bindtapMap(e) {

    console.log('=bindtapMap=', e.detail)
  },
  bindcallouttapFunc(e){
    console.log(this.data.markers[e.detail.markerId]);
    this.selectComponent('#popup').showPopup(this.data.markers[e.detail.markerId].streamUrl , this.data.markers[e.detail.markerId].longitude , this.data.markers[e.detail.markerId].latitude , this.data.markers[e.detail.markerId].srcUser)
  },
  masktap(){
  },
   //取消事件
   _close() {
    console.log('你点击了关闭按钮');
    this.popup.hidePopup();
  },
  statechange(e) {
    console.log('live-player code:', e.detail.code)
  },
  error(e) {
    console.error('live-player error:', e.detail.errMsg)
  }
})
