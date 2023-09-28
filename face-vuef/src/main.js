import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import axios from 'axios';
// 引入全局的样式文件
import './assets/css/global.less';
import './assets/css/base.css';
import './assets/fonts/icomoon.css';
//引入elementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//引入ecahrts
import * as echarts from 'echarts';

import SocketService from './utils/socket_service';
import "../public/js/video.js"; // 引入刚刚定义的video.js文件
import * as L from 'leaflet'
import icon from 'leaflet/dist/images/marker-icon.png'
import iconShadow from 'leaflet/dist/images/marker-shadow.png'

Vue.config.productionTip = false

// 将axios挂载到Vue的原型对象上,在别的组件中 this.$http
// Vue.prototype.$http = axios

Vue.use(ElementUI);

// 将全局的echarts对象挂载到Vue的原型对象上,别的组件中 this.$echarts
Vue.prototype.$echarts = echarts

//对服务器进行websocket的连接
SocketService.Instance.connect();
//其他组件    this.$socket
Vue.prototype.$socket = SocketService.Instance

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')