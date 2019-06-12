import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login'

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
            path: '/patient/charge',
            name: 'charge',
            component: () => import('./views/patient/Charge')
        },
        {
            path: '/medicine',
            name: 'medicine',
            component: () => import( './views/Medicine')
        },
        {
            path: '/inspection',
            name: 'inspection',
            component: () => import('./views/inspection/Inspection')
        },
        {
            path: '/retreatRegister',
            name: 'RetreatRegister',
            component: () => import('./views/patient/RetreatRegister')
        },
        {
            path: '/template',
            name: 'temp',
            component: () => import('./components/InvoiceTemplate')
        },
        {
            path: '/search',
            name: 'search',
            component: () => import('./views/patient/Search')
        },
        {
            path:'/finance/manage',
            name:'manage',
            component:()=> import('./views/admin/PaymentType')
        },
        {
            path:'/finance/check',
            name:'check',
            component:()=> import('./views/admin/DailySettle')
        }
    ]
})
