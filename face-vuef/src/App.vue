<template>
  <div id="app">
        <el-button plain class="el-icon-d-arrow-right" id="left-button" size="mini" @click="clickLeft" type="primary"></el-button>
        <el-drawer
        title="导航"
        :visible.sync="drawer"
        :direction="direction"
        :modal=false
        :size="202"
        :before-close="handleClose">
          
          <el-container style="height: 500px;weight: 200px; border: 1px solid #eee">
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
              <el-menu :default-openeds="['1', '3']">
                
                <el-menu-item index="1" @click="redirectMap()">
                  <i class="el-icon-circle-plus-outline"></i>
                  <span slot="title">地图展示</span>
                </el-menu-item>

                <el-menu-item index="2" @click="redirectPrimission()">
                  <i class="el-icon-search"></i>
                  <span slot="title">
                    审批查看
                  </span>
                </el-menu-item>

                <el-menu-item index="3" @click="redirectXunJian()">
                  <i class="el-icon-s-order"></i>
                  <span slot="title">巡检模块</span>
                </el-menu-item>

                <el-menu-item index="5" @click="redirectUserModel()">
                  <i class="el-icon-s-order"></i>
                  <span slot="title">用户模块</span>
                </el-menu-item>

                <el-menu-item index="6" @click="redirectStream()">
                  <i class="el-icon-s-order"></i>
                  <span slot="title">流播放模块</span>
                </el-menu-item>
              </el-menu>
            </el-aside>
          </el-container>

        </el-drawer>
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        drawer: false,
        direction: 'rtl',
        model: false
      };
    },
    methods: {
      handleClose(done) {
            done();
      },
      clickLeft(){
        this.drawer = true
        this.direction = 'ltr'
      },
      redirectMap(){
        this.$router.replace("/screenpage")
      },
      redirectPrimission(){
        this.$router.replace("/permission")
      },
      redirectXunJian(){
        this.$router.replace("/XunJianPage")
      },
      redirectUserModel(){
        this.$router.replace("/userInfo")
      },
      redirectStream(){
        this.$router.replace("/testT")
      },
    }
  }
</script>

<style>
#left-button {
    position: absolute;
    z-index: 2;
    margin-top: 50vh;
    width: 5px;
}
#right-button {
    position: absolute;
    float : right;
    margin-top: 20vh;
}
</style>
