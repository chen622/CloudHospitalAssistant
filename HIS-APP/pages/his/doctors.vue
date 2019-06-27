<template>
	<view class="container">

		<view class="page-body">
			<scroll-view style="width: 88%;margin-left: 6%;margin-right: 6%;margin-top: 10upx;" scroll-y :scroll-top="scrollTop" @scroll="scroll" :style="'height:'+height+'px'" scroll-with-animation>				
				<uni-grid @click="gridClick" show-out-border="false" :options="optinos"></uni-grid>
				
			<!-- 	<view @click="toDoctor(item)" :id="index===0?'first':''" class="nav-right-item" v-for="(item,index) in doctorList" :key="index">
					
					<image :src="item.avatar" />
					<view>{{item.realName}}</view>
				</view>
				 -->
				<page-foot :name="name" v-if="doctorList.length > 1"></page-foot>
			</scroll-view>
		</view>
	</view>
</template>

<script>
import uniIcon from '@/components/uni-icon/uni-icon.vue'
	import http from '../../store/http.js'
	import uniGrid from '../../components/uni-grid/uni-grid.vue'
	export default {
			
		components: {
			uniIcon,
			uniGrid
		},
		data() {
			return {
				optinos:[{image: "http://www.tjmugh.com.cn/pic/003/000/086/00300008606_eea3f3bf.jpg",
text: "畅晨"}],
				searchinput:'',
				doctorList: [],
				height: 0,
				categoryActive: 0,
				scrollTop: 0,
				scrollHeight: 0,
				name: "七月_"
			}
		},
		methods: {
		gridClick(ev){
				this.toDoctor(this.doctorList[ev.index])
			},
			toDoctor(item){
				 uni.navigateTo({
					url: 'doctordetail?doctor='+JSON.stringify(item)
				})
		},
		async searchdept(){
				
			},
			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},
			categoryClickMain(categroy, index) {
				
			},
			async getDoctors(id) {
				let opts={
                 	url: '/user/getDocByDept/'+id,
                    method: 'GET'
	             };
	             let param={};
				
		    await http.httpTokenRequest(opts, param).then(res => {
				this.doctorList=res.data.data
				this.optinos=[]
				for(var i=0;i<this.doctorList.length;i++){
					this.optinos.push({image:this.doctorList[i].avatar,text:this.doctorList[i].realName,index:i})
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
		 onLoad: function (options) {
			this.getDoctors(options.id)
			this.height = uni.getSystemInfoSync().windowHeight;
		}
	}
</script>

<style>
	.page-body {
		display: flex;
	}

	.nav {
		display: flex;
		width: 100%;
	}

	.nav-left {
		width: 30%;
	}
	
	.nav-left-item {
		height: 100upx;
		border-right: solid 1px #E0E0E0;
		border-bottom: solid 1px #E0E0E0;
		font-size: 30upx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.nav-right {
		width: 70%;
	}

	.nav-right-item {
		width: 28%;
		height: 220upx;
		float: left;
		text-align: center;
		padding: 11upx;
		font-size: 28upx;
	}

	.nav-right-item image {
		width: 170upx;
		height: 200upx;
		margin-left: 10upx;
	}

	.active {
		color: #007AFF;
	}
	.input-view {
		width: 92%;
		display: flex;
		background-color: #e7e7e7;
		height: 30px;
		border-radius: 15px;
		padding: 0 4%;
		flex-wrap: nowrap;
		margin: 7px 0;
		line-height: 30px;
	}
	
	.input-view .uni-icon {
		line-height: 30px !important;
	}
	
	.input-view .input {
		height: 30px;
		line-height: 30px;
		width: 94%;
		padding: 0 3%;
	}
	
	
</style>
