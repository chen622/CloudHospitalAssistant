import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login'
import Search from './views/Search'
import Charge from './views/Charge'
import Medicine from './views/Medicine'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/admin/user',
            name: 'adminUser',
            // route level code-splitting
            // this generates a separate chunk (about.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('./views/admin/User')
        },
        {
            path: '/admin/other',
            name: 'other',
            // route level code-splitting
            // this generates a separate chunk (about.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('./views/admin/Other')
        },
        {
            path: '/doctor/index',
            name: 'doctor',
            component: () => import('./views/doctor/Index')
        },
        {
            path: '/about',
            name: 'about',
            // route level code-splitting
            // this generates a separate chunk (about.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/patient/register',
            name: 'register',
            component: () => import('./views/patient/Register')
        },
        {
            path: '/charge',
            name: 'charge',
            component: Charge
        },
        {
            path:'/medicine',
            name:'medicine',
            component: Medicine
        }
    ]
})
