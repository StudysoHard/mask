<template>
    <div>
        <ul class = "drawing-panel">
            <li class="bmap-btn bmap-circle" id="circle" @click="draw(this)"></li>
        </ul>
        <div id = "container"></div>
    </div>
</template>

<style>
    body, html, #container {width: 100%; height: 100%; overflow: hidden; margin: 0; font-family: "微软雅黑";}
        ul li {list-style: none;}
        .info {
            z-index: 999;
            width: auto;
            min-width: 22rem;
            padding: .75rem 1.25rem;
            margin-left: 1.25rem;
            position: fixed;
            top: 1rem;
            background-color: #fff;
            border-radius: .25rem;
            font-size: 14px;
            color: #666;
            box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
        }
        .drawing-panel {
            z-index: 999;
            position: fixed;
            bottom: 3.5rem;
            margin-left: 2.5rem;
            padding-left: 0;
            border-radius: .25rem;
            height: 47px;
            box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
        }
        .bmap-btn {
            border-right: 1px solid #d2d2d2;
            float: left;
            width: 64px;
            height: 100%;
            background-image: url(//api.map.baidu.com/library/DrawingManager/1.4/src/bg_drawing_tool.png);
            cursor: pointer;
        }
        .drawing-panel .bmap-marker {
            background-position: -65px 0;
        }
        .drawing-panel .bmap-polyline {
            background-position: -195px 0;
        }
        .drawing-panel .bmap-rectangle {
            background-position: -325px 0;
        }
        .drawing-panel .bmap-polygon {
            background-position: -260px 0;
        }
        .drawing-panel .bmap-circle {
            background-position: -130px 0;
        }
</style>

<script>
import { request } from '../network/request';

  export default {
    data() {
      return {
        reload:this.reload,
        tableData: []
      }
    },
    mounted() {
      this.initMethod()
    } ,
    methods:{
        initMethod(){
            var map = new BMapGL.Map("container", {enableMapClick:false}); // 创建Map实例,GL版命名空间为BMapGL(鼠标右键控制倾斜角度)
            map.centerAndZoom(new BMapGL.Point(116.404, 39.915), 11);      // 初始化地图,设置中心点坐标和地图级别
            map.enableScrollWheelZoom(true);                               // 开启鼠标滚轮缩放

            var styleOptions = {
                strokeColor: '#5E87DB',   // 边线颜色
                fillColor: '#5E87DB',     // 填充颜色。当参数为空时，圆形没有填充颜色
                strokeWeight: 2,          // 边线宽度，以像素为单位
                strokeOpacity: 1,         // 边线透明度，取值范围0-1
                fillOpacity: 0.2          // 填充透明度，取值范围0-1
            };
            var labelOptions = {
                borderRadius: '2px',
                background: '#FFFBCC',
                border: '1px solid #E1E1E1',
                color: '#703A04',
                fontSize: '12px',
                letterSpacing: '0',
                padding: '5px'
            };

            // 实例化鼠标绘制工具
            var drawingManager = new BMapGLLib.DrawingManager(map, {
                isOpen: true,        // 是否开启绘制模式
                enableCalculate: false, // 绘制是否进行测距测面
                enableSorption: true,   // 是否开启边界吸附功能
                sorptiondistance: 20,   // 边界吸附距离
                circleOptions: styleOptions,     // 圆的样式
                polylineOptions: styleOptions,   // 线的样式
                polygonOptions: styleOptions,    // 多边形的样式
                rectangleOptions: styleOptions,  // 矩形的样式
                labelOptions: labelOptions,      // label样式
            });  
        },
        draw(){
            var arr = document.getElementsByClassName('bmap-btn');
            for(var i = 0; i<arr.length; i++) {
                arr[i].style.backgroundPositionY = '0';
            }
            e.style.backgroundPositionY = '-52px';
            switch(e.id) {
                case 'marker': {
                    var drawingType = BMAP_DRAWING_MARKER;
                    break;
                }
                case 'polyline': {
                    var drawingType = BMAP_DRAWING_POLYLINE;
                    break;
                }
                case 'rectangle': {
                    var drawingType = BMAP_DRAWING_RECTANGLE;
                    break;
                }
                case 'polygon': {
                    var drawingType = BMAP_DRAWING_POLYGON;
                    break;
                }
                case 'circle': {
                    var drawingType = BMAP_DRAWING_CIRCLE;
                    break;
                }
            }
            // 进行绘制
            if (drawingManager._isOpen && drawingManager.getDrawingMode() === drawingType) {
                drawingManager.close();
            } else {
                drawingManager.setDrawingMode(drawingType);
                drawingManager.open();
            }
        }
    }
  };
</script>