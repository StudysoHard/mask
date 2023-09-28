import axios from 'axios'

//方法四
export function request(config) {
    // 1.创建axios的实例
    const instance = axios.create({
        baseURL: 'http://www.heyongqiang.work:8889',
        method: 'get'
    })

    // 2.axios的拦截器
    // 2.1.请求拦截的作用
    instance.interceptors.request.use(config => {
        return config
    }, err => {
        // console.log(err);
    })

    // 2.2.响应拦截
    instance.interceptors.response.use(res => {
        // console.log(res);
        return res.data
    }, err => {
        console.log(err);
    })

    // 3.发送真正的网络请求---四种方法
    //axios内部也是用了promise函数
    return instance(config)
}