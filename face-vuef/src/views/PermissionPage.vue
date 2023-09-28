<template>
    <el-container style="height: 100%; background-color: #f5f6f7;">
  <el-container>
    <el-header style="text-align: right; font-size: 12px">
        <span class="title">审批界面</span>
    <el-select v-model="values" @change="selectMethod" placeholder="请选择">
        <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
        </el-option>
    </el-select>
    </el-header>
    
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="index" label="序号" width="150">
        </el-table-column>
        <el-table-column prop="name" label="用户名" width="200">
        </el-table-column>
        <el-table-column prop="requestTime" label="日期" width="300">
        </el-table-column>
        <el-table-column prop="content" label="申请内容" width="200">
        </el-table-column>
        <el-table-column prop="status" label="申请状态" width="100">
        </el-table-column>
        <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
            <el-button @click="handleClick(scope.row,true)" type="text" size="small">同意</el-button>
            <el-button @click="handleClick(scope.row,false)" type="text" size="small">不同意</el-button>
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
        tableData: [],
        options: [{
          value: 0,
          label: '摄像头申请'
        }, {
          value: 1,
          label: '身份申请'
        }, {
          value: 2,
          label: '流接入申请'
        }, ],
        values: null,
        index: "1", // 1表示查询所有  0表示只查询未审批记录
      }
    },
    mounted() {
      this.getDate()
    } ,
    methods:{
        selectMethod(){
            // 对应的选项
            if(this.values != "请选择"){
              request({
                  url: 'http://www.heyongqiang.work:8979/request/list?index=' + this.index + "&status=" + this.values,
                  method: 'GET'
              }).then((res) => {
                console.log(res);
                  this.tableData = []
                  this.tableData = res.result
              })
            }

        },
        handleClick(row,bol) {
            request({
                url: 'http://www.heyongqiang.work:8979/admin/atitude',
                method: 'POST',
                data: {
                  "atitude" : bol,
                  "requestId" : row.id,
                  "requestContent" : row.content
                }
            }).then((res) => {
              this.selectMethod()
              console.log(res);
            })
        },
        getDate(){
          request({
              url: 'http://www.heyongqiang.work:8979/request/list?index=' + this.index + "&status=" + "0",
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