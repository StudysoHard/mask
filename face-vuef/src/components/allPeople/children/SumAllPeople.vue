<!-- 对未戴口罩的全部人数的统计 -->
<template>
  <div>
    <div class="overview panel">
      <div class="inner">
        <div class="item">
          <h4>{{nomaskNum}}</h4>
          <span>
            <i class="icon-dot" style="color: #eacf19"></i>
            未戴口罩总人数
          </span>
        </div>
        <div class="item">
          <h4>{{ maskNum }}</h4>
          <span>
            <i class="icon-dot" style="color: #6acca3"></i>
            已佩戴口罩人数
          </span>
        </div>
        <div class="item">
          <h4>{{ this.COVID }}</h4>
          <span>
            <i class="icon-dot" style="color: #ed3f35"></i>
            已识别人员
          </span>
        </div>
      </div>
      <div class="panel-footer"></div>
    </div>
  </div>
</template>

<script>
// import SocketService from '../../../utils/socket_service';

export default {
  props: {
    nomask: {
      type: Array,
      require: true,
    },
  },
  data() {
    return {
      timer: null,
      maskNum: 0,
      nomaskNum: 0
    }
  },
  mounted() {
    this.countCOVID()
  },
  destroyed() {
    clearInterval(this.timer)
  },
  methods: {
    countCOVID() {
        this.timer = setInterval(() => {
            let datas =  this.$socket.socketData.faceTimeScoller
            this.maskNum = 0
            this.nomaskNum = 0
            if(this.nomask.length != datas.length){
              for(let i=0; i < datas.length; i++) {
                this.maskNum += datas[i].maskNumber
                this.nomaskNum += datas[i].nomaskNumber
              }
              //使用vuex全局状态管理这两个变量的数据，使兄弟组件-大屏中间的组件也能获得此数据
              this.$store.commit('Mask', this.maskNum);
              this.$store.commit('noMask', this.nomaskNum);
              // this.$emit('noCOVID',this.noCOVID)
              // this.$emit('COVID',this.COVID)
            }
        }, 1000)

    }
  }
}
</script>

<style scoped>
/* 概览区域 */
.overview {
  height: 1rem;
}
.overview .inner {
  display: flex;
  justify-content: space-around;
}
.overview h4 {
  padding-left: 0.2rem;
  color: #fff;
}

.overview span {
  font-size: 0.1rem;
  color: #4c9bfd;
}
.panel {
  position: relative;
}
.panel-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
}

.panel-footer::before {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 20px;
  height: 10px;
  /* border-left: 2px solid #02a6b5; */
  border-bottom: 2px solid #02a6b5;
  content: "";
}
 .panel-footer::after {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 20px;
  height: 10px;
  /* border-right: 2px solid #02a6b5; */
  border-bottom: 2px solid #02a6b5;
  content: "";
}
</style>