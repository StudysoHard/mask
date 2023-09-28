import cv2
import threading
import os
import time,datetime
# video_process
import argparse
import requests
import numpy as np
import pygame
import multiprocessing
import controller.utils.static as static
import base64
import redis

from controller.utils.cosUpload import uploadCos
from utils.anchor_generator import generate_anchors
from utils.anchor_decode import decode_bbox
from utils.nms import single_class_non_max_suppression
from PIL import Image, ImageDraw, ImageFont
from threading import Thread
from time import sleep



Net = cv2.dnn.readNet("C:\\face_mask_detection.caffemodel", "C:\\face_mask_detection.prototxt")

# anchor configuration
feature_map_sizes = [[33, 33], [17, 17], [9, 9], [5, 5], [3, 3]]
anchor_sizes = [[0.04, 0.056], [0.08, 0.11], [0.16, 0.22], [0.32, 0.45], [0.64, 0.72]]
anchor_ratios = [[1, 0.62, 0.42]] * 5

# generate anchors
anchors = generate_anchors(feature_map_sizes, anchor_sizes, anchor_ratios)

# for inference , the batch size is 1, the model output shape is [1, N, 4],
# so we expand dim for anchors to [1, anchor_num, 4]
anchors_exp = np.expand_dims(anchors, axis=0)

id2class = {0: 'Mask', 1: 'NoMask'}
id2chiclass = {0: '佩戴口罩', 1: '没有佩戴口罩'}
colors = ((0, 255, 0), (255, 0 , 0))
flag_p = 1

url = "http://localhost:5500"
headers = {'content-type': 'application/json'}

red = redis.Redis("114.116.116.54", port=6379)
all_mask = 0
all_nomask = 0
pre_mask = 0
pre_nomask = 0
pre_member = 0
human_hold_time = 0
need_reset_face = True
catchFace = True
flag = False
nowIouList = []


def getIou(box1,wh=False):
    # 定义一个函数来计算IOU的值
    iou = 0
    for boxs in nowIouList:
        for box2 in boxs:
            # 判断bbox的表示形式
            if wh == False:
                # 使用极坐标形式表示：直接获取两个bbox的坐标
                xmin1, ymin1, xmax1, ymax1 = box1
                xmin2, ymin2, xmax2, ymax2 = box2
            else:
                # 使用中心点形式表示： 获取两个两个bbox的极坐标表示形式
                # 第一个框左上角坐标
                xmin1, ymin1 = int(box1[0] - box1[2] / 2.0), int(box1[1] - box1[3] / 2.0)
                # 第一个框右下角坐标
                xmax1, ymax1 = int(box1[0] + box1[2] / 2.0), int(box1[1] + box1[3] / 2.0)
                # 第二个框左上角坐标
                xmin2, ymin2 = int(box2[0] - box2[2] / 2.0), int(box2[1] - box2[3] / 2.0)
                # 第二个框右下角坐标
                xmax2, ymax2 = int(box2[0] + box2[2] / 2.0), int(box2[1] + box2[3] / 2.0)
            # 获取矩形框交集对应的左上角和右下角的坐标（intersection）
            xx1 = np.max([xmin1, xmin2])
            yy1 = np.max([ymin1, ymin2])
            xx2 = np.min([xmax1, xmax2])
            yy2 = np.min([ymax1, ymax2])
            # 计算两个矩形框面积
            area1 = (xmax1 - xmin1) * (ymax1 - ymin1)
            area2 = (xmax2 - xmin2) * (ymax2 - ymin2)
            # 计算交集面积
            inter_area = (np.max([0, xx2 - xx1])) * (np.max([0, yy2 - yy1]))
            # 计算交并比
            iou = max(iou, inter_area / (area1 + area2 - inter_area + 1e-6))
    # print(iou)
    return iou


