<!-- Map-地图的真正显示 -->
<template>
  <div class="com-container">
    <div class="com-chart">
      <div class="no">
        <div class="no-hd">
          <ul>
            <li>{{ this.$store.state.nomask }}</li>
            <li>{{ this.$store.state.mask }}</li>
          </ul>
        </div>
        <div class="no-bd">
          <ul>
            <li>总未戴口罩人数</li>
            <li>总戴口罩人数</li>
          </ul>
        </div>
      </div>
      <div class="map">
        <div class="chart " ref="map_echart"></div>
        <div class="map1"></div>
        <div class="map2"></div>
        <div class="map3"></div>
      </div>
    </div>
  </div>
</template>

<script>
import Player from './children/Player';
import axios from 'axios'
// import bus from'../../utils/bus'

export default {
  data() {
    return {
      timer: null,
      seriesData: [
        { name: '天津市', value: 20057.34 },
        { name: '北京市', value: 15477.48 },
        { name: '上海市', value: 31686.1 },
        { name: '河北省', value: 6992.6 },
        { name: '山东省', value: 44045.49 },
        { name: '山西省', value: 4045.49 },
      ],
      map: null
    }
  },
  components: {
    Player
  },
  created() {
    // 通过bus  拿到兄弟发送过来的数据
    // bus.$on('COVID',(val)=>{
    //   console.log(val);
    // })
  },
  mounted() {
    // 1实例化对象
    this.initChart()
    // this.getData();
  },
  methods: {
    initChart() {
      // 获取地图数据
      // 将下载后的json文件放置/public目录下
      axios.get('/static/map/china.json').then(res => {
        // console.log(res);
        // 使用数据注册地图
        this.$echarts.registerMap('china', res.data)
        
          this.$nextTick(() => {
            // 初始化地图
            this.map = this.$echarts.init(this.$refs['map_echart'])
            // 设置基础配置项
            const option = {
              // 标题
              title: {
                text: "",
                left: 'center',
                // subtext: "下载链接",
                // sublink: "http://datav.aliyun.com/tools/atlas/#&lat=30.772340792178525&lng=103.94573258937584&zoom=9.5"
              },
              // 悬浮窗
              tooltip: {
                trigger: 'item',
                formatter: function (params) {
                  return `${params.name}: ${params.value || 0}`

                }
              },
              // 图例
              // visualMap: {
              //   min: 800,
              //   max: 50000,
              //   text: ['High', 'Low'],
              //   realtime: false,
              //   calculable: true,
              //   inRange: {
              //     color: ['lightskyblue', 'yellow', 'orangered']
              //   }
              // },
              geo: {
                map: 'china',
                zoom: 1,
                roam: 'false',
                // data: provinces,
                nameMap: {
                  '新疆维吾尔自治区': "新疆",
                  "西藏自治区": '西藏',
                  "甘肃省": "甘肃",
                  "宁夏回族自治区": "宁夏",
                  "广西壮族自治区": "广西",
                  "内蒙古自治区": "内蒙古",
                  "香港特别行政区": "香港",
                  "澳门特别行政区": "澳门"
                },
                label: {
                  emphasis: {
                    show: true,
                    color: "#fff"
                  }
                },
                // 所有地图的区域颜色
                itemStyle: {
                  normal: {
                    areaColor: "rgba(43, 196, 243, 0.42)",
                    borderColor: "rgba(43, 196, 243, 1)",
                    borderWidth: 1
                  },
                  emphasis: {
                    // areaColor: "#2B91B7",
                    show: true,
                    areaColor: '#3066ba', //鼠标滑过区域颜色
                    label: {
                      show: true,
                      textStyle: {
                        color: '#fff'//鼠标经过字体颜色
                      }
                    }
                  
                  }
                },
                emphasis: {
                  itemStyle: {
                    areaColor: 'rgba(0,60,153,0.5)',
                    shadowColor: 'rgba(0,0,0,0.8)',
                    shadowBlur: 5,
                    shadowOffsetY: 5
                  }
                },
                // 针对某些区域做一些特别的样式
                // regions: [{
                //   name: '广东省',
                //   itemStyle: {
                //     areaColor: 'yellow',
                //     color: 'black',
                //     borderColor: 'pink'
                //   }
                // }]
              },  
              // 要显示的散点数据
              series: [
                {
                  type: 'scatter',
                  coordinateSystem: 'geo',
                  data: [
                    { name: '江苏省', value: [120.15, 31.99, 9] }, // 值为，经纬度，数据
                    { name: '湖北省', value: [111, 31.89, 15477] },
                    { name: '四川省', value: [102.15, 31.89, 31686] },
                    { name: '浙江省', value: [120.15, 29.89, 6992] },
                    { name: '山东省', value: [118.15, 36.9, 44045] }
                  ],
                  SymbolSize: 4
                },
                // {
                //   type: 'lines',
                //   coordinateSystem: 'geo',
                //   data: [
                //     { coords: [[121.15, 38], [111, 31.89], [100.15, 31.89], [121.15, 29.89], [105.15, 30.89]] }
                //   ],
                //   polyline: true, // 是否是多段线
                //   lineStyle: {
                //     color: {
                //       type: 'radial',
                //       x: 0.5,
                //       y: 0.5,
                //       r: 0.5,
                //       colorStops: [{
                //         offset: 0, color: 'red' // 0% 处的颜色
                //       }, {
                //         offset: 1, color: 'blue' // 100% 处的颜色
                //       }],
                //       global: false, // 缺省为 false
                //       shadowColor: 'rgba(0, 0, 0, 0.5)',
                //       shadowBlur: 10,
                //       curveness: 1
                //     },
                //     opacity: 0.3,
                //     width: 2,
                //   },
                //   // 线特效的配置
                //   effect: {
                //     show: true,
                //     period: 5, // 特效动画的时间，单位为 s
                //     trailLength: 1, //特效尾迹长度[0,1]值越大，尾迹越长重
                //     // symbol: 'image://' + require('@/echartsMap/money.png'), // 自定义动画图标
                //     symbolSize: 15, //图标大小
                //     color: "red"
                //   }
                // }

              ]
            }
            // 将配置应用到地图上
            this.map.setOption(option);
            //  重点：当窗口或者大小发生改变时执行resize，重新绘制图表 
            window.onresize = this.map.resize;
          })
        
      })
    },
  },
}
</script>

