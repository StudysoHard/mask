<!-- 视频播放器 -->
<template>
  <div>
    <el-popover placement="bottom" trigger="click" popper-class="popperbox">
      <div>
        <img :src="url" class="video-play" alt="暂无视频" />
      </div>
      <el-button slot="reference" @click="visible = !visible">click 激活</el-button>
    </el-popover>
    <div id="container"></div>
    <ul class="btn-wrap" style="z-index: 99;">
      <li class="btn" @click="shText()">POI文字</li>
      <li class="btn" @click="shIcon()">POI的Icon</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      url: "http://localhost:4455/stream",
      visible: true,
      map: true
    };
  },
  mounted() {
    this.getMap()
  },
  methods: {
    getMap() {
      this.map = new BMapGL.Map("container");          // 创建地图实例 
      var point = new BMapGL.Point(119.01609, 25.45164);  // 创建点坐标 
      this.map.centerAndZoom(point, 19);                 // 初始化地图，设置中心点坐标和地图级别
      this.map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
      this.map.setHeading(64.5);   //设置地图旋转角度
      this.map.setTilt(73);       //设置地图的倾斜角度
      this.map.setDisplayOptions({
        poi: true       //是否显示POI信息 
      })
    },
    shText() {
      this.map.setDisplayOptions.poiText = !this.map.setDisplayOptions.poiText
      
    },
    shIcon() {
      this.map.setDisplayOptions({
        poiIcon: !poiIcon
      })
    },
  }
}
</script>

<style>

.video-play {
  width: 150px;
  height: 120px;
}
/* 弹出框的样式 */
.el-popover.popperbox {
  padding: 1px;
  background-color: rgba(0,0,0,.5);
}
/* 对弹出框中的小三角样式进行修改 */
.popperbox[x-placement^="bottom"] .popper__arrow::after {
  border-bottom-color: rgba(0, 0, 0, .5) !important;
}

.popperbox[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: rgba(0, 0, 0, .5);
}
/* 地图 */
#container {
  margin-right: 1px;
  width: 800px;
  height: 550px;
}
.btn-wrap {
  z-index: 999;
  width: 400px;
  position: fixed;
  bottom: 30px;
  left: 400px;
  padding: 5px;
  border-radius: 5px;
  background-color: rgba(265, 265, 265, 0.9);
  box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
}

.btn {
  width: 80px;
  height: 30px;
  float: left;
  background-color: #fff;
  color: rgba(27, 142, 236, 1);
  font-size: 8px;
  border: 1px solid rgba(27, 142, 236, 1);
  border-radius: 5px;
  margin: 0 5px 6px;
  text-align: center;
  line-height: 30px;
}

.btn:hover {
  background-color: rgba(27, 142, 236, 0.8);
  color: #fff;
  cursor: pointer;
}
</style>
</style>