<template>
  <div class="screen-container">
    <!-- 头部的盒子 -->
    <header>
      <h1>基于人工智能的口罩佩戴轨迹分析系统</h1>
      <div class="showTime"></div>
    </header>
    <!-- 页面主体部分 -->
    <div class="screen-body">
      <section class="screen-left">
        <div id="left-top" class="panel">
          <!-- 没戴口罩的全部人数显示 -->
          <AllPeople></AllPeople>
          <div class="panel-footer"></div>
        </div>
        <div id="left-bottom" class="panel">
          <!-- 具体某个摄像头抓拍到未戴口罩次数的排行榜 -->
          <CameraRank></CameraRank>
          <div class=" panel-footer"></div>
        </div>
      </section>
      <section class="screen-middle">
        <div id="middle">
          <Map></Map>
        </div>
      </section>
      <section class="screen-right">
        <div id="right-top" class="panel">
          <!-- 今日/本周/本月有多少人未戴口罩的折线图 -->
          <DetailPeople></DetailPeople>
          <div class=" panel-footer"></div>
        </div>
        <div id="right-bottom" class="panel">
          <!-- 具体某人未戴口罩被拍次数的排行榜 -->
          <PeopleRank ref="prank_ref"></PeopleRank>
          <div class=" panel-footer"></div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import AllPeople from '../components/allPeople/AllPeople';
import CameraRank from '../components/CameraRank';
import Map from '../components/map/Map';
import DetailPeople from '../components/detailPeople/DetailPeople';
import PeopleRank from '../components/PeopleRank';

export default {
  components: {
    AllPeople,
    CameraRank,
    Map: Map,
    DetailPeople,
    PeopleRank,
    
  },
  data() {
    return {
      timer: null,
    }
  },
  mounted() {
    this.time();
  },
  destroyed() {
    clearInterval(this.timer);
  },
  methods: {
    time() {
      if(this.timer) {
        clearInterval(this.timer)
      }
      this.timer = setInterval(() => {
        const dt = new Date();
        let y = dt.getFullYear();
        let mt = dt.getMonth() + 1;
        let day = dt.getDate();
        let h = dt.getHours(); //获取时
        let m = dt.getMinutes(); //获取分
        let s = dt.getSeconds(); //获取秒
        document.querySelector(".showTime").innerHTML =
          "当前时间：" +
          y +
          "年" +
          mt +
          "月" +
          day +
          "-" +
          h +
          "时" +
          m +
          "分" +
          s +
          "秒";
      }, 1000)
    },
    mapClick() {
      this.$router.push("/map3dpage");
    },
  }
}
</script>

<style lang="less" scoped>

/* 声明字体*/
@font-face {
  font-family: electronicFont;
  src: url(../assets/fonts/DS-DIGIT.TTF);
}
.screen-container {
  width: 100%;
  height: 100%;
  /* padding: 0 20px; */
  /* color: #fff; */
  box-sizing: border-box;
}
header {
  position: relative;
  height: 1rem;
  width: 100%;
  // background: url(../assets/img/head_bg.png) no-repeat;
  background-size: 100% 100%;
}

header h1 {
  font-size: 0.475rem;
  color: #fff;
  text-align: center;
  line-height: 0.5rem;
}

header .showTime {
  position: absolute;
  right: 0.375rem;
  top: 0;
  line-height: 0.5rem;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.25rem;
}
.screen-body {
  width: 100%;
  height: 100%;
  display: flex;
  margin-top: 10px;
  .screen-left {
    height: 100%;
    width: 27.6%;

    #left-top {
      height: 63%;
      position: relative;
    }

    #left-bottom {
      height: 37%;
      margin-top: 25px;
      margin-bottom: 25px;
      position: relative;
    }
  }

  .screen-middle {
    position: relative;
    height: 100%;
    width: 45%;
    margin: 0 0.125rem 0.1875rem;

    #middle {
      width: 100%;
      position: relative;
      cursor: pointer;
    }
  }

  .screen-right {
    height: 100%;
    width: 27.6%;

    #right-top {
      height: 46%;
      position: relative;
    }

    #right-bottom {
      height: 54%;
      margin-top: 25px;
      position: relative;
    }
  }
}
/* 面板样式 */
.panel {
  position: relative;
  /* height: 3.875rem; */
  /* padding: 0 0.1875rem 0.5rem; */
  border: 1px solid rgba(25, 186, 139, 0.17);
  margin-bottom: 0.1875rem;
  background: url(../assets/img/line\(1\).png) rgba(255, 255, 255, 0.03);
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

.panel .panel-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
}

.panel .panel-footer::before {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 10px;
  height: 10px;
  border-left: 2px solid #02a6b5;
  border-bottom: 2px solid #02a6b5;
  content: "";
}

.panel .panel-footer::after {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  border-right: 2px solid #02a6b5;
  border-bottom: 2px solid #02a6b5;
  content: "";
}
.el-button {
  position:absolute;
  bottom: 8%;
  left:5%
}
</style>