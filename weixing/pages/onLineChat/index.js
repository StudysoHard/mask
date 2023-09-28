Page({
  data: {
    url: 'ws://localhost:7777/ws',
    msgs: [],
    msg: '',
  },
  // 连接WebSocket服务  
  connectSocket() {    
    let _this = this;    
    // 连接websocket服务    
    let task = wx.connectSocket({      
      url: _this.data.url    
    });    
    // 监听websocket消息，并将接收到的消息添加到消息数组msgs中   
    task.onMessage(function(res) {       
      _this.setData({        
        msgs: [..._this.data.msgs, "接收到消息 -> " + res.data]      
      });    
    });    
    // 保存websocket实例     
    _this.setData({       
      socketTask: task,       
      msgs: [..._this.data.msgs,"连接成功!"]    
    });  
  },    
  
  // 获取输入内容，并临时保存在msg中  
  msgInput(e) {    
    this.setData({       
      msg: e.detail.value    
    });  
  },    
  
  // 发送消息  
  sendMsg() {    
    // 1.获取输入内容    
    let msg = this.data.msg;    
    // 2.发送消息到WebSocket服务端    
    this.data.socketTask.send({      
      data: msg    
    });  
  }
})