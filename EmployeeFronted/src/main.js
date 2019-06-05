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


new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
