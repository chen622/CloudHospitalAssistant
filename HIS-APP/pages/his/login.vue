<template>
    <view>
        <!-- #ifdef MP-WEIXIN -->
        <view v-if="isCanUse">
            <view>
                <view class='header'>
                    <image src='../../static/img/wx_login.png'></image>
                </view>
                <view class='content'>
                    <view>申请获取以下权限</view>
                    <text>获得你的公开信息(昵称，头像、地区等)</text>
                </view>

                <button class='bottom' type='primary' open-type="getUserInfo" withCredentials="true" lang="zh_CN" @getuserinfo="wxGetUserInfo">
                    授权登录
                </button>
            </view>
        </view>
        <!-- #endif -->
    </view>
</template>

<script>
	import store from '../../store/index.js'
    export default {
        data() {
            return {
                SessionKey: '',
                OpenId: '',
                name: null,
                avatarUrl: null,
                isCanUse: uni.getStorageSync('isCanUse')||true//默认为true
            };
        },
        methods: {
            //第一授权获取用户信息===》按钮触发
            wxGetUserInfo() {
                let _this = this;
                uni.getUserInfo({
                    provider: 'weixin',
                    success: function(infoRes) {
                        let name = infoRes.userInfo.name; //昵称
                        let avatarUrl = infoRes.userInfo.avatarUrl; //头像
                        try {
                            uni.setStorageSync('isCanUse', false);//记录是否第一次授权  false:表示不是第一次授权
                            _this.updateUserInfo();
                        } catch (e) {}
						_this.login();
                    },
                    fail(res) {}
                });
            },

　　　　　　//登录
                login() {
                let _this = this;
                uni.showLoading({
                    title: '登录中...'
                });
             
               // 1.wx获取登录用户code
                uni.login({
                    provider: 'weixin',
                    success: function(loginRes) {
                        let code = loginRes.code;
						console.log('************')
                        if (!_this.isCanUse) {
                            //非第一次授权获取用户信息
                            uni.getUserInfo({
                                provider: 'weixin',
                                success: function(infoRes) {
 　　　　　　　　　　　　　　　　　　　　　　//获取用户信息后向调用信息更新方法
                                    let name = infoRes.userInfo.name; //昵称
                                    let avatarUrl = infoRes.userInfo.avatarUrl; //头像
									_this.name=name
									_this.avatarUrl=avatarUrl
                                    _this.updateUserInfo(code);//调用更新信息方法
                                }
                            });
                        }
            
                        //2.将用户登录code传递到后台置换用户SessionKey、OpenId等信息
                        uni.request({
                            url: 'http://localhost:8078/wechat/login',
                            data: {
                                code: code,
								name:_this.name,
								avatarUrl:_this.avatarUrl
                            },
                            method: 'POST',
                            header: {
                                'content-type': 'application/json'
                            },
                            success: (res) => {
                                //openId、或SessionKdy存储//隐藏loading
                                uni.hideLoading();
								uni.setStorageSync('token',res.data.data.token)
								store.state.hasLogin=true
								console.log(uni.getStorageSync('token'))
								uni.switchTab({
									url:'index'
								})
                            }
                        });
                    },
                });
            },

        }
    }
</script>

<style>
    .header {
        margin: 90rpx 0 90rpx 50rpx;
        border-bottom: 1px solid #ccc;
        text-align: center;
        width: 650rpx;
        height: 300rpx;
        line-height: 450rpx;
    }

    .header image {
        width: 200rpx;
        height: 200rpx;
    }

    .content {
        margin-left: 50rpx;
        margin-bottom: 90rpx;
    }

    .content text {
        display: block;
        color: #9d9d9d;
        margin-top: 40rpx;
    }

    .bottom {
        border-radius: 80rpx;
        margin: 70rpx 50rpx;
        font-size: 35rpx;
    }
</style>