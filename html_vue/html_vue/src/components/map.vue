<template>
  <div style="height:100%;">
    <div class="allmap" id="allmap">
    </div>
  </div>
</template>

<script>
import {MP} from '../map.js'
export default {
  name: 'map',
  data () {
    return {
      ak: '7rk2V6zD49gvsR41OwylhifNcgMYGCGh'
    }
  },
  mounted () {
    this.$nextTick(function () {
      MP(this.ak).then(BMap => {
        var value = '上海'
        // 百度地图API功能
        var map = new BMap.Map('allmap') // 创建Map实例
        map.centerAndZoom(value, 12) // 初始化地图,设置中心点坐标和地图级别
        // 添加地图类型控件
        map.addControl(new BMap.MapTypeControl({
          mapTypes: [
            BMAP_NORMAL_MAP,
            BMAP_HYBRID_MAP
          ]
        }))
        map.setCurrentCity(value) // 设置地图显示的城市 此项是必须设置的
        map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
      })
    })
  }
}
</script>
