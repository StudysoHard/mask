<template>
     <div class="test-videojs">
     <video id="videoPlayer" class="video-js" muted></video>
     <div id="box">
 <div class="picker-main">
    <el-button style="top: 15px;position: relative" @click="showPicker()">切换流</el-button>
    <span id="stream_index">{{label}}</span>
    <div
      v-if="show"
      class="picker"
    >
      <section class="picker-main">
        <h3>
          <span @click="show = false">取消</span>
          <span>请选择</span>
          <span @click="sure()">确认</span>
        </h3>
        <ul ref="ul">
          <li
            v-for="(item, index) in list"
            :key="index"
            :class="active==item.id?'active':active==item.id-1||active==item.id+1?'active2':null"
            :ref="'li'+item.label"
          >{{item.val}}</li>
        </ul>
      </section>
    </div>
  </div>
     </div>
     </div>
</template>
<script>
import Videojs from "video.js"; // 引入Videojs
import { request } from '../network/request';

export default {
     data() {
     return {
     // https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8 是一段视频，可将cctv1 （是live现场直播，不能快退）的替换为它，看到快进快退的效果
     nowPlayVideoUrl: "http://47.115.203.85:8080/hls/vehicle_240p528kbs/index.m3u8",
      list: [
      ],
      options: {
        autoplay: true, // 设置自动播放
        muted: true, // 设置了它为true，才可实现自动播放,同时视频也被静音 （Chrome66及以上版本，禁止音视频的自动播放）
        preload: "auto", // 预加载
        controls: true // 显示播放的控件
     },

      show: false,
      active: 0,
      label: "",
      listOffsetTop: [],
      timer: null,
     }
     },
     mounted() {
      this.getDate();
      this.getVideo(this.nowPlayVideoUrl);
     },
    watch: {
    nowPlayVideoUrl(newVal, old) {
        this.getVideo(newVal);
        }
        },
        beforeDestroy() {
        if (this.player) {
        this.player.dispose(); // Removing Players,该方法会重置videojs的内部状态并移除dom
    }

    },
     methods: {
      getDate(){
          request({
            url: 'https://www.heyongqiang.work/camera/list',
            method: 'get',
          }).then((res) => {
            if (res.code === 200) { //res.data表示返回的数据
              this.list = res.result
              this.nowPlayVideoUrl = res.result[3].stream
              console.log("现在的List数据为");
              console.log(this.list);
            }else {
              vm.$message.error('获取信息失败');
            }
          })
      },
      showPicker() {
      this.show = true;
      this.active = 0;
      this.timer = setTimeout(() => {
        clearTimeout(this.timer);
        this.getOffsetTop();
        this.computeActive();
      }, 50);
      },
     beforeDestroy() {
     if (this.player) {
     this.player.dispose(); // Removing Players,该方法会重置videojs的内部状态并移除dom
     }
     }
    ,
    sure() {
      this.list.map((item, index) => {
        item.id == this.active ? (this.stream = item.val) : null;
      });
      this.nowPlayVideoUrl = this.stream
      this.show = false;
    },
    getOffsetTop() {
      this.listOffsetTop = [];
      this.list.map((item, index) => {
        let liTop = this.$refs["li" + item.label];
        this.listOffsetTop.push(liTop[0].offsetTop - 41);
      });
    },
    computeActive() {
      let scroll = this.$refs.ul;
      scroll.addEventListener("scroll", () => {
        this.listOffsetTop.map((item, index) => {
          item <= scroll.scrollTop + 100 ? (this.active = index - 2) : null;
        });
      });
    },
    getVideo(nowPlayVideoUrl) {
        this.player = Videojs("videoPlayer", this.options);
        //关键代码， 动态设置src，才可实现换台操作
        this.player.src([
        {
        src: nowPlayVideoUrl,
        type: "application/x-mpegURL" // 告诉videojs,这是一个hls流
        }
        ]);
    },
  }
};
</script>

<style lang="less" scoped>
#test-videojs {
  position: relative;
}
#videoPlayer {
    float: left;
     width: 70%;
     height: 1000px;
     margin: auto;
}
#box {
  right: 0;
  position: absolute;
  height: 1000px;
  width: 27%;
  margin-right: 20px;
  padding-left: 50px;
  background-color: #f7f7f7;
}
#stream_index{
  position: relative;
  right: 45px;
  top: 42px;
}
.picker {
  background-color: rgba(0, 0, 0, 0.2);
  max-height: 100vh;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  display: flex;
  .picker-main {
    width: 100%;
    position: absolute;
    top: 70px;
    background-color: #fff;
    h3 {
      padding: 0;
      margin: 0;
      display: flex;
      justify-content: space-around;
      border-bottom: solid 1px #ddd;
      font-size: 14px;
      line-height: 40px;
    }
    ul {
      max-height: 250px;
      padding: 0;
      margin: 0;
      overflow: auto;
      background-color: #fff;
      li {
        list-style: none;
        font-size: 14px;
        line-height: 50px;
        text-align: center;
        opacity: 0.3;
        height: 50px;
        background-color: #fff;
      }
    }
  }
}
.active {
  background-color: #ddd !important;
  color: #333;
  opacity: 1 !important;
}
.active2 {
  color: #333;
  opacity: 0.6 !important;
}
</style>