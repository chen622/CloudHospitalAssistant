<template>
	<view  class="page">
		<view class="uni-list" style="width: 99%;margin-top: 10upx;margin-left: 0.5%;"  >
			<radio-group >
				<uni-card 
				 is-full="true"
				 style="width: 100%;margin-top: 10upx;white-space: pre-wrap;"
				 v-for="(item, index) in registrations" :key="index"
				 :title="item.title" 
				 thumbnail="../../static/img/time.png" 
				 >
				 <view class="uni-column" style="width: 100%;margin-top: 10upx;">
					<view class="uni-flex uni-row" style="width: 100%;" >
						<view style="width:33%;">
							<text>号码 :  </text>
							{{item.sequence}}</view>
						<view style="width:33%;">医生 :  {{item.doctorName}}</view>
						<view style="width:33%;">时段 :  {{item.period}}</view>						
					</view>
					<view style="width: 100%;" class="uni-flex uni-row">
						<view style="width: 50%;">{{item.createTime}}</view>
						<view style="width:50%;">
							 <uni-tag size="small" :type="getTag(item.state)" inverted="true" :text="item.stateName" > </uni-tag>
						</view>
					</view>
					</view>
				</uni-card>
			</radio-group>
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
		},data() {
			return {
				registrations:[],
			}
		},onLoad() {
			this.getRegistration()
		},methods: {
			formatDate (value) {
			    let date = new Date(value);
			    let y = date.getFullYear();
			    let MM = date.getMonth() + 1;
			    MM = MM < 10 ? ('0' + MM) : MM;
			    let d = date.getDate();
			    d = d < 10 ? ('0' + d) : d;
			    return y + '-' + MM + '-' + d;
			},
			getTag(state){
				if(state==807 || state==809)
					return 'default'
				if(state==804)
					return 'warning'
				if(state==801 || state==802)
					return 'primary'
				else
					return 'success'
			},getRegistration(){
				
				let opts={
                 	url: '/patient/getRegistrations',
                    method: 'GET'
	             };
	             
		    	http.httpTokenRequest(opts, null).then(res => {
					this.registrations=res.data.data
					this.registrations.forEach(item =>{
						item.title='看诊日期： '+   this.formatDate(item.scheduleTime)
						item.createTime='挂号日期：'+this.formatDate(item.createTime)
						item.stateName='诊断状态：'+item.stateName
					})
					console.log(this.registrations[0].title)
	            },error => {
					uni.showToast({
						title: '网络错误，请稍后重试',
						duration: 2000,
						icon:'none'
					});  
			})
		}
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
