// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import './element/index.css'
import axios from 'axios'
import VueAxios from 'vue-axios'
import store from './store'
import vueJsonp from 'vue-jsonp'
import BaiduMap from 'vue-baidu-map'
import VueSocketIO from 'vue-socket.io'

Vue.use(new VueSocketIO({
  debug: true,
  connection: 'http://127.0.0.1:5000/test'
}))
Vue.use(BaiduMap, {
  ak: '7rk2V6zD49gvsR41OwylhifNcgMYGCGh'
})
Vue.use(vueJsonp)
Vue.use(VueAxios, axios)
Vue.use(ElementUI)
// 将axios绑定
Vue.prototype.$axios = axios
Vue.config.productionTip = false
axios.defaults.baseURL = '/api/'

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
