<!-- 今日/本周/本月有多少人未戴口罩的折线图 -->
<template>
  <div class="com-container">
    <div class="caption">
      <h6>识别口罩者统计</h6>
      <!-- html5新特性data-属性 -->
      <!-- 只有满足条件时才会获得active样式 -->
      <a href="javascript:;" :class="{active: flag=='day'}" data-type="day" @click="clicka('day', $event)">天</a>
      <a href="javascript:;" :class="{active: flag=='week'}" data-type="week" @click="clicka('week', $event)">周</a>
      <a href="javascript:;" :class="{active: flag=='month'}" data-type="month" @click="clicka('month', $event)">月</a>
    </div>
    <div class="com-chart" ref="stock_ref"></div>
  </div>
</template>

<script>
import SocketService from '../../utils/socket_service';

export default {
  data() {
    return {
      chartInstance: null,
      flag: 'day',
      timerId: null,    //定时器的标识
      allData: {
        day: [
          // [43, 73, 62, 54, 91, 54, 84, 43, 86, 43, 54, 53],  // 非患者人数
          // [32, 54, 34, 87, 32, 45, 62, 68, 93, 54, 54, 24],  //新冠患者人数
          [],
          [],
          ['0时', '1时', '2时', '3时', '4时', '5时', '6时', '7时', '8时', '9时', '10时', '11时', '12时', '13时', '14时', '15时', '16时', '17时', '18时', '19时', '20时', '21时', '22时', '23时']
        ],
        week: [
          // [34, 87, 32, 76, 98, 12, 32, 87, 39, 36, 29, 36],
          // [56, 43, 98, 21, 56, 87, 43, 12, 43, 54, 12, 98],
          [],
          [],
          ['周一','周二','周三','周四','周五','周六','周日']
        ],
        month: [
          // [23, 75, 12, 97, 21, 67, 98, 21, 43, 64, 76, 38],
          // [43, 31, 65, 23, 78, 21, 82, 64, 43, 60, 19, 34],
          [],
          [],
          ['1号', '2号', '3号', '4号', '5号', '6号', '7号', '8号', '9号', '10号', '11号', '12号', '13号', '14号', '15号', '16号', '17号', '18号', '19号', '20号', '21号', '22号', '23号','24号','25号','26号','27号','28号']
        ],
      },
    }
  },
  components: {
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
    // 在组件销毁的时候, 需要将监听器取消掉
    window.removeEventListener('resize', this.screenAdapter)
  },
  methods: {
    initChart() {
      this.chartInstance = this.$echarts.init(this.$refs.stock_ref);
      // 初始化配置项和数据
      const initOption = {
        //标题
        title: {
          text: '单位：人',
          left: 30,
          top: 30,
          textStyle: {
            color: '#4996f5',
          }
        },
        // 工具提示
        tooltip: {
          trigger: 'axis'
        },
        // 右上角图例组件
        legend: {
          textStyle: {
            color: '#4c9bfd',
          },
          right: '22%',
          bottom: '80%',
          // top: '2'
          // letf: '10%'
        },
        // 设置网格
        grid: {
          show: true,
          top: '20%',
          left: '3%',
          right: '4%',
          bottom: '20%',
          borderColor: '#012f4a',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#4c9bfd'
          },
          axisLine: {
            show: false
          },
          boundaryGap: false
        },
        yAxis: {
          type: 'value',
          axisTick: {
            show: false
          },
          axisLabel: {
            color: '#4c9bfd'
          },
          splitLine: {
            lineStyle: {
              color: '#012f4a'
            }
          }
        },
        series: [{
          // 去掉折线上面的小圆点
          symbol: "none",
          name: '已佩戴',
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#00f2f1'
          }
        }, {
          symbol: "none",
          name: '未佩戴',
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#ed3f35'
          }
        }]
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
      this.updateChart();
      this.startInterval()
    },
    updateChart() {
        let flag = false
        this.timer = setInterval(() => {
          // console.log(datas.noHourList);
          let datas =  this.$socket.socketData;
          this.allData.day[0] = datas.noHourList
          this.allData.day[1] = datas.hasHourList
          this.allData.week[0] = datas.noWeekList
          this.allData.week[1] = datas.hasWeekList
          this.allData.month[0] = datas.noMonthList
          this.allData.month[1] = datas.hasMonthList
          if(!flag){
            //month[0]-该月的每天有个数据，如果有数据， 现在是七月，len=31
            if(this.allData.month[0].length != 0){
              flag = true
              let len = this.allData.month[0].length
              while(len - 28 > 0){
                //在month[2]数组的第31个位置开始插入值，该数组长度就会为31
                this.allData.month[2][len-1] = len-- + '号'
              }
            }
          }
          
        }, 900)
        const dataOption = {
            xAxis: {
              data: this.allData.day[2],
            //   axisLabel: {
            //     show: true,
            //     interval: 7
            // }
            },
            series: [{
              data: this.allData.day[0],
            }, {
              data: this.allData.day[1],
            }]
          };

      this.chartInstance.setOption(dataOption);
    },
    //点击切换天/周/月数据
    clicka(val,event) {
      // flag控制是否是active
      if (val === 'day') { this.flag = 'day' }
      if (val === 'week') { this.flag = 'week' }
      if (val === 'month') { this.flag = 'month' }
      //获取点击天/周/月的数组
      let currData = this.allData[event.currentTarget.dataset.type];
      //option是一个整合的过程
      const changeDataOption = {
        xAxis: {
          data: this.allData.day[2],
        },
        series: [{
          data: this.allData.day[0],
        }, {
          data: this.allData.day[1],
        }],

      };
      // 将获取天 / 周 / 月的数组具体的数据给series
      changeDataOption.series[0].data = currData[0];
      changeDataOption.series[1].data = currData[1];
      changeDataOption.xAxis.data = currData[2];
      this.chartInstance.setOption(changeDataOption);
    },
    //自动循环天/周/月数据
    startInterval() {
      if (this.timerId) {
        clearInterval(this.timerId)
      }
      // tab索引
      let index = 1;
      //获取文档中所有的 a标签,重点！！！获取的dom中所有的a，所以跨组件了
      const allTab = document.querySelectorAll("a");
      this.timerId = setInterval(() => {
        index++;
        // 大于3索引切换到0索引
        if (index > 3) index = 1;
        // console.log(index);
        // 选中对应a触发点击
        allTab[index].click();
        // console.log(allTab[index])
      }, 3000)
    },
    // 当浏览器的大小发生变化的时候, 会调用的方法, 来完成屏幕的适配
    screenAdapter() {
      // console.log(this.$refs.seller_ref.offsetWidth)
      const titleFontSize = this.$refs.stock_ref.offsetWidth / 100 * 3.6
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

<style scoped>

.caption {
  display: flex;
  line-height: 1;
}

h6 {
  height: 0.3rem;
  margin-top: 0.4rem;
  margin-left: 0.5rem;
  padding-right: 0.3rem;
  border-right: 0.04rem solid #00f2f1;
  /* font-size: 0.833rem; */
  color: #fff;
}

a {
  padding: 0.167rem;
  font-size: 0.2rem;
  margin: 0.25rem 0 0 0.2rem;
  border-radius: 0.125rem;
  color: #0bace6;
}

a.active {
  background-color: #4c9bfd;
  color: #fff;
}
/* 约束屏幕尺寸 */
/* @media screen and (max-width: 1024px) {
  html {
    font-size: 42px !important;
  }
}

@media screen and (min-width: 1920px) {
  html {
    font-size: 80px !important;
  }
} */
</style>