<template>
	<view >
		<view class="uni-list-cell" hover-class="uni-list-cell-hover" v-if="visible" >
			<view class="uni-list-cell-navigate " style="height: 200upx;">
				<view class="uni-media-list-logo" style="height: 180upx;width: 180upx;">
					<image :src="doctor.avatar"></image>
				</view>
				<view class="uni-media-list-body" style="height: 140upx;">
					<view class="uni-media-list-text-top">{{doctor.realName}}  <uni-tag style="margin-left: 10upx;" :mark="true" text="专家" type="primary" size="small" />  <uni-rate :size="18" :value="4" :disabled="true" /></view>
					<view class="uni-media-list-text-bottom uni-ellipsis">{{doctor.selfDescription}}</view>
				</view>
			</view>
		</view>
		<view class="uni-list" style="width: 99%;margin-top: 10upx;" v-if="visible" >
			<radio-group @change="radioChange">
				<uni-card 
				 is-full="true"
				 v-for="(item, index,limit) in items" :key="item.value"
				 :title="item.constantVariable.name" 
				 thumbnail="../../static/img/time.png" 
				 :extra="item.date | formatDate" 
				 >
					<view class="uni-flex uni-row" >
						<view style="width:33%;margin-left: 15%;">
							 <uni-tag type="primary" inverted="true" :text="item.registrationType.name" > </uni-tag>
						</view>
						
						<view style="width:33%;">
							<uni-tag :type="item.isValid?'success':'default'" inverted="true" :text="'限额:'+item.limitRegistrationAmount" > </uni-tag>
						</view>
							
						<view  style="width:33%;margin-left: 14%;">
							<uni-tag @click="reserve(item)" :type="item.isValid?'success':'default'"  :text="item.isValid?'挂号':'已满'" > </uni-tag>
						</view>
						
					</view>
				</uni-card>
			</radio-group>
		</view>
		
		
		<view  class="uni-padding-wrap uni-common-mt" v-if="!visible">
			<form @submit="formSubmit">
				<view class="uni-form-item uni-column">
					<view class="title">性别</view>
					<radio-group name="radio"  @change="radioChange">
						<label>
							<radio style="transform:scale(0.7)" value="0" />男
						</label>
						<label>
							<radio style="transform:scale(0.7)" value="1" />女
						</label>
					</radio-group>
				</view>
	
				<view class="uni-form-item uni-column">
					<view class="title">真实姓名</view>
					<input class="uni-input" name="name" v-model="name" type="text" placeholder="请输入姓名" />
				</view>
				<view class="uni-form-item uni-column">
					<view class="title">身份证</view>
					<input class="uni-input" name="ID" v-model="ID" type="idcard" placeholder="请输入身份证号码" />
				</view>
				<view class="uni-form-item uni-column">
					<view class="title">电话号码</view>
					<input class="uni-input" name="tel" v-model="tel" type="number" placeholder="请输入电话号码" />
				</view>
				<view class="uni-btn-v">
					<button formType="submit">提交</button>
				</view>
			</form>
		</view>
		
	</view>

</template>

<script>
	import uniCard from "@/components/uni-card/uni-card.vue"
	import uniList from '@/components/uni-list/uni-list.vue'
	import uniListItem from '@/components/uni-list-item/uni-list-item.vue'
	import uniRate from '@/components/uni-rate/uni-rate.vue'
	import uniTag from '@/components/uni-tag/uni-tag.vue'
	import http from '../../store/http.js'
	export default {
		components: {
			uniList,
			uniListItem,
			uniRate,
			uniTag,
			uniCard
		},
		data() {
			return {
				current:0,
				items: [],
				items2:[],
				doctor:[],
				patient:[],
				sex:-1,
				name:null,
				tel:null,
				ID:null,
				visible:true,
			}
		},filters: {
            formatDate: function (value) {
                let date = new Date(value);
                let y = date.getFullYear();
                let MM = date.getMonth() + 1;
                MM = MM < 10 ? ('0' + MM) : MM;
                let d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                return y + '-' + MM + '-' + d;
            }
        },methods:{
			formSubmit: function(e) {
				if(this.sex==null || this.name==null || this.tel==null || this.ID==null){
					uni.showToast({
						title: '信息未填写完整或填写不正确',
						duration: 2000
					});
				}else{
					this.patient.sex=this.sex
					this.patient.realName=this.name
					this.patient.identityId=this.ID
					this.patient.phoneNumber=this.tel
					let opts={
	                 	url: '/wechat/updatePatient',
	                    method: 'POST'
		             };
				    http.httpTokenRequest(opts,this.patient).then(res => {
				
						this.visible=true
						this.patient.confirm=true
						
		            },error => {
						uni.showToast({
							title: '网络错误，请稍后重试',
							duration: 2000,
							icon:'none'
						});
					})    
				}
			},
			radioChange: function(evt) {
				this.sex=evt.target.value
			},
			async getSchdule(){
			let opts={
                 	url: '/wechat/getSchedule/'+this.doctor.id,
                    method: 'GET'
             };
		    http.httpTokenRequest(opts, null).then(res => {
				this.items=res.data.data
				for(var i=0;i<this.items.length;i++){
					this.items[i].value=this.items[i].id
				}	
            },error => {
				uni.showToast({
					title: e.data,
					duration: 2000,
					icon:'none'
				});
			})    
				
			},reserve(item){
				if(this.patient.confirm==true){
					this.actualReverse(item)
					return
				}
				let opts={
	                 	url: '/wechat/getPatient',
	                    method: 'GET'
	             };
			    http.httpTokenRequest(opts, null).then(res => {
					this.patient=res.data.data
					if(this.patient.confirm==true){
						this.actualReverse(item)
					}else{
						this.sex=this.patient.sex
						this.name=this.patient.realName
						this.ID=this.patient.identityId
						this.tel=this.patient.phoneNumber
						this.visible=false
					}
	            },error => {
					uni.showToast({
						title: e.data,
						duration: 2000,
						icon:'none'
					});
				})    
			},actualReverse(item){
				let opts={
	                 	url: '/registration/preRegistration/'+item.id,
	                    method: 'POST'
	             };
			    http.httpTokenRequest(opts, null).then(res => {
					if(res.data.code!=100){
						uni.showToast({
							title: res.data.msg,
							duration: 2000,
							icon:'none'
						});
					}else{
						this.visible=true
						uni.showToast({
							title: '预约成功',
							duration: 1500
						});
					}
	            },error => {
					uni.showToast({
						title: '网络错误，请稍后重试',
						duration: 2000,
						icon:'none'
					});
				})
			}
		},
		onLoad(options) {
			this.doctor = JSON.parse(options.doctor)	
			this.getSchdule()
		}
	}
</script>
                                                                                                                                                                                                              
<style>
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #fff
	}

	view {
		font-size: 28upx;
		line-height: inherit
	}

	.example {
		padding: 0 30upx 30upx
	}

	.example-title {
		font-size: 32upx;
		line-height: 32upx;
		color: #777;
		margin: 40upx 25upx;
		position: relative
	}

	.example .example-title {
		margin: 40upx 0
	}

	.example-body {
		padding: 0 40upx
	}
</style>