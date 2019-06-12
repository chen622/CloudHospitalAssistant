import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import api from './api'
import 'ant-design-vue/dist/antd.css'
import store from './store'

Vue.config.productionTip = false

Vue.use(Antd)
Vue.prototype.$api = api


Vue.prototype.dateToTimeStamp = function (date) {
  
    return this.datetimeToTimeStamp(date + " 00:00:00")
}

Vue.prototype.datetimeToTimeStamp = function (datetime) {
    let Time = new Date(datetime);
    let timestamp = Time.getTime();
    return timestamp
}

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
