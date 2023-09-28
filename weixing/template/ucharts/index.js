// {{page}}.js

export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  data: {
    scrollindex:0,  //当前页面的索引值
    totalnum:4,  //总共页面数
    starty:0,  //开始的位置x
    endy:0, //结束的位置y
    critical: 100, //触发翻页的临界值
    margintop:0,  //滑动下拉距离,
    chartData_column: {},
    chartData_mount: {},
    chartData_rose: {},
    chartData_word: {},
    opts_column: {
      color: ["#e2c08d","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
      padding: [15,15,0,5],
      enableScroll: false,
      legend: {},
      xAxis: {
        disableGrid: true
      },
      yAxis: {
        data: [
          {
            min: 0
          }
        ]
      },
      extra: {
        column: {
          type: "group",
          width: 30,
          activeBgColor: "#000000",
          activeBgOpacity: 0.08
        }
      }
    },
    opts_mount: {
      color: ["#1890FF","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
        padding: [15,15,0,5],
        enableScroll: false,
        legend: {},
        xAxis: {
          disableGrid: true
        },
        yAxis: {
          data: [
            {
              min: 0
            }
          ]
        },
        extra: {
          mount: {
            type: "mount",
            widthRatio: 1.5
          }
        }
    },
    opts_rose : {
      color: ["#e2c08d","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
      padding: [5,5,5,5],
      enableScroll: false,
      legend: {
        show: true,
        position: "left",
        lineHeight: 25
      },
      extra: {
        rose: {
          type: "radius",
          minRadius: 50,
          activeOpacity: 0.5,
          activeRadius: 10,
          offsetAngle: 0,
          labelWidth: 15,
          border: true,
          borderWidth: 2,
          borderColor: "#FFFFFF",
          linearType: "custom"
        }
      }
    },
    opts_word: {
      color: ["#1890FF","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
      padding: undefined,
      enableScroll: false,
      extra: {
        word: {
          type: "normal",
          autoColors: false
        }
      }
    }

  },
  scrollTouchstart:function(e){
    let py = e.touches[0].pageY;
    // this.getServerData_1();
    // this.getServerData_2();
    // this.getServerData_3();
    // this.getServerData_4();
    // this.getServerData_5();
    this.setData({
      starty: py
    })
  },
  scrollTouchmove:function(e){
    let py = e.touches[0].pageY;
    let d = this.data;
    this.setData({
      endy: py,
    })
    console.log("py: ;; " + this.data.scrollindex);
    if(py-d.starty<100 && py-d.starty>-100){    
      this.setData({
        margintop: py - d.starty
      })
    }
  },
  scrollTouchend:function(e){
    let d = this.data;
    if(d.endy-d.starty >100 && d.scrollindex>0){
      this.setData({
        scrollindex: d.scrollindex-1
      })
    }else if(d.endy-d.starty <-100 && d.scrollindex<this.data.totalnum-1){
      this.setData({
        scrollindex: d.scrollindex+1
      })
    }
    this.setData({
        starty:0,
        endy:0,
        margintop:0
    })
  },
  onReady() {
    wx.request({
      url: `${devConfig.url}/charts/all?userName=`+wx.getStorageSync('userName'),//MaterialEntryAdd 后端接口名称
      method: "GET",
      success:(res)=>{
        console.log(res);
        if(res.data.code != 200){
          wx.showToast({  //显示发送图片失败的信息
            title: res.data.message,
            icon: 'error',
            duration: 2500
          })
        } else {
          this.getServerData_5()
          this.setData({
            chartData_column: res.data.result.cameraCatch,
            chartData_mount: res.data.result.xunJianCatch,
            chartData_rose: res.data.result.userTop
          })
        }


      },
      fail: (err) => {
          wx.showToast({  //显示发送图片失败的信息
          title: '发送请求失败！！',
          icon: 'error',
          duration: 2500
        })
        console.log(err);
      }
    });
  },
  getServerData_5() {
    //模拟从服务器获取数据时的延时
    setTimeout(() => {
      //模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
      let res = {
          series: [
            {
              name: "hls流",
              textSize: 25,
              data: undefined
            },
            {
              name: "rtmp流",
              textSize: 20,
              data: undefined
            },
            {
              name: "微信小程序",
              textSize: 20,
              data: undefined
            },
            {
              name: "管理员端",
              textSize: 20,
              data: undefined
            },
            {
              name: "巡检客户端",
              textSize: 20,
              data: undefined
            },
            {
              name: "人工智能",
              textSize: 20,
              data: undefined
            },
            {
              name: "深度学习",
              textSize: 20,
              data: undefined
            },
            {
              name: "持续集成",
              textSize: 20,
              data: undefined
            },
            {
              name: "深度学习",
              textSize: 10,
              data: undefined
            },
            {
              name: "微信小程序",
              textSize: 12,
              data: undefined
            },
            {
              name: "目标检测",
              textSize: 10,
              data: undefined
            },
            {
              name: "持续集成",
              textSize: 12,
              data: undefined
            },
            {
              name: "持续集成",
              textSize: 10,
              data: undefined
            },
            {
              name: "巡检客户端",
              textSize: 12,
              data: undefined
            },
            {
              name: "hlv流",
              textSize: 10,
              data: undefined
            },
            {
              name: "rtmp流",
              textSize: 12,
              data: undefined
            }
          ]
        };
      this.setData({ chartData_word : JSON.parse(JSON.stringify(res)) });
    }, 500);
  },
  returnMap(){
    wx.switchTab({
      url: '../../pages/camera/index',
    })
  }
})