import "video.js/dist/video-js.css"; // 引入video.js的css
import hls from "videojs-contrib-hls"; // 播放hls流需要的插件
import Vue from "vue";
Vue.use(hls);