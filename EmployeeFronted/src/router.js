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
        },{
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
            path: '/medicine/index',
            name: 'medicine',
            component: () => import( './views/Medicine/Medicine')
        },
        {
            path: '/inspection/index',
            name: 'inspection',
            component: () => import('./views/inspection/Inspection')
        },
        {
            path: '/retreatRegister',
            name: 'RetreatRegister',
            component: () => import('./views/patient/RetreatRegister')
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
            component:()=> import('./views/Finance/DailySettle')
        },
        {
            path:'/finance/workload',
            name:'workload',
            component:()=> import('./views/Finance/WorkloadCalculate')
        }
    ]
})
