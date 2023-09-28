# -*- coding=utf-8
from qcloud_cos import CosConfig
from qcloud_cos import CosS3Client
import os
import sys
import logging

# 正常情况日志级别使用INFO，需要定位时可以修改为DEBUG，此时SDK会打印和服务端的通信信息
logging.basicConfig(level=logging.INFO, stream=sys.stdout)

# 1. 设置用户属性, 包括 secret_id, secret_key, region等。Appid 已在CosConfig中移除，请在参数 Bucket 中带上 Appid。Bucket 由 BucketName-Appid 组成
secret_id = 'AKIDLWR6VdV7SWJp42jAKErDIDsGOFqADyTh'     # 替换为用户的 SecretId，请登录访问管理控制台进行查看和管理，https://console.cloud.tencent.com/cam/capi
secret_key = 'ziALFrCH5dvwg9KGuIPFp8AuObYEemdD'   # 替换为用户的 SecretKey，请登录访问管理控制台进行查看和管理，https://console.cloud.tencent.com/cam/capi
region = 'ap-shanghai'      # 替换为用户的 region，已创建桶归属的region可以在控制台查看，https://console.cloud.tencent.com/cos5/bucket
                           # COS支持的所有region列表参见https://cloud.tencent.com/document/product/436/6224
token = None               # 如果使用永久密钥不需要填入token，如果使用临时密钥需要填入，临时密钥生成和使用指引参见https://cloud.tencent.com/document/product/436/14048
scheme = 'https'           # 指定使用 http/https 协议来访问 COS，默认为 https，可不填

config = CosConfig(Region=region, SecretId=secret_id, SecretKey=secret_key, Token=token, Scheme=scheme)
client = CosS3Client(config)

def uploadCos(img_name,img_path):
    # 使用高级接口上传一次，不重试，此时没有使用断点续传的功能
    response = client.upload_file(
        # 桶名称
        Bucket='heyongqiang-1308389982',
        # 保存图片名  之后访问的时候拼接路径
        Key=img_name,
        # 本地的路径
        LocalFilePath=img_path,
        EnableMD5=False,
        progress_callback=None
    )
    return "https://heyongqiang-1308389982.cos.ap-shanghai.myqcloud.com/" + img_name
    # url :  https://heyongqiang-1308389982.cos.ap-shanghai.myqcloud.com/上传文件名