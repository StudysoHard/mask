<template>
  <div id="imageMap" />
</template>
<script>
 
import 'leaflet/dist/leaflet.css'
import 'leaflet/dist/leaflet'
import 'leaflet/dist/leaflet-src'
import 'leaflet/dist/leaflet-src.esm'
import * as L from 'leaflet'
import icon from 'leaflet/dist/images/marker-icon.png'
import iconShadow from 'leaflet/dist/images/marker-shadow.png'
const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow
})
L.Marker.prototype.options.icon = DefaultIcon
 
export default {
  name: 'ImageMap',
  data() {
    return {
      imageMap: ''
    }
  },
  mounted() {
    this.initDate()
  },
  methods: {
    initDate() {
      var map = L.map('imageMap', {
        minZoom: 1,
        maxZoom: 4,
        center: [0, 0],
        zoom: 1,
        crs: L.CRS.Simple // 这表明leaflet使用1：1映射，在屏幕像素和经纬度坐标系统之间。换句话说，我们的图片是平的，不是全球的，我们在投影一张平面图片。
      })
      // 定义了图片尺寸和它的路径，路径可以引用网络链接
      var w = 4000
      var h = 3000
      var url = 'http://192.168.0.121:33334/u/202112/15135947bnp3.jpg'
      // 把图片通过地图的方式放出来，所以需要一个像素坐标到经纬度坐标（系统）的转换。 把西南，东北角的像素坐标逆映射为经纬度坐标网。
      var southWest = map.unproject([0, h], map.getMaxZoom() - 1)
      var northEast = map.unproject([w, 0], map.getMaxZoom() - 1)
      var bounds = new L.LatLngBounds(southWest, northEast)
      L.imageOverlay(url, bounds).addTo(map)
 
      L.marker([0, 0])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.marker([-230, 156])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.marker([-230, 320])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.marker([-120, 116])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.marker([-190, 356])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.marker([-100, 56])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.marker([-80, 400])
        .addTo(map)
        .bindPopup('<b>文字提示</b><br />I am a popup.')
        .openPopup()
      L.polygon([
        [-239, 77],
        [-239, 133],
        [-141, 148],
        [-141, 110]
      ])
        .addTo(map)
        .bindPopup('I am a polygon.')
 
      map.setMaxBounds(bounds)
    }
  }
}
</script>
<style>
#imageMap {
  width: 100%;
  height: 100%;
  border: 1px solid #ccc;
  margin-bottom: 10px;
}
</style>