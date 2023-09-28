<template>
    <el-container style="height: 100%; background-color: #f5f6f7;">
  <el-container>
    <el-header style="text-align: right; font-size: 12px">
        <span class="title">用户信息界面</span>
        <el-button @click="createUser()">生成用户</el-button>
    </el-header>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="index" label="序号" width="80">
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="200">
        </el-table-column>
        <el-table-column prop="loginTime" label="日期" width="300">
        </el-table-column>
        <el-table-column prop="roleName" label="用户角色" width="150">
        </el-table-column>
        <el-table-column prop="xunJianCircle" label="巡检半径" width="100">
        </el-table-column>
        <el-table-column prop="haveCamera" label="可访问数量" width="100">
        </el-table-column>
        <el-table-column prop="cameraCounts" label="接入摄像头" width="140">
        </el-table-column>
        <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">重置密码</el-button>
        </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
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
</style>

<script>
import { request } from '../network/request';

  export default {
    data() {
      return {
        reload:this.reload,
        tableData: []
      }
    },
    mounted() {
      this.getDate()
    } ,
    methods:{
      createUser(){
          request({
              url: 'http://www.heyongqiang.work:8979/logout',
              method: 'POST'
          }).then((res) => {
            console.log(res)
            this.getDate()
          })
      },
        handleClick(row) {
          request({
              url: 'http://www.heyongqiang.work:8979/admin/resetPassord?id=' + row.id,
              method: 'GET'
          }).then((res) => {
            tis.getDate()
          })
        },
        getDate(){
          request({
              url: 'http://www.heyongqiang.work:8979/admin/userInfo',
              method: 'POST'
          }).then((res) => {
            console.log(res)
            this.tableData = []
            this.tableData = res.result
          })
        }

    }
  };
</script>