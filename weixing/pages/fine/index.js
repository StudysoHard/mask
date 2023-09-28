// pages/ui/index.js
import * as blazeface from '../../model/index'
import * as tf from '@tensorflow/tfjs-core'
import {
  colorMap,
  statusColor,
  statusDisc
} from './constant'
import {
  modelUrl,
  faceServerHost,
  devicePosition,
  popupSetting,
  dimThreshold,
  brightThreshold,
  detectFacePerFrame
} from './config'
export const devConfig = {
  url: "https://www.heyongqiang.work",  //后端接口路径
  imgUrl: "http://localhost:.../", //文件存储目录清单（方便调用）
  imgUrlEx: "http://localhost:..." //系统网址
};
Page({
  data: {
    // 人脸监测结果，将会展示到界面中
    result: '模型载入中',
    // 状态，开始状态是导入模型
    status: 'LOADING_MODEL',
    // 配置
    popupSetting,
    devicePosition,
    modelUrl,
    faceServerHost,
    colorMap,
    statusColor,
    statusDisc,
    dimThreshold,
    brightThreshold
  },
  _model: null,
  _frameWidth: 0,
  _frameHeight: 0,
  _listener: null,
  _count: 0,
  _preStatus: 'LOADING_MODEL',

  //当设置blazeface url路径时触发
  changeModelUrl(event) {
    this.data.modelUrl = event.detail.value;
    this.loadmodel(this.data.modelUrl);
  },
  //当设置人脸识别后端接口地址时触发
  changeFaceServerHost(event) {
    this.data.faceServerHost = event.detail.value;
  },
  //当设置明亮长度最低阈值时触发
  sliderDimValueChange(event) {
    this.data.dimThreshold = event.detail.value;
  },
  //当设置明亮长度最高阈值时触发
  sliderBrightValueChange(event) {
    this.data.brightThreshold = event.detail.value;
  },
  // 点击‘设置面板’时触发，弹出设置框
  popupSetting() {
    this.setData({
      popupSetting: true
    })
    this._listener.stop();
  },
  // 关闭设置框时触发
  closePopup(){
    this._listener.start()
  },
  async onReady() {
    this.loadmodel(this.data.modelUrl)
    this._listener = this.addCameraLinstener()
    this._listener.start();
  },
  async loadmodel(modelUrl) {
    const model = await blazeface.load({
      maxFaces: 3,
      modelUrl: modelUrl
    });
    this._model = model;
  },
  addCameraLinstener() {
    const camera = wx.createCameraContext();
    const listener = camera.onCameraFrame(async frame => {
      this._count++;
      if (this._count === detectFacePerFrame) {
        const res = await this.detectFace(frame);
        
        // 记录图片帧的长与宽
        this._frameWidth = frame.width
        this._frameHeight = frame.height
        
        const status = this.checkFace(frame, res)
        // 若状态改变，改变界面背景和提示
        if (this._preStatus != status) {
          this._preStatus = status
          this.setData({
            status
          })
        }

        if (status === 'DETECT_SUCCESS') {
          this._listener.stop()
          this.lockFace()
          this.faceReconition()
        }

        // reset count
        this._count = 0;
      }
    });
    return listener
  },
  async detectFace(frame) {
    if (this._model) {
      const res = await this._model.estimateFaces(new Uint8Array(frame.data),
        frame.width, frame.height);
      return res
    }
  },
  checkFace(frame, res) {
    // 判断是否仅有一张人脸
    const len = res.length
    if (len === 0)
      return 'DETECT_NOT_FOUND'
    if (len > 1)
      return 'DETECT_MULTI_FACES'
    
    const faceInfo = res[0]
    // 取整，便于后面的计算
    let start = faceInfo.topLeft.map(v => Math.round(v))
    const end = faceInfo.bottomRight.map(v => Math.round(v))
    let size = [end[0] - start[0], end[1] - start[1]]
    // 经过可视化人脸预测框，发现预测框为正方形
    // 框不能框中额头部分，所以对框的起始高度
    // 做微调处理
    start[1] = Math.round(start[1] - size[1] * 0.2)
    size[1] = Math.round(size[1] * 1.2)

    if (size[0] < 0.5 * this._frameWidth)
      return 'TOO_FAR'
    if (size[0] > 0.8 * this._frameWidth)
      return 'TOO_CLOSE'
    if (start[0] < 0 || end[0] > this._frameWidth)
      return 'NOT_CENTER'
    // camera在frame的数据上做了缩放居中
    // 所以在检测高度上的居中需要这样算
    const temp = (this._frameHeight - this._frameWidth) / 2
    if (start[1] - temp < 0 || end[1] - temp > this._frameWidth)
      return 'NOT_CENTER'
    // 计算光照强度
    const brightMean = tf.tidy(() => {
      const imageTensor = tf.browser.fromPixels({
        data: new Uint8Array(frame.data),
        width: frame.width,
        height: frame.height
      })
      const croppedImage = tf.slice(imageTensor,
          [start[1], start[0], 0],
          [size[1], size[0], 3]).expandDims(0)
      const flattedImage = croppedImage.reshape([-1])
      const brightMean = tf.mean(flattedImage)
      return brightMean.arraySync()
    })
    // 这里可以检验tf是否出现内存泄漏问题
    // console.log(tf.memory())
    if (brightMean > this.data.brightThreshold)
      return 'TOO_BRIGHT'
    if (brightMean < this.data.dimThreshold)
      return 'TOO_DIM'
    return 'DETECT_SUCCESS'
  },
  reload(){
    // TODO
    this._listener.start()
    this.setData({
      status: 'DETECT_NOT_FOUND'
 })
  },
  // 把成功检测的人脸定格并呈现在界面中
  // 成功瞬间拍一张图片作为定格的图片
  lockFace() {
    const ctx = wx.createCameraContext()
    ctx.takePhoto({
      quality: 'low',
      success: res => {
        console.log(res.tempImagePath)
        this.setData({
          faceImage: res.tempImagePath
        })
      }
    })
  },
  retrunLogin(){
    wx.redirectTo({
      url: '../login/index',
    })
  },
  faceReconition() {
    //上传到后端进行处理
    // TODO
    this.setData({
      status: 'RECOGNITION_SUCESS'
    })
    wx.showToast({
      title: '识别成功',
      icon: 'success'
    })

  },

  // find的确认按钮
  onConfirm() {
        // wx.request({
        //   url: `${devConfig.url}/login/postImg`,//MaterialEntryAdd 后端接口名称
        //   method: "POST",
        //   data: {
        //     "faceImg": this.data.faceImage,
        //     "userName" : wx.getStorageSync('userName')
        //   },// 向后端传参
        //   success:(res)=>{
        //     console.log(res.data)
        //     wx.showToast({  //显示登录成功信息
        //       title: '图片上传成功',
        //       icon: 'success',
        //       duration: 2500
        //     })
        //     //已经登录直接跳转首页
        //     wx.switchTab({
        //       url: '../map/index',
        //     })
        //   },
        //   fail: (err) => {
        //       wx.showToast({  //显示打卡失败信息
        //       title: '打卡失败！！请退出重新登录',
        //       icon: 'fall',
        //       duration: 2500
        //     })
        //     console.log(err);
        //   }
        // });

            wx.uploadFile({
              url: `${devConfig.url}/postImg`, //仅为示例，非真实的接口地址
              filePath: this.data.faceImage,                  //要传的图片路径
              name: 'file',                                //获取图片二进制文件的key
              formData: {                                  //其他需要携带的参数
                'userName': wx.getStorageSync('userName')
              },
              success (res){
                console.log(res);
                wx.switchTab({
                  url: '../map/index',
                })
                //do something
              }
            })

    //跳转到主页

  }
 
})