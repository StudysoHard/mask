<template>
  <div class="map">
    <div id="container"></div>
    <ul class="btn-wrap" style="z-index: 99;">
      <li class="btn btnbig" @click="clickBackHome">返回数据面板</li>
    </ul>
  </div>
</template>

<script>
import { request } from '../network/request';

export default {
  data() {
    return {
      // map: null,
      url: "http://localhost:5002/url?url=",
      inits : 0,
      timer: null,
      markerlist: [
        // {
        //   latitude: 119.01467,
        //   longitude: 25.44717,
        //   streamurl: "rtsp://192.168.43.1:8554/live"
        // },
        // {
        //   latitude: 119.01567,
        //   longitude: 25.44717,
        //   streamurl: "0"
        // },
        // {
        //   latitude: 119.01750,
        //   longitude: 25.45160,
        //   streamurl: "0"
        // },
        // {
        //   latitude: 119.01750,
        //   longitude: 25.45120,
        //   streamurl: "0"
        // },
        // {
        //   latitude: 119.01750,
        //   longitude: 25.45190,
        //   streamurl: "0"
        // },
      ],
    };
  },
  mounted() {
    this.updateSocketData()
    // this.getMap();
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
      // request({
      //   url: 'http//localhost:5002/url?url=0',
      //   method: 'get'
      // })
      // 创建视频信息窗口，后面使用转义字符拼接！！！
      let sContent = `<div class="sContent"><img  id='imgplayer' src=` + url + 0 + ` /></div>
      <div class="b">
        <li class="btn " id="ex-btn">展开</li>
        <li class="btn " id="tr-btn">转换</li>
        <li class="btn " id="sh-btn">缩小</li>
      </div>`;
      for (let i = 0; i < markerlist.length; i++) {
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
          openInfo(this.init++,sContent, e);
        });
      };
      //打开信息窗口
      function openInfo(inits,sContent, e) {
        console.log(inits);
        if(inits == 0) {
          inits = 1
        } else {
        let p = e.target;
        let point = new BMapGL.Point(p.getPosition().lng, p.getPosition().lat);
        console.log(p.streamurl);
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
      }
    },
    clickBackHome() {
      this.$router.push("/screenpage");
    },
    updateSocketData() {
      let flag = false;
      console.log("this.socket: " + this.$socket.socketData);
      //每隔1s检查数据是否更新
      this.timer = setInterval(() => {
        //从后端获取数据
        let points = this.$socket.socketData.cameraInfos;
        // console.log(points);
        for(let i=0; i<points.length; i++) {
          let latitude = parseFloat(points[i].cameraPosition.split(",")[0]);
          let longitude = parseFloat(points[i].cameraPosition.split(",")[1]);
          let streamurl = points[i].streamUrl;
          this.markerlist[i] = { "latitude": latitude, "longitude": longitude, "streamurl":streamurl }
        }
        // console.log(this.markerlist)
        if(!flag) {
          // 没有初始化
          if(points.length != 0) {
            console.log("init");
            this.getMap();
            flag = true
          } 
        }else {
            if(points.length != this.markerlist.length) {
              console.log("update");
              // 数据更新
              this.getMap()
            }
        }
      }, 1000)
    }
  },
}
</script>

<style>



.map {
  width: 100%;
  height: 100%;
  
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
  margin-top: 4px;
  width: auto;
  height: 20px;
  float: left;
  background-color: #fff;
  color: rgba(27, 142, 236, 1);
  font-size: 8px;
  border: 1px solid rgba(27, 142, 236, 1);
  border-radius: 5px;
  text-align: center;
  line-height: 20px;
}

.btn:hover {
  background-color: rgba(27, 142, 236, 0.8);
  color: #fff;
  cursor: pointer;
}
.btn-wrap .btnbig {
  margin-left: 45%;
  width: 200px;
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
  width: 100%;
  padding: 5px;
  border-radius: 5px;
  background-color: rgba(265, 265, 265, 0.9);
  box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
}
</style>