<style scoped>

.no {
  background: rgba(101, 132, 226, 0.1);
  padding: 0.1875rem;
}

.no .no-hd {
  position: relative;
  border: 1px solid rgba(25, 186, 139, 0.17);
}

.no .no-hd::before {
  position: absolute;
  top: 0;
  left: 0;
  content: "";
  width: 30px;
  height: 10px;
  border-top: 2px solid #02a6b5;
  border-left: 2px solid #02a6b5;
}

.no .no-hd::after {
  position: absolute;
  bottom: 0;
  right: 0;
  content: "";
  width: 30px;
  height: 10px;
  border-right: 2px solid #02a6b5;
  border-bottom: 2px solid #02a6b5;
}

.no .no-hd ul {
  display: flex;
}

.no .no-hd ul li {
  position: relative;
  flex: 1;
  line-height: 1rem;
  font-size: 0.875rem;
  color: #ffeb7b;
  text-align: center;
  font-family: "electronicFont";
}

.no .no-hd ul li::after {
  content: "";
  position: absolute;
  top: 25%;
  right: 0;
  height: 50%;
  width: 1px;
  background: rgba(255, 255, 255, 0.2);
}

.no .no-bd ul {
  display: flex;
}

.no .no-bd ul li {
  flex: 1;
  text-align: center;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.225rem;
  height: 0.5rem;
  line-height: 0.5rem;
  padding-top: 0.125rem;
}

.map {
  position: relative;
  height: 10.125rem;
}

.map .map1 {
  width: 6.475rem;
  height: 6.475rem;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: url(../../assets/img/map.png);
  background-size: 100% 100%;
  opacity: 0.3;
}

.map .map2 {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 8.0375rem;
  height: 8.0375rem;
  background: url(../../assets/img/lbx.png);
  animation: rotate1 15s linear infinite;
  opacity: 0.6;
  background-size: 100% 100%;
}

.map .map3 {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 7.075rem;
  height: 7.075rem;
  background: url(../../assets/img/jt.png);
  animation: rotate2 10s linear infinite;
  opacity: 0.6;
  background-size: 100% 100%;
}
.map .chart {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 10.125rem;
}
@keyframes rotate1 {
  form {
    transform: translate(-50%, -50%) rotate(0deg);
  }

  to {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

@keyframes rotate2 {
  form {
    transform: translate(-50%, -50%) rotate(0deg);
  }

  to {
    transform: translate(-50%, -50%) rotate(-360deg);
  }
}
</style>