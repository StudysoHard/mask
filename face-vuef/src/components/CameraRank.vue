<!-- 具体某个摄像头抓拍到未戴口罩次数的排行榜 -->
<template>
  <div class="com-container">
    <div class="com-chart" ref="crank_ref">
    </div>
  </div>
</template>

<script>
import {request} from '../network/request';
// import SocketService from '../utils/socket_service';


export default {
  data() {
    return {
      chartInstance: null,
      //服务器返回的数据
      allData: [
        // {
        //   id: 1,
        //   name: '一号',
        //   values: 15
        // },
        // {
        //   id: 2,
        //   name: '二号',
        //   values: 50
        // },
        // {
        //   id: 3,
        //   name: '三号',
        //   values: 20
        // },
        // {
        //   id: 4,
        //   name: '四号',
        //   values: 2
        // },
        // {
        //   id: 5,
        //   name: '五号',
        //   values: 30
        // },
        // {
        //   id: 6,
        //   name: '六号',
        //   values: 200
        // }
      ], 
      currentPage: 1, //当前显示的页数
      totalPage: 0,   //一共有多少页
      timer: null, //每隔一段时间查询后端推送的数据是否更新的定时器
      timerId: null    //定时器的标识
    }
  },
  created() {
  },
  mounted() {
    // 实例化对象
    this.initChart();
    this.getData();
    window.addEventListener('resize', this.screenAdapter)
    // 在页面加载完成的时候, 主动进行屏幕的适配
    this.screenAdapter()
  },
  destroyed() {
    clearInterval(this.timerId)
    clearInterval(this.timer)
    // 在组件销毁的时候, 需要将监听器取消掉
    window.removeEventListener('resize', this.screenAdapter)
  },
  methods: {
    initChart() {
      this.chartInstance = this.$echarts.init(this.$refs.crank_ref);
      // 初始化配置项和数据
      const initOption = {
        //标题
        title: {
          text: '▎摄像头抓拍',
          left: 20,
          top: 15,
          textStyle: {
            color: '#fff',
          }
        },
        grid: {
          top: '25%',
          left: '3%',
          right: '6%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            // show: false,    //是否显示 x 轴
            axisTick: {
              alignWithLabel: true
            },
            //修改刻度标签 相关样式
            axisLabel: {
              textStyle: {
                color: "rgba(255,255,255,.6)",
                fontSize: "12"
              }
            },
            //不显示x坐标轴的样式
            axisLine: {
              show: false,
              //  如果想要设置单独的线条样式
            },
          }
        ],
        yAxis: [
          {
            type: 'value',
            // y 轴文字标签样式
            axisLabel: {
              textStyle: {
                color: "rgba(255,255,255,.6)",
                fontSize: "12"
              }
            },
            // y轴线条样式
            axisLine: {
              lineStyle: {
                color: "rgba(255,255,255,.1)"
                // width: 1,
                // type: "solid"
              }
            },
            // y 轴分隔线样式
            splitLine: {
              lineStyle: {
                color: "rgba(255,255,255,.1)"
              }
            }
          },
        ],
        series: [
          {
            name: "未戴口罩人数",
            type: 'bar',
            // 修改柱子宽度
            barWidth: "35%",
            itemStyle: {
              // 修改柱子圆角
              barBorderRadius: 5
            },
          }
        ],
        color: ["#2f89cf"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          }
        },
      };
      // 把初始化配置项给实例对象
      this.chartInstance.setOption(initOption);
      // 对图表对象进行鼠标事件的监听
      this.chartInstance.on('mouseover', () => {
        clearInterval(this.timerId)
      })
      this.chartInstance.on('mouseout', () => {
        this.startInterval()
      })
    },
    //获取服务器数据
    getData() {
        this.timer = setInterval(() => {
        let datas =  this.$socket.socketData.cameraTomList
        if(this.allData.length != datas.length){
          // 更新数据
          this.allData = datas
          this.updateChart();
        } else {
          let sumPre = 0
          for(let i = 0; i < this.allData.length ; i++){
            // 遍历每个元素
            if(datas[i].counts != this.allData[i].counts){
              // 更新数据
              this.allData = datas
              this.updateChart();
            }
          }
        }
        }, 1000)

      // const ret = await request('/camera/getAll')
      // console.log(ret);
      // 对数据排序
      // this.allData.sort((a, b) => {
      //   return a.values - b.values // 从小到大的排序
      // })
      // 每7个元素显示一页
      this.totalPage = this.allData.length % 7 === 0 ? this.allData.length / 7 : this.allData.length / 7 + 1;
      this.updateChart();
      // 启动定时器
      this.startInterval()
    },
    updateChart() {
      //展示的列数为7
      const start = (this.currentPage - 1) * 7;
      const end = this.currentPage * 7;
      const showData = this.allData.slice(start, end)
      const rankNames = showData.map((item) => {
        return item.cameraId;
      })
      const rankValues = showData.map((item) => {
        return item.counts;
      })
      const dataOption = {
        xAxis: [
          {
            data: rankNames,
          }
        ],
        series: [
          {
            data: rankValues,
            data: rankValues
          }
        ],
      };
      this.chartInstance.setOption(dataOption);
    },
    //设置定时器，每次展示七个数据，循环展示
    startInterval() {
      if (this.timerId) {
        clearInterval(this.timerId)
      }
      this.timerId = setInterval(() => {
        this.currentPage++
        if (this.currentPage > this.totalPage) {
          this.currentPage = 1
        }
        this.updateChart()
      }, 3000)
    },
    // 当浏览器的大小发生变化的时候, 会调用的方法, 来完成屏幕的适配
    screenAdapter() {
      // console.log(this.$refs.seller_ref.offsetWidth)
      const titleFontSize = this.$refs.crank_ref.offsetWidth / 100 * 3.6
      // 和分辨率大小相关的配置项
      const adapterOption = {
        title: {
          textStyle: {
            fontSize: titleFontSize
          }
        },
        tooltip: {
          axisPointer: {
            lineStyle: {
              width: titleFontSize
            }
          }
        },
        series: [
          {
            barWidth: titleFontSize,
            itemStyle: {
              barBorderRadius: [0, titleFontSize / 2, titleFontSize / 2, 0]
            }
          }
        ]
      }
      this.chartInstance.setOption(adapterOption)
      // 手动的调用图表对象的resize 才能产生效果
      this.chartInstance.resize()
    }
  }
}
</script>

