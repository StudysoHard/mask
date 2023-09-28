<!-- 具体某人未戴口罩被拍次数的排行榜 -->
<template>
  <div class="com-container">
    <div class="com-chart" ref="prank_ref">
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
        // },
        // {
        //   id: 7,
        //   name: '七号',
        //   values: 80
        // },
        // {
        //   id: 8,
        //   name: '八号',
        //   values: 100
        // },
      ],
      currentPage: 1, //当前显示的页数
      totalPage: 0,   //一共有多少页
      timerId: null,    //定时器的标识
      timer: null
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
      this.chartInstance = this.$echarts.init(this.$refs.prank_ref);
      // 初始化配置项和数据
      const initOption = {
        //标题
        title: {
          text: '▎未戴口罩次数',
          left: 20,
          top: 20,
          textStyle: {
            color: '#fff',
          }
        },
        grid: {
          top: '17%',
          left: '3%',
          right: '6%',
          bottom: '3%',
          containLabel: true
        },
        yAxis: [
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
        xAxis: [
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
            name: "未戴口罩次数",
            type: 'bar',
            // 修改柱子宽度
            barWidth: "35%",
            //柱子间距
            // barGrap: "-40%",
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
      // const ret = await request('/camera/getAll')
      // console.log(ret);
      // 对数据排序
        this.timer = setInterval(() => {
            // 获取全部人脸
            let data =  this.$socket.socketData.allPeopleTops;
            let init = false;
            if(!init){
              if(data.length != 0){
                init = true
                this.allData = data;
                this.updateChart();
                this.startInterval()
                this.screenAdapter()
              }
            } else{
              for(let i = 0; i < data.length; i++){
                if(data[i].count != this.allData[i].count || data[i].faceName != this.allData[i].faceName){
                  // console.log("更新");
                  this.allData = data;
                  this.updateChart();
                  this.startInterval()
                  this.screenAdapter()
                }
              }
            }
        }, 1000)
      // 每5个元素显示一页
      this.currentPage = this.allData.length % 7 === 0 ? this.allData.length / 7 : this.allData.length / 7 + 1;
      this.updateChart();
      // 启动定时器
      this.startInterval()
    },
    updateChart() {
      //展示的列数为5
      const start = 0;
      const end = this.currentPage.length;
      const showData = this.allData.slice(start, end)
      const rankNames = showData.map((item) => {
        return item.faceName;
      })
      const rankValues = showData.map((item) => {
        return item.count;
      })
      const dataOption = {
        yAxis: [
          {
            data: rankNames,
          }
        ],
        series: [
          {
            data: rankValues,
          }
        ],
      };
      this.chartInstance.setOption(dataOption);
    },
    //设置定时器，每次展示5个数据，循环展示
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
      const titleFontSize = this.$refs.prank_ref.offsetWidth / 100 * 3.6
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

<style>

</style>