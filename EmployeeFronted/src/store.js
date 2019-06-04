import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        isLogin: false,
        userType: -1,
        urls: []
    },
    mutations: {
        setLogin (state, login) {
            state.isLogin = login
        },
        setUserType (state, userType) {
            state.userType = userType
        },
        setUrls (state, urls) {
            state.urls = urls
        }
    },
    actions: {}
})