# 加载中文
def puttext_chinese(img, text, point, color):
    pilimg = Image.fromarray(img)
    draw = ImageDraw.Draw(pilimg)
    fontsize = int(min(img.shape[:2])*0.04)
    font = ImageFont.truetype("simhei.ttf", fontsize, encoding="utf-8")
    y = point[1]-font.getsize(text)[1]
    if y <= font.getsize(text)[1]:
        y = point[1]+font.getsize(text)[1]
    draw.text((point[0], y), text, color, font=font)
    img = np.asarray(pilimg)
    return img

def getOutputsNames(net):
    layersNames = net.getLayerNames()
    return [layersNames[i - 1] for i in net.getUnconnectedOutLayers()]

# 捕捉到人脸
# todo cos
def cutImg(img,xmin,xmax,ymin,ymax):
    global url
    global headers
    img = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)
    cutImg = img[xmin:xmax,ymin:ymax]
    # 不存在就创建对应 camearId  的文件夹
    if not os.path.exists('./'+ str(static.cameraId)):
        os.mkdir('./'+ str(static.cameraId) )
    # 存入路径生成图片
    ts = int(round(time.time() * 1000))
    cv2.imwrite('./'+ str(static.cameraId) + '/' + str(ts) +'.jpg', cutImg)
    # 发送图片到  cos 上
    cosPath =  uploadCos(str(ts) +'.jpg','./'+ str(static.cameraId) + '/' + str(ts) +'.jpg')
    # 转base64 , 网络地址 给deepface 端
    r = open('./'+ str(static.cameraId) + '/' + str(ts) +'.jpg', "rb")
    res = r.read()
    # todo min-deepface
    base = base64.b64encode(res)
    # 发送到消息队列
    red.xadd("cutImg",
           {
                "img": "data:image/,"+str(base)[2:],
                "imgPath": cosPath,
                "cameraId": static.cameraId,
                "catchTime": ts
            }
    )
    # 构建请求参数
    # requestData = {
    #     "img":"data:image/,"+str(base)[2:],
    #     "imgPath" : cosPath,
    #     "cameraId" : static.cameraId,
    #     "catchTime" : ts
    # }
    # ret = requests.post(url, json=requestData, headers=headers)



