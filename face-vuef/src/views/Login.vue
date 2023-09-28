<template>
  <div class="loginbody">
    <div class="logindata">
      <div class="logintext">
        <h2>基于人工智能的口罩佩戴轨迹分析系统</h2>
      </div>
      <div class="formdata">
        <el-form ref="form" :model="form" :rules="rules">
          <el-form-item prop="username">
            <el-input v-model="form.username" clearable placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" clearable placeholder="请输入密码" show-password></el-input>
          </el-form-item>
        </el-form>
      </div>
      <!-- <div class="tool">
        <div>
          <el-checkbox v-model="checked" @change="remenber">记住密码</el-checkbox>
        </div>
        <div>
          <span class="shou" @click="forgetpas">忘记密码？</span>
        </div>
      </div> -->
      <div class="butt">
        <el-button type="primary" @click.native.prevent="login('form')">登录</el-button>
        <el-button class="shou" @click="resetLoginForm">重置</el-button>
        <!-- <el-button class="shou" @click="register">注册</el-button> -->
      </div>
    </div>
  </div>
</template>

<script>
import { request } from '../network/request';

export default {
  data() {
    return {
      form: {
        password: "",
        username: "",
      },
      checked: false,
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, message: "不能小于3个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 1, message: "不能小于3个字符", trigger: "blur" },
        ],
      },
    };
  },
  mounted() {
    // if (localStorage.getItem("news")) {
    //   this.form = JSON.parse(localStorage.getItem("news"))
    //   this.checked = true
    // }
  },
  methods: {
    //重置表单内容
    resetLoginForm(){
     this.$refs.loginFormRef.resetFields();
    },
    //登录方法
    login(form) {
      /*把当前vue对象赋给vm，this表示当前vue对象，直接写this.form.username是拿不到值的，
      因为在axois中写this表示的是axios对象*/
      var vm = this;
      // 为表单绑定验证功能
      this.$refs[form].validate((valid) => {
        //验证成功，发送请求
        if (valid) {
          request({
            url: 'http://www.heyongqiang.work:8889/login',
            method: 'post',
            data: {
              userName: vm.form.username,
              userPwd: vm.form.password
            }
          }).then((res) => {
            console.log(res);
            if (res.code === 200) { //res.data表示返回的数据
              vm.$message({
                message: '登录成功',
                type: 'success',
              });
              vm.$router.replace("/screenpage")
            }else {
              vm.$message.error('用户名或密码错误');
            }
          })
        } 
        //如果验证失败，返回
        else { 
          return false;
        }
      });
    },
    // remenber(data) {
    //   this.checked = data
    //   if (this.checked) {
    //     localStorage.setItem("news", JSON.stringify(this.form))
    //   } else {
    //     localStorage.removeItem("news")
    //   }
    // },
    // forgetpas() {
    //   this.$message({
    //     type: "info",
    //     message: "功能尚未开发额😥",
    //     showClose: true
    //   })
    // },
    // register() { },
  }
}
</script>

<style scoped>

.loginbody {
  z-index: 2;
  width: 100%;
  height: 100%;
  min-width: 1000px;
  background-image: url("../assets/img/login.jpg");
  background-size: 100% 100%;
  background-position: center center;
  overflow: auto;
  background-repeat: no-repeat;
  position: fixed;
  line-height: 100%;
  padding-top: 150px;
}

.logintext {
  margin-bottom: 20px;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
  color: white;
  text-shadow: 2px 2px 4px #000000;
}

.logindata {
  width: 400px;
  height: 300px;
  transform: translate(-50%);
  margin-left: 50%;
}

.tool {
  display: flex;
  justify-content: space-between;
  color: #606266;
}

.butt {
  margin-top: 10px;
  text-align: center;
}

.shou {
  cursor: pointer;
  color: #606266;
}
/* .el-button {
  width: 100px;
} */
</style>