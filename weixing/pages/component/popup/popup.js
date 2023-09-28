// {{component}}.js
Component({
  options: {
    multipleSlots: true // 在组件定义时的选项中启用多slot支持
  },
  /**
   * 组件的属性列表
   */
  properties: {
    title: {            // 属性名
      type: String,     // 类型（必填），目前接受的类型包括：String, Number, Boolean, Object, Array, null（表示任意类型）
      value: '标题'     // 属性初始值（可选），如果未指定则会根据类型选择一个
    },
    // 弹窗内容
    streamUrl: {
      type: String , 
      value:  `未知`
    },
    // 弹窗内容
    longitude: {
      type: String , 
      value:  `未知`
    },
    // 弹窗内容
    latitude: {
      type: String , 
      value:  `未知`
    },
    // 弹窗内容
    srcUser: {
      type: String , 
      value:  `未知`
    }
  },
  /**
   * 组件的初始数据
   */
  data: {
    flag: true,
  },
  /**
   * 组件的方法列表
   */
  methods: {
    //隐藏弹框
    hidePopup: function () {
      this.setData({
        flag: !this.data.flag
      })
    },
    //展示弹框
    showPopup (streamUrl , longitude , latitude , srcUser) {
      console.log(streamUrl);

      this.setData({
        flag: !this.data.flag,
        streamUrl: streamUrl ,
        longitude : longitude , 
        latitude: latitude,
        srcUser : srcUser
      })
    },
    /*
    * triggerEvent 用于触发事件
    */
    _close() {
      this.triggerEvent("close");
    },
    statechange(e) {
      console.log('live-player code:', e.detail.code)
    },
    error(e) {
      console.error('live-player error:', e.detail.errMsg)
    },
    getverifyCode(){
      console.log(this.data.streamUrl);
    }
  }
})
