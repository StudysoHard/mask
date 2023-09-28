# mask
口罩识别和数据分析系统，系统能够在多种情况下检测识别口罩的情况，支持数据流的接入分析进而绘制出对应的人员移动位置轨迹，并且已经部署到微信小程序中，支持免安装开箱即用的服务。
## 技术栈 
后端语言

java8 + python 

前端语言

vue3.0 + element-ui + 微信小程序

数据交互层应用

redis / kafka 【这一版用redis】

持久层应用

mysql + milvus + redis

资源层应用

ffmpeg

WEB应用层框架

spring mvc + flask

持久层框架

mybaties-plus

## 目录说明

common ： 放用到的python依赖，conda 环境啥的

weixing:  微信小程序前端

face-vue: pc的前端，轨迹分析，流播放啥的都在这里

face-boot: java后端，包括微信小程序的后端和pc管理的后端

python:   深度学习的项目，早期三个模块是一起的，后面太臃肿的部署不起就简化了

- asyncStart  主服务，里面有抽象了几个接口用来判断输入图片的口罩佩戴情况 
- handleStream   视频流管理的服务，用于实时接入流，处理流，截取流图片，内部已经实现iou的功能，防止处理相似数据
- mini-deepface   简化的deepface 项目，已经抽象出几个接口比较人脸的拟合度



sql: 放sql的初始化脚本

## 功能展示

1. 小程序端的口罩识别
部署全部服务之后登录小程序，点击巡检然后点击开始巡检按钮按钮，就可以进行巡检拍摄人员的巡检工作

2. 数据采集和分析
在小程序端进行巡检的时候可以在网页端的数据大屏中实时展示采集的结果

3. 小程序端可以在首页地图上获取到对应权限的摄像头信息，并且能够进行资源的申请

4. 网页端能够进行视频流的播放，并且可以切换不同的流服务

5. 口罩识别模块会实时采集摄像头中未佩戴口罩人员的数量加入到数据大屏展示中

6. 对于每次巡检的轨迹进行绘制，在地图上展示人员行动的路线

7. 具备人脸搜索功能，准确度在75%左右

8. 小程序还集成了聊天室的功能，可以在这里交流经验
等等

## 部署方式
这里我使用jenkins的方式配置部署，后续的接口更新，服务部署会更加的容易，配合上harbor的仓库服务部署起来简直太爽了【当然那时候没钱就没上harbor了】
1. 购买两台2核4g的云服务器
一台用来运行java后端，nginx  , 数据库, jenkins服务
一台用来运行口罩识别的python模块 , 流服务 , redis
tips: 如果你财大气粗当然可以再卖几台把服务给独立出来
2. 给服务器都安装上docker然后部署基础的服务 
mysql, redis , nginx这些都部署上去，教程晚上一搜一大堆
3. 部署jenkins
这个我推荐使用docker来部署，毕竟搞坏了重新挂载目录重启容器又是一条好汉
部署要求： 
- 需要在jenkins中集成maven环境 ， 安装maven插件
- jenkins中要集成jdk8 的环境

4. 部署java后端服务 包括小程序服务端和页面大屏的服务端 
参考连接： 
https://www.yuque.com/r/note/d6acb6ae-187c-4692-9ef8-d15e88b4c88f
5. 搞一个域名和ssh证书，将小程序的服务端口转发到443
这一步是小程序那边强制要求服务端必须要 https 没办法
6. 部署服务器的深度学习conda环境
执行 wget https://repo.anaconda.com/archive/Anaconda3-2019.10-Linux-x86_64.sh
获取安装包，然后sh 安装包全部 yes即可
然后创建一个tensorflow 的环境，将common中的environment.yaml 导入到该环境中
验证： 输入conda -v 出现版本号就是安装conda成功了
7. 安装ffmpeg配置rtmp的流服务器,并且将流推送出去
参考部署文档

https://blog.csdn.net/raoxiaoya/article/details/109130549 和 https://blog.csdn.net/yxpandjay/article/details/101211405

两个加起来就可以搭建一个流服务器
验证： 安装vlc 输入你服务对应的推流地址，能够播放出你推出来的视频

【本地验证的时候可以使用手机下载一个ip摄像头 软件，然后确定 pc能 ping通手机之后 使用手机播放的流来测试】

8. 运行python服务

  把项目common\python\目录的两个文件下载到电脑，并且将python服务中的  face_mask_detection.caffemodel ，修改 main.py 中的第 15 16 行为自己服务器上对应文件的路径
  然后上传至服务器，将环境切换为tensorflow ， 然后python main.js 【如果遇到依赖缺失按照提示安装即可】

以上就是系统后端部署的步骤，管理员的前端就不介绍了，你想要使用jenkins搞一个镜像也行，还是直接配置到ng也行
小程序的原码放到weixing文件夹里面了，可以导入自己的小程序里面去

## 写在最后
本项目的人工智能部分是基于github的开源项目魔改的
https://github.com/AIZOOTech/FaceMaskDetection
因为是在校开发的，本来是有实现未佩戴口罩的人脸识别功能，但是由于模型计算出来的准确率只有50%左右，，所以就搁置了