<style lang="less" scoped>

/* .panel {
  position: relative;
  height: 3.875rem;
  border: 1px solid rgba(25, 186, 139, 0.17);
  background: rgba(255, 255, 255, 0.04) url(../assets/img/line\(1\).png);
  padding: 0 0.1875rem 0.5rem;
  margin-bottom: 0.1875rem;
}

.panel::before {
  position: absolute;
  top: 0;
  left: 0;
  content: "";
  width: 10px;
  height: 10px;
  border-top: 2px solid #02a6b5;
  border-left: 2px solid #02a6b5;
}

.panel::after {
  position: absolute;
  top: 0;
  right: 0;
  content: "";
  width: 10px;
  height: 10px;
  border-top: 2px solid #02a6b5;
  border-right: 2px solid #02a6b5;
}

.panel .panel-footer {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
}

.panel .panel-footer::before {
  position: absolute;
  bottom: 0;
  left: 0;
  content: "";
  width: 10px;
  height: 10px;
  border-bottom: 2px solid #02a6b5;
  border-left: 2px solid #02a6b5;
}

.panel .panel-footer::after {
  position: absolute;
  bottom: 0;
  right: 0;
  content: "";
  width: 10px;
  height: 10px;
  border-bottom: 2px solid #02a6b5;
  border-right: 2px solid #02a6b5;
}
.panel h2 {
  height: 0.6rem;
  line-height: 0.6rem;
  text-align: center;
  color: #fff;
  font-size: 0.25rem;
  font-weight: 400;
}

.panel h2 a {
  margin: 0 0.1875rem;
  color: #fff;
  text-decoration: underline;
}

.panel .chart {
  height: 3rem;
}*/
</style> 