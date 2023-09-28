<template>
    <el-container style="height: 100%; background-color: #f5f6f7;">
  <el-container>
    <el-header style="text-align: right; font-size: 12px">
        <span class="title">巡检模块</span>
    </el-header>
    
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="index" label="序号" width="150">
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="400">
        </el-table-column>
        <el-table-column prop="date" label="日期" width="300">
        </el-table-column>
        <el-table-column prop="longitude" label="巡检开启经度" width="300">
        </el-table-column>
        <el-table-column prop="latitude" label="巡检开启纬度" width="300">
        </el-table-column>

        <el-table-column 
        type="text" 
        label="历史图片"
        width="150"
        @click="showImage(scope.row)">
        <template slot-scope="scope">
            <el-button @click="showImage(scope.row)" type="text" size="middle">查看</el-button>
        </template>
        </el-table-column>

        
        <el-table-column
        label="操作"
        width="300">
        <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="middle">查看轨迹分析</el-button>
        </template>
        
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>

<el-dialog
  title="巡检图片"
  :visible.sync="dialogVisible"
  width="30%"
  :before-close="handleClose">
<div v-for="it in xunJianinfo">
  <el-image 
    style="width: 100px; height: 100px;position: relative"
    :src="it.imgPath" 
    :preview-src-list="imgList">
  </el-image>
  <div class="right-box">
    <div class="lon">拍摄经度：{{it.longitude}}</div>
    <div class="lat">拍摄纬度：{{it.latitude}}</div>
    <div class="name">用户名：{{it.userName}}</div>
  </div>

</div>
</el-dialog>

    </el-container>
</template>

<style>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  
  .el-aside {
    color: #333;
  }
  .title{
    float: left;
    margin-left: 300px;
    font-size:xx-large
  }
  .right-box {
    margin-left: 20px;
    display: inline-block;
    width: 180px;
    height: 100px
  }
  .lon {
    line-height: 30px;
    height: 30px;
    width: 180px;
  }
  .lat {
    line-height: 30px;
    margin-top: 5px;
    height: 30px;
    width: 180px;
  }
  .name {
    line-height: 30px;
    margin-top: 5px;
    height: 30px;
    width: 180px;
  }
</style>

<script>
import { request } from '../network/request';

  export default {
    data() {
      return {
        reload:this.reload,
        tableData: [],
        dialogVisible: false,
        imgList: [],
        xunJianinfo: []
      }
    },
    mounted() {
      this.getDate()
    } ,
    methods:{
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
        handleClick(row) {
          // 如果巡检拍摄的图片过少则无法跳转
          request({
            url: 'http://www.heyongqiang.work:8979/xunjian/counts/'+row.id,
            method: 'GET'
          }).then((res) =>{
            console.log("打印巡检返回数量");
            console.log(res);
            if(res.result <= 4){
              this.$message('本次巡检拍摄图片不足五张无法分析');
            } else{
              this.$router.push({
                path: '/personlocuspage',
                query: {
                  id: row.id
                }
              });
            }
          })
        },
        showImage(row){
          // 请求巡检图片
          request({
            url: 'http://www.heyongqiang.work:8979/xunjian/getInfos/'+row.id,
            method: 'GET'
          }).then((res) =>{
            this.imgList = res.result.images
            this.xunJianinfo = res.result.infos
            console.log(res);
          })
          this.dialogVisible = true
        } ,
        getDate(){
          request({
              url: 'https://www.heyongqiang.work/admin/xunJian',
              method: 'GET'
          }).then((res) => {
            console.log(res)
            this.tableData = []
            this.tableData = res.result
          })
        }

    }
  };
</script>