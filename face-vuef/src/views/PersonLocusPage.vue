<template>
  <div>
    <el-container>
      <el-header></el-header>
      <el-container>
        <el-aside>
        </el-aside>
        <el-main>
          <div id="container"></div>
          <ul class="btn-wrap" style="z-index: 99;">
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
import { request } from '../network/request';

export default {
  data() {
    return {
      // map: null,
      url: "http://localhost:5002/url?url=",
      timer: null,
      imgPath: null,   //图片
      faceName: null, //编号
      time: null,   // 被拍摄时间
      isCOVID: null,  //是否是新冠患者
      info: '',   //包含时间的个人轨迹信息

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
      ]
    };
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
      let url = this.url;
      let markerlist = this.markerlist;
      let map = new BMapGL.Map("container");          // 创建地图实例
      window.map = map;                               //将map变量存储在全局，其他方法才能使用
      let point = new BMapGL.Point(119.01609, 25.45164);  // 创建点坐标 
      map.centerAndZoom(point, 19);                 // 初始化地图，设置中心点坐标和地图级别
      map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
      map.setHeading(64.5);   //设置地图旋转角度
      map.setTilt(73);       //设置地图的倾斜角度
      // 创建视频信息窗口，后面使用转义字符拼接！！！
      let sContent = `<div class="sContent"><img  id='imgplayer' src=` + url + markerlist[0].streamurl + ` /></div>
      <div class="b">
        <li class="btn " id="ex-btn">展开</li>
        <li class="btn " id="tr-btn">转换</li>
        <li class="btn " id="sh-btn">缩小</li>
      </div>`;
      for(let i=0; i<markerlist.length; i++) {
        // 创建点标记
        let marker = new BMapGL.Marker(new BMapGL.Point(markerlist[i].latitude, markerlist[i].longitude));
        // 给每个摄像头绑定视频流
        marker.streamurl = markerlist[i].streamurl;
        // console.log(marker);
        // 在地图上添加点标记
        map.addOverlay(marker);
        addClickHandler(sContent, marker);
      }
      //自动播放的视频
      // 给点标记添加点击事件
      function addClickHandler(sContent, marker) {
        marker.addEventListener("click", function (e) {
          openInfo(sContent, e);
        });
      };
      //打开信息窗口
      function openInfo(sContent, e) {
        let p = e.target;
        let point = new BMapGL.Point(p.getPosition().lng, p.getPosition().lat);
        // console.log(p.streamurl);
        // 点击某摄像头，把该摄像头的视频流地址传给后端
        let porthandle = '/api/camera_change?url=' + p.streamurl;
        request({
          url: porthandle,
          method: 'get'
        })
        let infoWindow = new BMapGL.InfoWindow(sContent);  // 创建信息窗口对象 
        map.openInfoWindow(infoWindow, point); //开启信息窗口
        //判断窗口的打开状态
        if (!infoWindow.isOpen()) {
          //如果没有打开，则监听打开事件，获取按钮，添加事件
          infoWindow.addEventListener("open", function () {
            document.getElementById("ex-btn").onclick = function (e) {
              alert("aa");
            }
            document.getElementById("tr-btn").onclick = function (e) {
              // //设置信息窗口内容
              // p.setContent()
              alert("ab");
            }
            document.getElementById("sh-btn").onclick = function (e) {
              alert("ac");
            }
          })
        } else {//如果已经打开，直接获取按钮，添加事件
          document.getElementById("ex-btn").onclick = function (e) {
            alert("aa");
          }
          document.getElementById("tr-btn").onclick = function (e) {
            alert("ab");
          }
          document.getElementById("sh-btn").onclick = function (e) {
            alert("ac");
          }
        }
      }
    },
    updateSocketData() {
      
      //把人脸名字传给后端获得该人轨迹信息
      request({
        url: 'http://www.heyongqiang.work:8979/xunjian/move/' + this.$route.query.id,
        method: "get"
      }).then((res) => {
        let path = res.result;
        this.info = res.result;
        // console.log(this.imgPath)
        for (let i = 0; i < path.length; i++) {
          let latitude = parseFloat(path[i].longitude);
          let longitude = parseFloat(path[i].latitude);
          this.path[i] = { "latitude": latitude, "longitude": longitude }
        }
    
        // if (path.length == 1) {
        //   // 只有一个防止卡死
        //   this.path[1] = this.path[0]
        // }
      })
      let flag = false
      //每隔1s检查数据是否更新
      this.timer = setInterval(() => {
        //从后端获取数据
        let points = this.$socket.socketData.cameraInfos;
        // console.log(points);
        for (let i = 0; i < points.length; i++) {
          let latitude = parseFloat(points[i].cameraPosition.split(",")[0]);
          let longitude = parseFloat(points[i].cameraPosition.split(",")[1]);
          let streamurl = points[i].streamUrl;
          this.markerlist[i] = { "latitude": latitude, "longitude": longitude, "streamurl": streamurl, "id": points[i].id }
        }
        //flag控制只初始化地图和轨迹一次
        if (!flag) {
          // 没有初始化
          if (points.length != 0) {
            console.log("init");
            this.getMap();
            this.getTrack()
            flag = true
          }
        } else {
          //如果数据更新再对地图和轨迹进行更新
          if (points.length != this.markerlist.length) {
            console.log("update");
            // 数据更新
            this.getMap()
            this.getTrack()
          }
        }
      }, 1000)
    },
    getTrack() {
      //轨迹
      let path = this.path;
      let points = [];
      for (let i = 0; i < path.length; i++) {
        points.push(new BMapGL.Point(path[i].latitude, path[i].longitude));
      }
      let pl = new BMapGL.Polyline(points);
      let trackAni = new BMapGLLib.TrackAnimation(map, pl, {
        overallView: true,
        tilt: 30,
        duration: 20000,
        delay: 300
      });
      window.trackAni = trackAni;
      // 轨迹开始
      function start() {
        trackAni.start();
      }
      start();
    },
     // 轨迹暂停
    pauseAni() {
      trackAni.pause();
    },
    // 轨迹继续
    continueAni() {
      trackAni.continue();
    }
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
  width:1px !important;
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
/* 地图 */
#container {
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
  margin-top:10px;
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
  top:45px;
  left: 105px
}
.el-row {
  margin-top: 25px;
  margin-left: 5px
}
.right {
  color: #292c2d;
}
.locus {
  margin-left: 30px;
}
</style>