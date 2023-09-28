export default class SocketService {
    /**
     * 单例设计模式
     */
    static instance = null;
    static get Instance() {
        if (!this.instance) {
            this.instance = new SocketService()
        }
        return this.instance
    }

    //和服务器连接的socket对象
    ws = null;
    user = 11;
    socketData = null;

    //存储回调函数
    // callBackMapping = {};

    // 重新连接尝试的次数
    connectRetryCount = 0

    //定义连接服务器的方法
    connect() {
        //连接服务器
        if (!window.WebSocket) {
            return console.log('您的浏览器不支持WebSocket');
        }
        this.ws = new WebSocket('ws://114.116.98.155:8889/webSocket/' + this.user);
        //连接成功的事件
        this.ws.onopen = () => {
                console.log('连接服务端成功了');
                // 重置重新连接的次数
                this.connectRetryCount = 0
            }
            //1.连接服务器端失败
            //2.当连接成功之后, 服务器关闭的情况
        this.ws.onclose = () => {
                console.log('连接服务端失败');
                this.connectRetryCount++
                    setTimeout(() => {
                        this.connect()
                    }, 500 * this.connectRetryCount)
            }
            //得到服务端发送来的数据
        this.ws.onmessage = msg => {
            console.log('从服务器取到了数据');
            this.socketData = JSON.parse(msg.data)
                //真正从服务器发送过来的原始数据是 在msg中的data字段
                // console.log("this data : " + msg.data);
                // const recvData = JSON.parse(msg.data);
                // recvData = 
        }
    };
    //回调函数的注册
    // registerCallBack(socketType, callBack) {
    //   this.callBackMapping[socketType] = callBack;
    // };
    //取消某一个回调函数
    // unRegisterCallBack(socketType) {
    //   this.callBackMapping[socketType] = null;
    // };
}