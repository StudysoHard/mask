import random
import redis
from pymilvus import connections, FieldSchema, CollectionSchema, DataType, Collection,  utility
import time
import calendar
from controller.utils import snowFlow
from controller.utils.deepface import changeImgToMilvus

# This example shows how to:
#   1. connect to Milvus server
#   2. create a collection
#   3. insert entities
#   4. create index
#   5. search


_HOST = 'localhost'
_PORT = '19530'
_DIM = 2622
# Const names
_COLLECTION_NAME = 'face_milvus'
_XIN_COLLECTION_NAME = 'xin_face_milvus'

_ID_FIELD_NAME = 'id_field'
_XIN_ID_FIELD_NAME = 'xin_id_field'
_VECTOR_FIELD_NAME = 'face_vector'

# Vector parameters
snowId = snowFlow.IdWorker(1, 2, 0)

_INDEX_FILE_SIZE = 32  # max file size of stored index

# Index parameters
_METRIC_TYPE = 'IP'
_INDEX_TYPE = 'IVF_FLAT'
_NLIST = 1024
_NPROBE = 16
_TOPK = 1

# facename 的字典
nameList = {}

r = redis.Redis("101.43.42.54", port=6379)


# Create a Milvus connection
def create_connection():
    print(f"\nCreate connection...")
    connections.connect(host=_HOST, port=_PORT)
    print(f"\nList connections:")
    print(connections.list_connections())


# 不存在就创建collection
def boolean_collection():
    field1 = FieldSchema(name=_ID_FIELD_NAME, dtype=DataType.INT64, description="int64", is_primary=True)
    field2 = FieldSchema(name=_VECTOR_FIELD_NAME, dtype=DataType.FLOAT_VECTOR, description="float vector", dim=_DIM,
                         is_primary=False)
    field3 = FieldSchema(name="faceName", dtype=DataType.INT64, description="what face it is")
    schema = CollectionSchema(fields=[field1, field2, field3], description="collection description")
    if (has_collection(_COLLECTION_NAME) == False):
        return Collection(name=_COLLECTION_NAME, data = None ,schema=schema)
    else :
        return Collection(name=_COLLECTION_NAME, schema=schema)

# 不存在就创建collection
def boolean_xin_collection():
    field1 = FieldSchema(name=_XIN_ID_FIELD_NAME, dtype=DataType.INT64, description="int64", is_primary=True)
    field2 = FieldSchema(name=_VECTOR_FIELD_NAME, dtype=DataType.FLOAT_VECTOR, description="float vector", dim=_DIM,
                         is_primary=False)
    schema = CollectionSchema(fields=[field1, field2], description="collection description")
    if (has_collection(_XIN_COLLECTION_NAME) == False):
        return Collection(name=_XIN_COLLECTION_NAME, data = None,schema=schema)
    else :
        return Collection(name=_XIN_COLLECTION_NAME, schema=schema)


def has_collection(name):
    return utility.has_collection(name)


# Drop a collection in Milvus
def drop_collection(name):
    collection = Collection(name)
    collection.drop()
    print("\nDrop collection: {}".format(name))


# List all collections in Milvus
def list_collections():
    print("\nlist collections:")
    print(utility.list_collections())

def insert(collection , human_face, faceId):
    global snowId
    # 当前时间错
    ts = calendar.timegm(time.gmtime())
    # 雪花算法的id
    milvus_id = snowId.get_id()
    # 插入milvus的数据
    data = [
        [milvus_id],
        # [[random.random() for _ in range(dim)]],
        [human_face],
        [faceId]
    ]
    collection.insert(data)
    return milvus_id

def insert_xin_colletion(collection , human_face ):
    global snowId
    # 当前时间错
    ts = calendar.timegm(time.gmtime())
    # 雪花算法的id
    milvus_id = snowId.get_id()
    # 插入milvus的数据
    data = [
        [milvus_id],
        # [[random.random() for _ in range(dim)]],
        [human_face],
    ]
    collection.insert(data)
    # 返回插入的  milvus 的id
    return ts

def get_entity_num(collection):
    print("\nThe number of entity:")
    print(collection.num_entities)


