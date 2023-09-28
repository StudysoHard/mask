# from flask_script import Manager

import requests
import argparse
from controller import create_app
import controller.utils.static as static
from controller import create_app
from controller.utils.camera import VideoCamera

# 创建APP对象
app = create_app('dev')
# # 创建脚本管理
video_camera = None
global_frame = None
flag = True

# 接收端口
parser = argparse.ArgumentParser(description="subprocess task")
parser.add_argument('--port', type=int, default=0, help='set task run prot')
parser.add_argument('--streamUrl', type=str, default=0, help='this is stream ceamera get ')
parser.add_argument('--cameraId', type=int, default=0, help='this id was sign which camera is')
args = parser.parse_args()

inPort = args.port
static.streamUrl = args.streamUrl
static.cameraId = args.cameraId

# 视频流
@app.route('/')
def video_viewer():
    print("in")
    video_stream()



# 获取视频流
def video_stream():
    global video_camera
    video_camera = VideoCamera()
    while True:
        frame = video_camera.get_stream()

    # mgr.run()
app.run(threaded=True, host="localhost",port=inPort,debug=True)













