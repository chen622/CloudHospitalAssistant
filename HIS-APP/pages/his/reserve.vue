<template>
	<view class="container">
		<view class="input-view">
			<uni-icon type="search" size="22" color="#666666" />
			<input confirm-type="search" v-model="searchinput" class="input"  type="text" placeholder="搜索科室" @confirm="searchdept()" />
		</view>
		<view v-if="isSearch">
			<scroll-view style="width: 100%" scroll-y :scroll-top="scrollTop" @scroll="scroll" :style="'height:'+height+'px'" scroll-with-animation>
				
				<view :id="index===0?'first':''" class="nav-right-item" v-for="(item,index) in searchResult" :key="index">
					<navigator :url="'doctors?id='+item.id">
					<image src='../../static/img/dept.png' />
					<view>{{item.name}}</view>
					</navigator>
				</view>
				
				<page-foot :name="name" v-if="subCategoryList.length > 1"></page-foot>
			</scroll-view>
		</view>
		
		<view class="page-body" v-if="!isSearch">
			<scroll-view class="nav-left" scroll-y :style="'height:'+height+'px'">
				<view class="nav-left-item" @click="categoryClickMain(item,index)" :key="index" :class="index==categoryActive?'active':''"
				    v-for="(item,index) in categoryList">
					{{item.kindName}}
				</view>
			</scroll-view>
			<scroll-view class="nav-right" scroll-y :scroll-top="scrollTop" @scroll="scroll" :style="'height:'+height+'px'" scroll-with-animation>
				
				<view :id="index===0?'first':''" class="nav-right-item" v-for="(item,index) in subCategoryList" :key="index">
					<navigator :url="'doctors?id='+item.id">
					<image src='../../static/img/dept.png' />
					<view>{{item.name}}</view>
					</navigator>
				</view>
				
				<page-foot :name="name" v-if="subCategoryList.length > 1"></page-foot>
			</scroll-view>
		</view>
		
	</view>
</template>

<script>
	import uniIcon from '@/components/uni-icon/uni-icon.vue'
	import http from '../../store/http.js'
	export default {
			
		components: {
			uniIcon
		},
		data() {
			return {
				searchResult:[],
				isSearch:false,
				test:'aaaaa',
				searchinput:'',
				deptList:[],
				categoryList: [],
				subCategoryList: [],
				height: 0,
				categoryActive: 0,
				scrollTop: 0,
				scrollHeight: 0,
				name: "七月_"
			}
		},
		methods: {
			async searchdept(){
				if(this.searchinput==null || this.searchinput==''){
					this.isSearch=false	
					return
				}
				for(var i=0;i<this.categoryList.length;i++){
					let d=this.categoryList[i].departments
					for(var j=0;j<d.length;j++){
						if(d[j].name.indexOf(this.searchinput)>=0){
							d[j].index=d[j].id
							this.searchResult.push(d[j])
						}
					}
				}
				this.isSearch=true
			},
			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},
			categoryClickMain(categroy, index) {
				this.categoryActive = index;
				this.subCategoryList = categroy.departments;
				this.scrollTop = -this.scrollHeight * index;
			},
			async getCategory() {
				
				
				let opts={
                 	url: '/department_kind/getClinical',
                    method: 'GET'
	             };
	             let param={};
				
			    http.httpTokenRequest(opts, param).then(res => {
					this.categoryList=res.data.data
					this.subCategoryList=this.categoryList[0].departments
	            },error => {
				uni.showToast({
					title: '网络错误，请稍后重试',
					duration: 2000,
					icon:'none'
				})
					});  
			}	
		},
		onLoad: function () {
			this.getCategory();
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
		width: 100upx;
		height: 100upx;
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
