<!-- 没戴口罩的全部人数显示 -->
<template>
  <div class="com-container">
    <!-- @COVID="getCOVID" @noCOVID="getnoCOVID" -->
    <SumAllPeople :nomask="nomask"></SumAllPeople>
    <div class="monitor panel">
      <div class="inner">
        <div class="tabs">
          <a href="javascript:;">未戴口罩</a>
        </div>
        <div class="content" style="display: block;">
          <div class="head">
            <span class="col">识别结果</span>
            <span class="col">拍摄时间</span>
            <span class="col">拍摄人员</span>
          </div>
          <div class="marquee-view">
            <div class="marquee" :class="{'animate-up': isAnimateUp}" @mouseenter="animateStop()"
              @mouseleave="animateUp()">
              <div class="row" v-for="item in nomask" :key="item.id">
                <span class="col">未佩戴：{{item.nomaskNumber}} 已佩戴：{{item.maskNumber}}</span>
                <span class="col">{{item.catchTime}}</span>
                <span class="col " id="bol">{{ item.userName}}</span>
                <!-- <span class="icon-dot"></span> -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SumAllPeople from './children/SumAllPeople'
// import SocketService from '../../utils/socket_service';
// import bus from'../../utils/bus'

export default {
  components: {
    SumAllPeople: SumAllPeople
  },
  data() {
    return {
      isAnimateUp: false,
      timer: null,
      nomask: [
        // {
        //   id: 0,
        //   time: "2022-01-01-12:30",
        //   isCOVID: false
        // },
        // {
        //   id: 1,
        //   time: "2022-02-01-12:30",
        //   isCOVID: false
        // },
        // {
        //   id: 2,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 3,
        //   time: "2022-01-01-12:30",
        //   isCOVID: false
        // },
        // {
        //   id: 4,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 5,
        //   time: "2022-04-01-12:40",
        //   isCOVID: true
        // },
        // {
        //   id: 6,
        //   time: "2022-03-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 7,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 8,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 9,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 10,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
        // {
        //   id: 11,
        //   time: "2022-01-01-12:30",
        //   isCOVID: true
        // },
      ],
      // COVID: 0,
      // noCOVID: 0
    }
  },
  mounted() {
    this.updateSocketData();
    // this.scrollAnimate();
  },
  destroyed() {
    clearInterval(this.timer)
  },
  methods: {
    scrollAnimate() {
      if(this.nomask.length > 7) {
        this.timer = setInterval(() => {
          this.isAnimateUp = true
          setTimeout(() => {
            this.nomask.push(this.nomask[0])
            this.nomask.shift()
            this.isAnimateUp = false
          }, 850)
        }, 900)
      }
    },
    updateSocketData() {
        //每隔1s检查数据是否更新
        this.timer = setInterval(() => {
          // console.log(this.COVID);
            // 获取全部人脸
            let faceTimeScoller =  this.$socket.socketData.faceTimeScoller
            console.log(faceTimeScoller);
            if(faceTimeScoller.length != this.nomask.length){
              // 有数据更新
              this.nomask = faceTimeScoller;
              this.scrollAnimate();
              // console.log(this.nomask)
            }
        }, 1000)
    },
    //鼠标经过停止
    animateStop() {
      clearInterval(this.timer);
    },
    //鼠标离开继续滚动
    animateUp() {
      this.scrollAnimate();
    },
    // getCOVID(val){
    // //   if(val != this.COVID){
    // //     // 发送给兄弟
    // //     send()
    // //   }
    //   this.COVID = val;
    // },
    // getnoCOVID(val){
    // //   if(val != this.noCOVID){
    // //     send()
    // //   }
    //   this.noCOVID = val;
    // },
    showPath(faceName, imgPath,time,isCOVID,people) {
      // this.$router.push({
      //   path: '/personlocuspage',
      //   query: {
      //     faceName: faceName,
      //     imgPath: imgPath,
      //     time: time,
      //     isCOVID: isCOVID,
      //     people : people
      //   }
      // });
    }
    //事件总线eventBus，兄弟组件中使用
    // send(){
    //   // 通过eventBus发送数据
    //   bus.$emit('COVID',this.COVID);
    //   bus.$emit('noCOVID',this.noCOVID);
    // }
  },
  
 
}
</script>

<style scoped>

/* 面板样式 */
.panel {
  position: relative;
  /* border: 1px solid rgba(25, 186, 139, 0.17); */
  border: 1px solid rgba(25, 186, 139, 0.17);
  margin-bottom: 0.1875rem;
  background: url(../../assets/img/line\(1\).png) rgba(255, 255, 255, 0.03);
}

.panel::before {
  position: absolute;
  top: 0;
  left: 0;
  width: 10px;
  height: 10px;
  border-left: 2px solid #02a6b5;
  border-top: 2px solid #02a6b5;
  content: "";
}

.panel::after {
  position: absolute;
  top: 0;
  right: 0;
  width: 10px;
  height: 10px;
  border-right: 2px solid #02a6b5;
  border-top: 2px solid #02a6b5;
  content: "";
}

.panel .inner {
  padding: 1rem 0rem;
  position: absolute;
  top: -0.8rem;
  right: -1rem;
  bottom: -0.833rem;
  left: 0rem;
}

.panel h3 {
  font-size: 0.833rem;
  color: #fff;
}

/* 监控区域 */
.monitor {
  margin-top: 0.2rem;
  height: 7rem;
}

.monitor .inner {
  /* padding: 1rem 0; */
  display: flex;
  flex-direction: column;
}

.monitor .tabs {
  /* padding: 0 1.5rem; */
  margin-bottom: 0.2rem;
  display: flex;
}

.monitor .tabs a {
  /* display: none; */
  margin-left: 0.5rem;
  color: #fff;
  font-size: 0.2rem;
  padding: 0 0.5rem;
  padding-left: 0;
  font-weight: 800;
}

.monitor .content {
  flex: 1;
  position: relative;
  display: none;
}

.monitor .head {
  display: flex;
  justify-content: space-between;
  line-height: 1;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 0.1rem 0.4rem;
  color: #68d8fe;
  font-size: 0.2rem;
}

.monitor .row {
  display: flex;
  justify-content: space-between;
  line-height: 1.2;
  font-size: 0.2rem;
  color: #61a8ff;
  padding: 0.1rem 0.5rem;
  cursor: pointer;
}

.monitor .row .icon-dot {
  position: absolute;
  left: 0.64rem;
  opacity: 0;
}

.monitor .row:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: #68d8fe;
}

.monitor .row:hover .icon-dot {
  opacity: 1;
}

.monitor .col:first-child {
  width: 2.5rem;
}

.monitor .col:nth-child(2) {
  width: 3rem;
  /* white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden; */
}

.monitor .col:nth-child(3) {
  width: 2rem;
}
.monitor .marquee-view .col #bol {
  padding-left: 2rem;
}
/* 动画 */
.monitor .marquee-view {
  position: absolute;
  width: 100%;
  top: 0.5rem;
  bottom: 0;
  overflow: hidden;
}

.animate-up {
  transition: all 5s linear;
  transform: translateY(-50%);
}
</style>