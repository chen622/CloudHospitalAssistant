<template>
    <div id="login2" class="main" >
        <img :src="imgUrl" style="margin: 100px 10px 10px 700px; height: 100px">
        <h1 style="text-align: center; font-size:medium">欢迎使用云医助理</h1>
        <a-form id="formLogin" class="user-layout-login" :form="form" @submit="handleSubmit" style="margin: 50px 10px 10px 650px; width: 400px; font-size: large">
            <a-tabs defaultActiveKey="1" @change="callback" style="text-align: center" size="large">
                <a-tab-pane key="1" tab="账号密码登录"  >
                    <a-form-item>
                        <a-input size="large" type="text" placeholder="账户: admin" v-model="loginform.username" v-decorator="['username',{{rules.username}}]" >
                            <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                        </a-input>
                    </a-form-item>

                    <a-form-item>
                        <a-input size="large" type="password" autocomplete="false" placeholder="密码: admin or ant.design" v-model="loginform.password" @keyup.enter.native="handleSubmit">
                            <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                        </a-input>
                    </a-form-item>
                </a-tab-pane>

                <a-tab-pane key="2" tab="手机号登录">
                    <a-form-item>
                        <a-input size="large" placeholder="手机号"></a-input>
                    </a-form-item>
                    <a-form-item>
                        <a-input size="large" placeholder="验证码"></a-input>
                    </a-form-item>
                </a-tab-pane>
            </a-tabs>

            <a-form-item>
                <a-checkbox v-model="checked">记住密码</a-checkbox>
                <router-link :to="{ name: 'recover', params: { user: 'aaa'} }" class="forge-password" style="float: right;">忘记密码</router-link>
            </a-form-item>

            <a-form-item style="margin-top:24px">
                <a-button size="large" type="primary" htmlType="submit" class="login-button" style="width: 100%">登陆</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>

<script>


    export default {
        name:'login2',
        components: {

        },
        data () {
            return {
                form: this.$form.createForm(this),
                imgUrl:require("../assets/logo-grey-white.png"),
                loginform:{
                    username: '',
                    password: '',
                },
                rules:{
                    username: [{required: true, message: '请输入用户名' ,trigger: 'blur'}],
                    password: [{required: true, message: '请输入密码' , trigger: 'blur'}]
                },
                checked: false

            }
        },

        methods: {
            callback(key){
                console.log(key)
            },
            handleSubmit(event){
                this.$refs.loginform.validate((valid) =>{
                       if(valid){

                       }
                })
            }

        }
    }
</script>

<style  scoped>

</style>
