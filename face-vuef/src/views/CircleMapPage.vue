<template>
  <div>
    <el-container>
      <el-header></el-header>
      <el-container>
         <el-aside>
        </el-aside>
        <el-main>
          <ul class="drawing-panel">
            <li class="bmap-btn bmap-marker" id="marker"></li>
            <li class="bmap-btn bmap-polyline" id="polyline"></li>
            <li class="bmap-btn bmap-rectangle" id="rectangle"></li>
            <li class="bmap-btn bmap-polygon" id="polygon" ></li>
            <li class="bmap-btn bmap-circle" id="circle" @click="draw($event)"></li>
          </ul>
          <div id="container"></div>
          <ul class="btn-wrap" style="z-index: 99">
            <li class="btn btn1" @click="pauseAni()">暂停</li>
            <li class="btn" @click="continueAni()">继续</li>
            <router-link to="/screenpage">
              <li class="btn" @click="continueAni()">首页</li>
            </router-link>
          </ul>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { request } from '../network/request'

export default {
  data() {
    return {
      // map: null,
      url: 'http://localhost:5002/url?url=',
      timer: null,
      imgPath: null, //图片
      faceName: null, //编号
      time: null, // 被拍摄时间
      isCOVID: null, //是否是新冠患者
      info: '', //包含时间的个人轨迹信息
      drawingManager: {},
      markerlist: [
        {
          // latitude: 119.01467,
          // longitude: 25.44717,
          // streamurl: "rtsp://192.168.43.1:8554/live"
        },
        {
          // latitude: 119.01567,
          // longitude: 25.44717,
          // streamurl: "0"
        },
        {
          // latitude: 119.01750,
          // longitude: 25.45160,
          // streamurl: "0"
        },
        {
          // latitude: 119.01750,
          // longitude: 25.45120,
          // streamurl: "0"
        },
        {
          // latitude: 119.01750,
          // longitude: 25.45190,
          // streamurl: "0"
        },
      ],
      path: [
        {
          // latitude: 119.01467,
          // longitude: 25.44717
        },
        {
          // latitude: 119.01567,
          // longitude: 25.44717
        },
        {
          // latitude: 119.01750,
          // longitude: 25.45160
        },
      ],
    }
  },
  mounted() {
    this.updateSocketData()
    // this.getMap(),
    // this.getTrack()
  },
  destroyed() {
    clearInterval(this.timer)
  },
  methods: {
    getMap() {
      let url = this.url
      let markerlist = this.markerlist
      let map = new BMapGL.Map('container') // 创建地图实例
      window.map = map //将map变量存储在全局，其他方法才能使用
      let point = new BMapGL.Point(119.01609, 25.45164) // 创建点坐标
      map.centerAndZoom(point, 19) // 初始化地图，设置中心点坐标和地图级别
      map.enableScrollWheelZoom(true) //开启鼠标滚轮缩放
      map.setHeading(64.5) //设置地图旋转角度
      map.setTilt(73) //设置地图的倾斜角度
      // 实例化鼠标工具
      var styleOptions = {
        strokeColor: '#5E87DB', // 边线颜色
        fillColor: '#5E87DB', // 填充颜色。当参数为空时，圆形没有填充颜色
        strokeWeight: 2, // 边线宽度，以像素为单位
        strokeOpacity: 1, // 边线透明度，取值范围0-1
        fillOpacity: 0.2, // 填充透明度，取值范围0-1
      }
      var labelOptions = {
        borderRadius: '2px',
        background: '#FFFBCC',
        border: '1px solid #E1E1E1',
        color: '#703A04',
        fontSize: '12px',
        letterSpacing: '0',
        padding: '5px',
      }
      this.drawingManager = new BMapGLLib.DrawingManager(map, {
        enableCalculate: true,  // 绘制是否进行测距测面
        enableSorption: true,   // 是否开启边界吸附功能
        sorptiondistance: 20,   // 边界吸附距离
        enableGpc: true,        // 是否开启延边裁剪功能
        enableLimit: true,      // 是否开启超限提示
        limitOptions: {
            area: 50000000,     // 面积超限值
            distance: 30000     // 距离超限值
        },
        circleOptions: styleOptions,     // 圆的样式
        polylineOptions: styleOptions,   // 线的样式
        polygonOptions: styleOptions,    // 多边形的样式
        rectangleOptions: styleOptions,  // 矩形的样式
        labelOptions: labelOptions,      // label样式
      })
      this.drawingManager.addEventListener('circlecomplete', function (e, overlay) {
        console.log(overlay)
      })
      // 创建视频信息窗口，后面使用转义字符拼接！！！
      let sContent =
        `<div class="sContent"><img  id='imgplayer' src=` +
        url +
        markerlist[0].streamurl +
        ` /></div>
      <div class="b">
        <li class="btn " id="ex-btn">展开</li>
        <li class="btn " id="tr-btn">转换</li>
        <li class="btn " id="sh-btn">缩小</li>
      </div>`
      for (let i = 0; i < markerlist.length; i++) {
        // 创建点标记
        let marker = new BMapGL.Marker(new BMapGL.Point(markerlist[i].latitude, markerlist[i].longitude))
        // 给每个摄像头绑定视频流
        marker.streamurl = markerlist[i].streamurl
        // console.log(marker);
        // 在地图上添加点标记
        map.addOverlay(marker)
        addClickHandler(sContent, marker)
      }
      //自动播放的视频
      // 给点标记添加点击事件
      function addClickHandler(sContent, marker) {
        marker.addEventListener('click', function (e) {
          openInfo(sContent, e)
        })
      }
      //打开信息窗口
      function openInfo(sContent, e) {
        let p = e.target
        let point = new BMapGL.Point(p.getPosition().lng, p.getPosition().lat)
        // console.log(p.streamurl);
        // 点击某摄像头，把该摄像头的视频流地址传给后端
        let porthandle = '/api/camera_change?url=' + p.streamurl
        request({
          url: porthandle,
          method: 'get',
        })
        let infoWindow = new BMapGL.InfoWindow(sContent) // 创建信息窗口对象
        map.openInfoWindow(infoWindow, point) //开启信息窗口
        //判断窗口的打开状态
        if (!infoWindow.isOpen()) {
          //如果没有打开，则监听打开事件，获取按钮，添加事件
          infoWindow.addEventListener('open', function () {
            document.getElementById('ex-btn').onclick = function (e) {
              alert('aa')
            }
            document.getElementById('tr-btn').onclick = function (e) {
              // //设置信息窗口内容
              // p.setContent()
              alert('ab')
            }
            document.getElementById('sh-btn').onclick = function (e) {
              alert('ac')
            }
          })
        } else {
          //如果已经打开，直接获取按钮，添加事件
          document.getElementById('ex-btn').onclick = function (e) {
            alert('aa')
          }
          document.getElementById('tr-btn').onclick = function (e) {
            alert('ab')
          }
          document.getElementById('sh-btn').onclick = function (e) {
            alert('ac')
          }
        }
      }
    },
    updateSocketData() {
      //把人脸名字传给后端获得该人轨迹信息
      request({
        url: 'http://www.heyongqiang.work:8889/camera/single/move',
        method: 'post',
        data: {
          faceName: this.$route.query.faceName,
          id: 1000,
          isCOVID: this.$route.query.isCOVID,
        },
      }).then((res) => {
        let path = res.data
        this.info = res.data
        this.imgPath = this.$route.query.imgPath
        this.faceName = this.$route.query.people
        this.time = this.$route.query.time
        // this.time = res.data[0].catchTime;
        // console.log(res.data.catchtime)
        this.isCOVID = this.$route.query.isCOVID
        console.log(this.path)
        // console.log(this.imgPath)
        for (let i = 0; i < path.length; i++) {
          console.log(this.path)
          let latitude = parseFloat(path[i].position.split(',')[0])
          let longitude = parseFloat(path[i].position.split(',')[1])
          this.path[i] = { latitude: latitude, longitude: longitude }
        }
        console.log(this.path)
        // if (path.length == 1) {
        //   // 只有一个防止卡死
        //   this.path[1] = this.path[0]
        // }
      })
      let flag = false
      //每隔1s检查数据是否更新
      this.timer = setInterval(() => {
        //从后端获取数据
        let points = this.$socket.socketData.cameraInfos
        // console.log(points);
        for (let i = 0; i < points.length; i++) {
          let latitude = parseFloat(points[i].cameraPosition.split(',')[0])
          let longitude = parseFloat(points[i].cameraPosition.split(',')[1])
          let streamurl = points[i].streamUrl
          this.markerlist[i] = { latitude: latitude, longitude: longitude, streamurl: streamurl, id: points[i].id }
        }
        //flag控制只初始化地图和轨迹一次
        if (!flag) {
          // 没有初始化
          if (points.length != 0) {
            console.log('init')
            this.getMap()
            this.getTrack()
            flag = true
          }
        } else {
          //如果数据更新再对地图和轨迹进行更新
          if (points.length != this.markerlist.length) {
            console.log('update')
            // 数据更新
            this.getMap()
            this.getTrack()
          }
        }
      }, 1000)
    },
    getTrack() {
      //轨迹
      let path = this.path
      let points = []
      for (let i = 0; i < path.length; i++) {
        points.push(new BMapGL.Point(path[i].latitude, path[i].longitude))
      }
      let pl = new BMapGL.Polyline(points)
      let trackAni = new BMapGLLib.TrackAnimation(map, pl, {
        overallView: true,
        tilt: 30,
        duration: 20000,
        delay: 300,
      })
      window.trackAni = trackAni
      // 轨迹开始
      function start() {
        trackAni.start()
      }
      start()
    },
    // 轨迹暂停
    pauseAni() {
      trackAni.pause()
    },
    // 轨迹继续
    continueAni() {
      trackAni.continue()
    },
    // 工具绘画
    draw(e) {
      var arr = document.getElementsByClassName('bmap-btn')
      for (var i = 0; i < arr.length; i++) {
        console.log(arr[i])
        arr[i].style.backgroundPositionY = '0'
      }
      console.log(e.target.id)
      switch (e.target.id) {
        case 'marker': {
          var drawingType = BMAP_DRAWING_MARKER
          break
        }
        case 'polyline': {
          var drawingType = BMAP_DRAWING_POLYLINE
          break
        }
        case 'rectangle': {
          var drawingType = BMAP_DRAWING_RECTANGLE
          break
        }
        case 'polygon': {
          var drawingType = BMAP_DRAWING_POLYGON
          break
        }
        case 'circle': {
          var drawingType = BMAP_DRAWING_CIRCLE
          break
        }
      }
      // 进行绘制
      if (this.drawingManager._isOpen && this.drawingManager.getDrawingMode() === drawingType) {
        // true 开启绘制 false结束绘制
        console.log('绘制关闭')
        this.drawingManager.close()
      } else {
        this.drawingManager.setDrawingMode(drawingType)
        this.drawingManager.open()
      }
        this.drawingManager.addEventListener('overlaycomplete', function(e) {
          alert(e.calculate);
        });
    },
  },
}
</script>

