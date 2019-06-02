<template>
    <div id="app">
        <a-layout style="background: #fff">
            <a-layout-header class="header">
                <a-row type="flex" align="middle" justify="start">
                    <a-col :span="8">
                        <img class="logo" src="./assets/logo/logo-grey-none.png">
                    </a-col>
                    <a-col :span="16">
                        <a-menu class="right-title" v-model="current" mode="horizontal" :multiple="false">
                            <a-menu-item key="index" @click="toRouter('/home')">
                                <a-icon type="home"/>
                                首页
                            </a-menu-item>
                            <a-menu-item key="user">
                                <a-icon type="wechat"/>
                                患者平台
                            </a-menu-item>
                            <a-sub-menu>
                                <span slot="title" class="submenu-title-wrapper"><a-icon type="setting"/>控制台</span>
                                <a-menu-item key="login">登录</a-menu-item>
                                <a-menu-item key="about">关于</a-menu-item>
                            </a-sub-menu>
                        </a-menu>
                    </a-col>
                </a-row>
            </a-layout-header>
            <a-layout-content style="background: #fff">
                <router-view></router-view>
            </a-layout-content>
            <a-layout-footer style="background: #000;color: white">
                <a-row type="flex" justify="space-around" align="top">
                    <a-col span="8">
                        <img class="logo" src="./assets/logo/logo-white-none.png">
                    </a-col>
                    <a-col span="8">
                        <p class="footer-title" >东软智慧医院</p>
                        <p class="footer-info" >地址：沈阳市浑南区创新路66号</p>
                        <p class="footer-info" >电话：024-66666666</p>
                    </a-col>
                </a-row>
            </a-layout-footer>
        </a-layout>
    </div>
</template>

<script>
    import ACol from "ant-design-vue/es/grid/Col";

    export default {
        components: {ACol},
        data: () => ({
            current: ['mail'],
        }),
        methods: {
            toRouter: function (router) {
                this.$router.push({path: router})
            }
        },
    }
</script>

<style scoped>
    .header {
        background: #fff !important;
        box-shadow: 0 5px 8px #8b8b8b;
        position: relative;
        z-index: 10;
        max-width: 100%;
        padding: 0 100px !important;
        margin-bottom: 0;
        height: 70px;
    }

    .logo {
        width: 214px;
        height: 70px;
    }

    .right-title {
        float: right;
        height: 70px;
    }


    .ant-menu-item {
        height: 70px;
        line-height: 70px;
        min-width: 72px;
        font-size: 18px;

    }

    .ant-menu-submenu {
        height: 70px;
        line-height: 70px;
        min-width: 72px;
        font-size: 18px;

    }

    .footer-title {
        font-size: 20px;font-weight: bold
    }

    .footer-info {
        font-size: 14px;
        margin-bottom: 4px;
    }
</style>