def inference(image, conf_thresh=0.5, iou_thresh=0.4, target_shape=(160, 160), draw_result=True, chinese=True):
    global all_mask
    global all_nomask
    global pre_mask
    global pre_nomask
    global catchFace
    global pre_member
    global human_hold_time
    global preIouList
    global nowIouList

    height, width, _ = image.shape
    blob = cv2.dnn.blobFromImage(image, scalefactor=1/255.0, size=target_shape)
    net=Net
    net.setInput(blob)

    y_bboxes_output, y_cls_output = net.forward(getOutputsNames(net))
    # remove the batch dimension, for batch is always 1 for inference.
    y_bboxes = decode_bbox(anchors_exp, y_bboxes_output)[0]
    y_cls = y_cls_output[0]

    # To speed up, do single class NMS, not multiple classes NMS.
    bbox_max_scores = np.max(y_cls, axis=1)
    bbox_max_score_classes = np.argmax(y_cls, axis=1)

    # keep_idx is the alive bounding box after nms.
    keep_idxs = single_class_non_max_suppression(y_bboxes, bbox_max_scores, conf_thresh=conf_thresh, iou_thresh=iou_thresh)
    # keep_idxs  = cv2.dnn.NMSBoxes(y_bboxes.tolist(), bbox_max_scores.tolist(), conf_thresh, iou_thresh)[:,0]
    tl = round(0.002 * (height + width) * 0.5) + 1  # 厚度
    # 统计实时人脸个数
    real_time_faces = keep_idxs.shape
    count_mask_face = 0
    count_nomask_face = 0
    boxs = []
    for idx in keep_idxs:
        conf = float(bbox_max_scores[idx])
        class_id = bbox_max_score_classes[idx]
        bbox = y_bboxes[idx]
        # clip the coordinate, avoid the value exceed the image boundary.

        xmin = max(0, int(bbox[0] * width))
        ymin = max(0, int(bbox[1] * height))
        xmax = min(int(bbox[2] * width), width)
        ymax = min(int(bbox[3] * height), height)
        if(class_id == 1):
            # 没有佩戴口罩的人
            count_nomask_face += 1
            boxA = [xmin,ymin,xmax,ymax]
            boxs.append(boxA)
            if(getIou(boxA) < 0.6):
                # 剪裁人脸图片 并且读取转化为base64
                # print("截取图片")
                cutImg(image,ymin,ymax,xmin,xmax)
            # if(catchFace == True):
            #     deepFaceInterface()
            #     catchFace = False
        else:
            # 佩戴口罩的人
            count_mask_face += 1
    nowIouList.append(boxs)
    global need_reset_face
    need_reset_face = True

    if(pre_member != real_time_faces):

        # 如果视野人数和之前没有变化则不记录人员变化情况
        if (pre_nomask < count_nomask_face):
            # 有没有佩戴口罩的人被识别到了
            if(human_hold_time < 20):
                # 如果人脸不一样且在误判去区间内
                human_hold_time += 1
                need_reset_face = False
            else:
                # 大于误判区间
                all_nomask += (count_nomask_face - pre_nomask)
                human_hold_time = 0
                pre_nomask = count_nomask_face

        if (pre_mask < count_mask_face):
            if(human_hold_time < 20):
                # 如果人脸不一样且在误判去区间内
                human_hold_time += 1
                need_reset_face = False
            else:
                # 大于误判区间
                all_mask += (count_mask_face - pre_mask)
                human_hold_time += 1
                pre_mask = count_mask_face
    else:
        # 说明有人在视野中佩戴/脱去 口罩
        if(pre_nomask < count_nomask_face):
            # 脱去口罩
            if(human_hold_time < 20):
                human_hold_time += 1
                need_reset_face = False
            else:
                all_nomask += 1
                all_mask -= 1
        if(pre_mask < count_mask_face):
            # 带上口罩
            if(human_hold_time < 20):
                human_hold_time += 1
                need_reset_face = False
            else:
                all_nomask -= 1
                all_mask += 1
    if(need_reset_face == True):
        human_hold_time = 0
        pre_mask = count_mask_face
        pre_nomask = count_nomask_face
        pre_member = real_time_faces

    if(len(nowIouList) > 2):
        nowIouList.pop(0)
    # 保存上一个人脸记录
    # print("捕捉到佩戴口罩人脸总数 "+str(all_mask))
    # print("捕捉到没有佩戴口罩人脸总数  "+str(all_nomask))
    # print("捕捉到当前佩戴口罩人脸"+str(count_mask_face))
    # print("捕捉到当前没有佩戴口罩人脸"+str(count_nomask_face))
    # if(all_nomask != 0 or all_mask != 0):
    #     # 统计到有佩戴口罩的情况
    #     print("佩戴口罩率: " + str(all_mask/(all_nomask+all_mask)))


    def run(self):
        while self.isRunning:
            # 这里cv2.VideoCapture() read方法表示读取每一帧    ret 表示读取帧的正确性     frame表示读取每一帧的图片 是一个三维矩阵
            ret, frame = self.cap.read()
            print(frame)
            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            # 加载模型返回结果
            frame = inference(frame, target_shape=(260, 260), conf_thresh=0.5)
            frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
            if ret:
                self.out.write(frame)
        self.out.release()

    def stop(self):
        self.isRunning = False



    def __del__(self):
        self.out.release()


class VideoCamera(object):
    def __init__(self):
        # static.cameraId
        if (static.streamUrl == '0'):
            self.url = 0
        else:
            self.url = static.streamUrl
        self.cap = cv2.VideoCapture(self.url)

    # 退出程序释放摄像头
    def __del__(self):
        self.cap.release()


    # 对于视频流进行判断的
    def get_stream(self):
        ret, frame = self.cap.read()
        # 转化图片为 rgb格式
        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        # 加载模型
        frame = inference(frame, target_shape=(260, 260), conf_thresh=0.5)