<style scoped>
/* 布局 */
.el-header {
  width: 100%;
  height: 40px !important;
  background-color: #292c2d;
}
.el-aside {
  width: 10px !important;
  height: 600px;
  background-color: #f6f7fb;
}
.el-main {
  position: relative;
  padding: 0 !important;
}
/* 信息窗口显示 */
.sContent {
  width: 600px;
  height: 300px;
}
.b {
  width: 202px;
  height: 30px;
}
#imgplayer {
  width: 100%;
  height: 100%;
}
.btn {
  /* width: 40px; */
  height: 25px;
  float: left;
  background-color: #fff;
  color: rgba(27, 142, 236, 1);
  font-size: 8px;
  border: 1px solid rgba(27, 142, 236, 1);
  border-radius: 10px;
  text-align: center;
  line-height: 20px;
}
.btn1 {
  margin-left: 40%;
}
.btn:hover {
  background-color: rgba(27, 142, 236, 0.8);
  color: #fff;
  cursor: pointer;
}
.drawing-panel {
  z-index: 999;
  position: fixed;
  bottom: 3.5rem;
  margin-left: 2.5rem;
  padding-left: 0;
  border-radius: 0.25rem;
  height: 47px;
  box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
}

.bmap-btn {
  border-right: 1px solid #d2d2d2;
  float: left;
  width: 64px;
  height: 100%;
  background-image: url(//api.map.baidu.com/library/DrawingManager/1.4/src/bg_drawing_tool.png);
  cursor: pointer;
}
.drawing-panel .bmap-marker {
  background-position: -65px 0;
}
.drawing-panel .bmap-polyline {
  background-position: -195px 0;
}
.drawing-panel .bmap-rectangle {
  background-position: -325px 0;
}
.drawing-panel .bmap-polygon {
  background-position: -260px 0;
}
.drawing-panel .bmap-circle {
  background-position: -130px 0;
}

/* 地图 */
#container {
  /**new */
  width: 100%;
  height: 100%;
}

.btn-wrap {
  z-index: 999;
  position: absolute;
  bottom: 0px;
  left: 0px;
  padding-top: 10px;
  width: 100%;
  height: 40px;
  border-radius: 5px;
  background-color: rgba(265, 265, 265, 0.9);
  box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
}

h2 {
  text-align: center;
  background-color: #eceff0;
}
.el-image {
  margin-top: 10px;
  /* margin-left: 80px; */
  width: 90px;
  height: 90px;
  border-radius: 90px;
}
.block {
  position: relative;
}
span {
  position: absolute;
  top: 45px;
  left: 105px;
}
.el-row {
  margin-top: 25px;
  margin-left: 5px;
}
.right {
  color: #292c2d;
}
.locus {
  margin-left: 30px;
}
</style>
