<template>
    <div>
    <a href="https://aizoo.com"><img src="../assets/img/login.jpg" height="30px"></a>
    <h1 class='title'>借助TensorFlow.js</h1>
    <h1 class='title'>让人脸口罩识别跑在您本地浏览器里面</h1>
    <div id="container">
        <img src="../assets/img/login.jpg" ref="image" class="center">
        <div id="text-container">
        <p id="instruction" class="green-color">
        <button id="uploader-btn" onClick="clickUploader()">打开本地图片</button>
        <p class="green-color "> 或者将图片拖到这里 📂,  然后下方会显示结果</p>
        
        <p id="warning" class="result-color"></p>
        <p class="result-color">
            检测结果如下：
        </p>
        <input name="imgFile" type="file" id="fileUploader" accept="image/*" onChange="handleFiles()">
        </div>
        <canvas id="canvas" ref="canvas"></canvas>
    </div>
        
    </div>
</template>


<script>
import { ref } from 'vue'

  export default {
    data() {
      return {
        drawer: false,
        direction: 'rtl',
        model: false,
        image: ''
    }
    },
    mounted() {
        setup(this.$refs.image,this.$refs.canvas)
    },

    methods: {
      handleClose(done) {
            done();
      },
      setup(){
        setup()
      },
      clickLeft(){
        this.drawer = true
        this.direction = 'ltr'
      },
      handleFiles(){
        handleFiles();
      },
      clickUploader(){
        clickUploader()
      }
    }
  }
</script>

<style>
#left-button {
    position: absolute;
    margin-top: 50vh;
    width: 5px;
}
#right-button {
    position: absolute;
    float : right;
    margin-top: 20vh;
}

.title {
  text-align: center
}

#tips {
  color:rosybrown
}

#container {
  background: #c79ef812;
  /* height: 500px; */
  padding: 50px;
  font-size: 14px;
  border-radius: 33px;
  text-align: center;
}

#text-container {
  height: 100px;
  width: 100%;
  text-align: center;
}

#instruction {
  margin-top: 20px;
}

.result-color {
  color: #A159FC;
}

.green-color {
  color: #0bcf82;
}

.highlight {
  opacity: 0.4;
  background-color: rgba(161,89,252, 0.2) !important;
}

.center {
  display: block;
  margin-left: auto;
  margin-right: auto;
}

#fileUploader {
  opacity: 0;
}

#uploader-btn {
  background: #faebd700;
  margin-top: 1em;
  color: #0bcf81;
  border: solid 1px #0bcf81;
  padding: .6em 2em;
  line-height: 2;
  font-size: 12px;
  font-family: Apercu;
  cursor: pointer;
  border-radius: 10px;
}

#uploader-btn:hover {
  color: white;
  background-color: #0bcf82;
  border: solid 1px #0bcf82;
  -webkit-transition: all 0.3s ease;
  -moz-transition: all 0.3s ease;
  -o-transition: all 0.3s ease;
  transition: all 0.3s ease;
}

</style>