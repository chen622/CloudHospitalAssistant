<template>
    <div style="background-image: url('../assets/VCG4178853233.jpg')">
        <a-row type="flex" align="middle" justify="center">
            <a-col span="8">
                <img style="width: 100%" src="../assets/logo/logo-grey-white.png">
            </a-col>
            <a-col span="24" style="text-align: center;font-size: 30px">
                <h1>欢迎使用云医助理</h1>
            </a-col>
            <a-col span="8">
                <a-form class="user-layout-login" :form="form" @submit="handleSubmit">
                    <a-tabs defaultActiveKey="1" style="text-align: center" size="large">
                        <a-tab-pane key="1" tab="账号密码登录">
                            <a-form-item>
                                <a-input size="large" type="text" placeholder="用户名"
                                         v-decorator="['username',{rules: rules.username}]">
                                    <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                                </a-input>
                            </a-form-item>

                            <a-form-item>
                                <a-input size="large" type="password"
                                         placeholder="密码"
                                         v-decorator="['password',{rules: rules.password}]">
                                    <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                                </a-input>
                            </a-form-item>
                        </a-tab-pane>
                    </a-tabs>

                    <a-form-item>
                        <a-checkbox v-model="checked">记住密码</a-checkbox>
                    </a-form-item>

                    <a-form-item style="margin-top:24px">
                        <a-button :loading="logining" size="large" type="primary" class="login-button"
                                  style="width: 100%" @click="handleSubmit">登录
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-col>
        </a-row>
    </div>
</template>

<script>

    import '../tencentCheck'
    import '../assets/VCG4178853233.jpg'

    export default {
        name: 'Login',
        data () {
            return {
                form: this.$form.createForm(this),
                loginForm: {
                    username: '',
                    password: '',
                },
                rules: {
                    username: [{required: true, message: '请输入用户名', trigger: 'blur'}, {
                        min: 3,
                        message: "用户名长度应大于5",
                        trigger: 'blur'
                    }],
                    password: [{required: true, message: '请输入密码', trigger: 'blur'}]
                },
                checked: false,
                logining: false
            }
        },
        methods: {
            handleSubmit () {
                let that = this
                this.form.validateFields((err) => {
                        if (!err) {
                            // // eslint-disable-next-line
                            // let captcha1 = new TencentCaptcha('2073984460', function (res) {
                            //     if (res.ret === 0) {
                            //         // console.log(res)
                            //         that.login()
                            //
                            //     }
                            // })
                            // captcha1.show()
                            that.login()
                        }
                    },
                );
            },
            login () {
                let that = this
                that.logining = true
                that.$store.commit("setOtherLoading", {loading: true, type: 1})
                sessionStorage.removeItem("token")
                that.$api.get('/user/login', this.form.getFieldsValue(),
                    function (res) {
                        that.$store.commit('setLoading', false)
                        that.logining = false
                        if (res.config && res.config.method === 'get') {
                            sessionStorage.setItem("token", res.data.Authorization)
                            that.$store.commit("setUserType", parseInt(res.data.userType))
                            that.$store.commit("setLogin", true)
                            that.$router.replace({path: "/"})
                            that.$message.destroy()
                            that.$message.success("登录成功！")
                            that.$api.get("/user/function", null,
                                res => {
                                    that.$store.commit("setUrls", res.data)
                                }, () => {
                                })
                        }
                    }, function (err) {
                        that.$store.commit('setLoading', false)
                        that.logining = false
                        if (err) {
                            if (err.response && err.response.status === 403) {
                                sessionStorage.removeItem("token")
                                that.$message.error("用户名或密码错误");
                            } else {
                                that.$message.error("网络错误")
                            }
                            // eslint-disable-next-line
                            console.log('API error: ' + err)
                        }
                    }
                )
            }
        }
    }
</script>

<style scoped>

</style>
