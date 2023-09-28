import logging
from logging.handlers import RotatingFileHandler
from flask import Flask
from config import config_dict
from threading import Thread
import requests
from controller.utils.camera import VideoCamera
from time import sleep


# 工厂函数: 由外界提供物料, 在函数内部封装对象的创建过程
def create_app(config_type):  # 封装web应用的创建过程
    app = Flask(__name__)
    return app


