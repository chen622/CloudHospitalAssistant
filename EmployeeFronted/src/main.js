import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import api from './api'
import 'ant-design-vue/dist/antd.css'
import store from './store'
import Print from './print'
// import myCharts from './myCharts'

// const url = "http://localhost:8020"
// const url = "https://his.ccm.ink:8010"

// Vue.use(myCharts)
Vue.config.productionTip = false

Vue.use(Antd)
Vue.use(Print) // 注册
Vue.prototype.$api = api
Vue.prototype.$url = api.url


Vue.prototype.dateToTimeStamp = function (date) {

    return this.datetimeToTimeStamp(date + " 00:00:00")
}

Vue.prototype.datetimeToTimeStamp = function (datetime) {
    let Time = new Date(datetime)
    return Time.getTime()
}

Vue.filter('timeStampToDatetime', function (value) {
    let date = new Date(value)
    let y = date.getFullYear()
    let MM = date.getMonth() + 1
    MM = MM < 10 ? ('0' + MM) : MM
    let d = date.getDate()
    d = d < 10 ? ('0' + d) : d
    let h = date.getHours()
    h = h < 10 ? ('0' + h) : h
    let m = date.getMinutes()
    m = m < 10 ? ('0' + m) : m
    let s = date.getSeconds()
    s = s < 10 ? ('0' + s) : s
    return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s
})
Vue.filter('timeStampToDate', function (value) {
    let date = new Date(value)
    let y = date.getFullYear()
    let MM = date.getMonth() + 1
    MM = MM < 10 ? ('0' + MM) : MM
    let d = date.getDate()
    d = d < 10 ? ('0' + d) : d
    return y + '-' + MM + '-' + d
})


let vue = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')


export default vue


