// blazeface 模型的获取地址，这里要使用hs静态服务器工具
export const modelUrl = 'https://huawei-obs-3651.obs.cn-north-1.myhuaweicloud.com/model.json';
// 后端人脸识别服务接口地址
export const faceServerHost = 'http://192.168.199.134:5000/';
// 默认的摄像头为前置摄像头
export const devicePosition = 'front';
// 上弹框，开始状态是没有弹出
export const popupSetting = false;
// 光照强度阈值
export const dimThreshold = 20;
export const brightThreshold = 240;
//间隔多少帧检测一次人脸
export const detectFacePerFrame = 10;