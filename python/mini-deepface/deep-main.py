# from flask_script import Manager
import redis
import time
from flask import Flask, jsonify, request, make_response
from controller.utils.deepface import changeImgToMilvus
from sklearn import preprocessing
import numpy as np
from controller.utils import milvus

# 创建APP对象
app = Flask(__name__)
r = redis.Redis("101.43.42.54", port=6379)

# 视频流
# 接收 asy 的base 64的图片入口
@app.route('/',methods=['GET'])
def img_catch():
    while True:
        flag = True
        for i in r.xrange("cutImg", 1):
            flag = False
            img = i[1][b'img'].decode('utf-8')
            imgPath = i[1][b'imgPath'].decode('utf-8')
            cameraId = i[1][b'cameraId'].decode('utf-8')
            catchTime = i[1][b'catchTime'].decode('utf-8')
            # r.xdel("mystream1", i[0])
            face_vector = changeImgToMilvus.changeImgToMilvus(img)
            res = face_vector / np.linalg.norm(face_vector)
            # deepface = changeImgToMilvus.changeImgToMilvus(img)
            # face_vector = preprocessing.scale(deepface)
            # dataFace, faceName, newFace, flag = milvus.insertAndFindMilvus(face_vector.tolist())
            dataFace, faceName, newFace, flag = milvus.insertAndFindMilvus(res.tolist())
            # 拿到之后删除这个cutimg
            r.xdel("cutImg", i[0])
            if (flag == False):
                # 添加到普通的消息队列中
                r.xadd("normalQueue",
                       {
                           "milvus_id": dataFace,
                           "faceName": faceName,
                           "is_new_face": int(newFace),
                           "img_url": imgPath,
                           "camera_id": cameraId,
                           "catch_time": catchTime
                       }
                       )
            else:
                # 添加到 新冠消息队列中
                r.xadd("xinQueue",
                       {
                           "milvus_id": dataFace,
                           "img_url": imgPath,
                           "camera_id": cameraId,
                           "catch_time": catchTime
                       }
                       )
            print('===========================================success=============================================')
        # 没有进循环
        if(flag == True):
            time.sleep(5)

# java端发送base64 的入口
@app.route('/insertXinMilvus',methods=['POST'])
def insertMilvus():
    img =  request.data.decode('utf-8')
    face_vector =  changeImgToMilvus.changeImgToMilvus(img)
    # 返回最终插入的 mivlus 的 id
    return str(milvus.insertXinCollection(face_vector))


# deepface mini 运行端口
app.run(threaded=True, host="localhost",port=5500,debug=True)













