<template>
	<view class="page">
		<view class="uni-list">
			<view class="uni-list-cell" hover-class="uni-list-cell-hover" v-for="(value,key) in list" :key="key">
					<view @click="toDetail(value)" class="uni-list-cell-navigate uni-navigate-right uni-media-list " style="width: 100%;">
						<view class="uni-media-list-body" style="width: 100%;">
							<view class="uni-media-list-text-top">{{value.firstDiagnose[0].createTime | formatDate}}</view>
							
							<view class="uni-media-list-text-bottom uni-ellipsis" style="width: 100%;">
								<view  class="uni-flex uni-row" >
									<view style="width: 70%;">
										主要诊断	:	{{value.finalDiagnose.length==0?value.firstDiagnose[0].diseaseSecond.name:value.finalDiagnose[0].diseaseSecond.name}}  
									</view >
									
									<view style="width: 30%;">总金额： {{value.totalMoney}}</view>
								</view>
								
							</view>
						</view>
					</view>
			</view>
		</view>
	</view>
</template>

<script>
	import http from "../../store/http.js"
	export default {
		data() {
			return {
				imgUrls: [
					"../../static/shuijiao.jpg",
					"https://img-cdn-qiniu.dcloud.net.cn/uniapp/images/muwu.jpg",
					"https://img-cdn-qiniu.dcloud.net.cn/uniapp/images/cbd.jpg"
				],
				list: [{
						title: "2019-06-01",
						content: "主要诊断:感冒  总费用:200.00 检查项目:无",
					},
					{
							title: "2019-06-01",
							content: "主要诊断:感冒  总费用:200.00 检查项目:验血",
					},
					{
							title: "2019-06-01",
							content: "主要诊断:感冒  总费用:200.00 检查项目:无",
					},
					{
							title: "2019-06-01",
							content: "主要诊断:感冒  总费用:200.00 检查项目:无",
					},
					{
							title: "2019-06-01",
							content: "主要诊断:感冒  总费用:200.00 检查项目:无",
					},
					{
							title: "2019-06-01",
							content: "主要诊断:感冒  总费用:200.00 检查项目:无",
					},
				]
			}
		},
		onLoad() {
			setTimeout(() => {
				this.showImg = true;
			}, 400)
			this.getAllMR()
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
        },methods: {
			getAllMR(){
				let opts={
                 	url: '/medical_record/getAllRecordWithout',
                    method: 'GET'
	             };
			    http.httpTokenRequest(opts,null).then(res => {
					this.list=res.data.data
	            },error => {
					uni.showToast({
						title: '网络错误，请稍后重试',
						duration: 2000,
						icon:'none'
					});
				})    
			},toDetail(value){
				uni.navigateTo({
					url:'detail?MR='+JSON.stringify(value)
				})
			}
		}
	}
</script>

<style>
	image,
	swiper,
	.img-view {
		width: 750upx;
		height: 500upx;
	}
	.page-section-title{
		margin-top: 50upx;
	}
	.title {
		padding: 20upx;
	}
</style>
