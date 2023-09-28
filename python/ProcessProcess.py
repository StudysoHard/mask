# coding:utf-8
from flask import Flask,request,jsonify
import requests
from threading import Thread
from time import sleep
import subprocess
import random
import json
from json import JSONEncoder


# 定义存放的数组类
class elements:
    def __str__(self):
        return json.dumps(dict(self), ensure_ascii=False)

    def __repr__(self):
        return self.__str__()

    def __init__(self,cameraId,streamUrl):
        self.cameraId = cameraId
        self.streamUrl = streamUrl
        self.port = 0
        self.pid = 0
    def setPort(self,port,pid):
        self.port = port
        self.pid = pid

class MyEncoder(JSONEncoder):
    def default(self, obj):
        return obj.elements

# 管理收集线程的路由表
portList = []

jsonList = []
'''
管理特殊进程 
1. handle
2. nohandle
3. milvus
因为其他特殊线程的端口是固定的
'''
expire = []



'''
初始化的时候需要创建的模块
    1.  handleStream
    2.  nohandleStream
    3.  min-deepface
    4.  catch-data
:param element:
:return:
'''


# 创建收集线程
def initCatch(element):
    port =  random.randint(1000, 9999)
    # out = subprocess.getoutput("netstat -ano | findstr " + str(port))
    # while(out != ''):
    #     # 防止端口冲突
    #     port = random.randint(1000, 9999)
    #     out = subprocess.getoutput("netstat -ano | findstr " + str(port))
    # manage = subprocess.Popen("python ./asyncStart/async.py --port "+ str(port) + " --streamUrl " + str(element.streamUrl) + " --cameraId " + str(element.cameraId) ,
    #                      shell=True)
    # # 线程等待 1 秒
    # sleep(5)
    # subprocess.Popen("curl localhost:" + str(port), shell=True)
    # element.setPort(port,manage.pid)
    # portList.append(element)
    # jsonList.append(json.dumps(element.__dict__))
    # resp_obj = jsonify({'port': port , "pid" : manage.pid })
    resp_obj = jsonify({'port': port , "pid" : 66666 })
    return resp_obj

def initHandle():
    manage = subprocess.Popen("python ./handleStream/main.py",
                              shell=True)
    expire.append(manage.pid)
    print('initHandle')


def initMilvus():
    manage = subprocess.Popen("python ./mini-deepface/deep-main.py",
                              shell=True)
    res = subprocess.run("netstat -ano | findstr " + str(5500), shell=True)
    # while(res == None):
    #     sleep(2)
    #     print("no ready")
    sleep(10)
    subprocess.Popen("curl localhost:" + str(5500), shell=True)
    expire.append(manage.pid)
    print('init miluvs')

app = Flask(__name__)

# 初始化所有模块
@app.route('/init',methods=['GET'])
def init():
    # initHandle()
    initMilvus()
    return "success"

# 添加摄像头
# java 管理subprocess
@app.route('/addUrl',methods=['GET'])
def addStream():
    args =  request.args
    # 获得java端传递过来的 stream  和 cameraid
    streamUrl = args['streamUrl']
    cameraId = args['cameraId']
    element = elements(cameraId,streamUrl)
    return initCatch(element)

# todo 待完善
@app.route('/getAll',methods=['GET'])
def getAll():
    return json.dumps(jsonList)

@app.route('/stopCamera',methods=['GET'])
def stopCamera():
    args =  request.args
    port = args['port']
    pid = args['pid']
    # out = subprocess.getoutput("netstat -ano | findstr " + str(port))
    # if (out == ''):
    #     return "port is not used"
    # # windows 杀pid
    # subprocess.Popen("taskkill /pid " + str(pid) + " -t -f")
    print('stop Camera')
    return "success"

app.run(host="127.0.0.1",port=5004)