def create_index(collection, filed_name):
    index_param = {
        "index_type": _INDEX_TYPE,
        "params": {"nlist": _NLIST},
        "metric_type": _METRIC_TYPE}
    collection.create_index(filed_name, index_param)
    print("\nCreated index:\n{}".format(collection.index().params))


def drop_index(collection):
    collection.drop_index()
    print("\nDrop index sucessfully")


def release_collection(collection):
    collection.release()


def search(collection, vector_field, id_field, search_vectors):
    global snowId
    newFace = False
    search_param = {
        "data": [search_vectors],
        "anns_field": vector_field,
        "param": {"metric_type": _METRIC_TYPE, "params": {"nprobe": _NPROBE}},
        "limit": _TOPK,
        "expr": "id_field > 0"}
    results = collection.search(**search_param)
    for i, result in enumerate(results):
        # print("\nSearch result for {}th vector: ".format(i))
        for j, res in enumerate(result):
            queryDict = collection.query(
                expr="id_field in ["+str(res.id)+"]",
                output_fields=["id_field","faceName"]
            )
            # 查询出来匹配度最高的哪一个人的 faceName
            faceName = queryDict[0]['faceName']
            print("distance 是 : "+str(res.distance))
            if (res.distance > 0.65):
            # 匹配度满足
                if(nameList.get(str(faceName)) == None):
                    #     断线重启
                    print("people   no insert milvue")
                elif(int(nameList.get(str(faceName))) < 5):
                    insert(collection,search_vectors,faceName)
                    nameList[str(faceName)] += 1
                    print("people increan 1")
                else:
                    print("people   no insert milvue")
            else:
                # 匹配度低
                faceName = snowId.get_id()
                nameList[str(faceName)] = 1
                insert(collection,search_vectors,faceName)
                newFace = True
                print("catch new people")
            return queryDict[0]['id_field'] , faceName ,newFace , False
        # 里面没有数据  这个时候插入初始化milvus库
        print("init milvus")
        faceName = snowId.get_id()
        nameList[str(faceName)] = 1
        milvus_id =  insert(collection,search_vectors,faceName)
        return milvus_id , faceName , False , False

def search_xin(collection, vector_field, id_field, search_vectors):
    global snowId
    newFace = False
    search_param = {
        "data": [search_vectors],
        "anns_field": vector_field,
        "param": {"metric_type": _METRIC_TYPE, "params": {"nprobe": _NPROBE}},
        "limit": _TOPK,
        "expr": "xin_id_field > 0"}
    results = collection.search(**search_param)
    for i, result in enumerate(results):
        # print("\nSearch result for {}th vector: ".format(i))
        for j, res in enumerate(result):
            print("新冠的 distance最高是 ： "  + str(res.distance))
            if (res.distance > 0.5):
                # 库中查出来匹配度最高
                queryDict = collection.query(
                    expr="xin_id_field in ["+str(res.id)+"]",
                    output_fields=["xin_id_field"]
                )
                # 查询出来匹配度最高的哪一个人的 faceName
                xin_id_field = queryDict[0]['xin_id_field']
                return xin_id_field, None, "none", "yes"
    return None , None , None , "no"

# 入milvus库
def insertAndFindMilvus(human_face):
    # create a connection
    create_connection()
    # 去新冠库中匹配
    xin_collection = boolean_xin_collection()
    xin_collection.load()
    if(xin_collection.has_index() == False):
        create_index(xin_collection,_VECTOR_FIELD_NAME)
    dataFace , faceName , newFace , flag =  search_xin(xin_collection,_VECTOR_FIELD_NAME,_XIN_ID_FIELD_NAME,human_face)
    if(flag == "yes"):
        return dataFace , faceName , newFace , True
    # find collection is created ?
    collection =  boolean_collection()
    collection.load()
    if(collection.has_index() == False):
        create_index(collection,_VECTOR_FIELD_NAME)
    # search and insert
    return search(collection, _VECTOR_FIELD_NAME, _ID_FIELD_NAME, human_face)

# 接收图片vector  将这个  插入到milvus中
def insertXinCollection(human_face):
    create_connection()
    xin_collection = boolean_xin_collection()
    # 加载colletion
    xin_collection.load()
    if (xin_collection.has_index() == False):
        create_index(xin_collection, _VECTOR_FIELD_NAME)
    # 插入其中
    return insert_xin_colletion(xin_collection , human_face)
