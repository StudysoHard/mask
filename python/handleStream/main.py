# coding=UTF8
from controller.utils.camera import VideoCamera
from flask import Flask,Response ,request
import requests

# parser = argparse.ArgumentParser(description="subprocess task")
# parser.add_argument('--port', type=int, default=0, help='set task run prot')
# parser.add_argument('--streamUrl', type=int, default=0, help='run url')
# args = parser.parse_args()
# inPort = args.port
# streamUrl = args.streamurl

# 创建APP对象
app = Flask(__name__)
# # 创建脚本管理
video_camera = None
global_frame = b''
flag = True


# 视频流
@app.route('/url')
def video_viewer():
    args =  request.args
    url = args['url']
    print("in")
    return Response(video_stream(url),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/camera_change',methods=["GET"])
def camera_change():
    url = request.args['url']
    video_camera.set(False,url)
    return "OK"


# 获取视频流
def video_stream(url):
    global video_camera
    global global_frame
    video_camera = VideoCamera(url,True)
    while True:
        frame = video_camera.get_stream()
        if frame is not None:
            global_frame = frame
            yield (b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')
        else:
            yield (b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + global_frame + b'\r\n\r\n')

# 处理的流
app.run(threaded=True, host="localhost",port=5002,debug=True)












