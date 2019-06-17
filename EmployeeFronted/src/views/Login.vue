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
                        <a-button size="large" type="primary" class="login-button"
                                  style="width: 100%" @click="handleSubmit">登录
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-col>
        </a-row>
    </div>
</template>

<script>
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
                checked: false

            }
        },
        methods: {
            handleSubmit () {
                let that = this
                this.form.validateFields((err) => {
                        if (!err) {
                            sessionStorage.removeItem("token")
                            that.$api.get('/user/login', this.form.getFieldsValue(),
                                function (res) {
                                    sessionStorage.setItem("token", res.headers.authorization)
                                    that.$api.get("/user/function", null,
                                        res => {
                                            that.$store.commit("setUrls", res.data)
                                        }, res => {
                                            // eslint-disable-next-line
                                            console.log('API error: ' + res)
                                        })
                                    that.$store.commit("setLogin", true)
                                    that.$store.commit("setUserType", parseInt(res.headers.usertype))
                                    that.$router.replace({path: "/"})
                                    that.$message.success("登录成功！")
                                }, function (err) {
                                    if (err) {
                                        if (err.response && err.response.status === 403) {
                                            sessionStorage.removeItem("token")
                                            that.$message.error("用户名或密码错误");
                                        }
                                        // eslint-disable-next-line
                                        console.log('API error: ' + err)
                                    }
                                }
                            )
                            // axios({
                            //     method: 'POST',
                            //     url: 'http://localhost:8078/user/login',
                            //     params: this.form.getFieldsValue()
                            // }).then(function (res) {
                            //     sessionStorage.setItem("token", res.headers.authorization)
                            //     setTimeout(() => {
                            //         that.$api.get("/user/function", null,
                            //             res => {
                            //                 that.$store.commit("setUrls", res.data)
                            //             }, res => {
                            //                 // eslint-disable-next-line
                            //                 console.log('API error: ' + res)
                            //             })
                            //     }, 4000)
                            //     that.$store.commit("setLogin", true)
                            //     that.$store.commit("setUserType", parseInt(res.headers.usertype))
                            //     that.$router.replace({path: "/"})
                            //     that.$message.success("登录成功！")
                            // }).catch(function (err) {
                            //     console.log(err)
                            //     if (err) {
                            //         if (err.response && err.response.status === 403) {
                            //             sessionStorage.removeItem("token")
                            //             that.$message.error("用户名或密码错误");
                            //         }
                            //         // eslint-disable-next-line
                            //         console.log('API error: ' + err)
                            //     }
                            // });
                        }
                    },
                );
            },
        }
    }
</script>

<style scoped>

</style>
