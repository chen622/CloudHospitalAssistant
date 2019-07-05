<template>
	<view  class="page">	
			<view class="uni-list" style="width: 99%;margin-top: 10upx;margin-left: 0.5%;"  >
				<uni-card 
				 is-full="true"
				 title="初诊" 
				 thumbnail="http://img-cdn-qiniu.dcloud.net.cn/new-page/uni.png" 
				 >
					<view class="uni-flex uni-row" >
						<uni-list>
							<uni-list-item style="width: 100%;" :key="index"  v-for="(item,index) in MR.firstDiagnose" :show-arrow="false" :title="item.diseaseSecond.name"  />
						</uni-list>
					</view>
				</uni-card>		
			</view>
			
			<view class="uni-list" style="width: 99%;margin-top: 10upx;margin-left: 0.5%;"  >
				<uni-card 
				 is-full="true"
				 title="最终诊断" 
				 thumbnail="http://img-cdn-qiniu.dcloud.net.cn/new-page/uni.png" 
				 >
					<view class="uni-flex uni-row" >
						<uni-list>
							<uni-list-item style="width: 100%;" :key="index"  v-for="(item,index) in MR.finalDiagnose" :show-arrow="false" :title="item.diseaseSecond.name"  />
						</uni-list>
					</view>
				</uni-card>		
			</view>
			
			<view class="uni-list" style="width: 99%;margin-top: 10upx;margin-left: 0.5%;"  >
				<uni-card 
				 is-full="true"
				 title="检查项目" 
				 thumbnail="http://img-cdn-qiniu.dcloud.net.cn/new-page/uni.png" 
				 >
					<view class="uni-flex uni-row"  style="width: 100%;">
						<uni-list  style="width: 100%;">
							<uni-list-item @click="checkResult(item)" tyle="width: 100%;" :key="index"  v-for="(item,index) in inspections"  :title="item.nonDrugName" :note="item.note"  />
						</uni-list>
					</view>
				</uni-card>		
			</view>
			
			<view class="uni-list" style="width: 99%;margin-top: 10upx;margin-left: 0.5%;"  >
				<uni-card 
				 is-full="true"
				 title="处方" 
				 thumbnail="http://img-cdn-qiniu.dcloud.net.cn/new-page/uni.png" 
				 >
					<view class="uni-flex uni-row"  style="width: 100%;">
						<uni-list>
							<uni-list-item style="width: 100%;"  :key="index"  v-for="(item,index) in prescriptions" :show-arrow="false" :title="item.drugName" :note="item.note" />
						</uni-list>
					</view>
				</uni-card>		
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
				result:{},
				visible:false,
				test:'1111',
				extraIcon1: {
					color: '#007aff',
					size: '22',
					type: 'info-filled'
				},
				extraIcon2: {
					color: '#4cd964',
					size: '22',
					type: 'spinner'
				},
				MR:null,
				inspections:[],
				prescriptions:[],
			}
		},
		onLoad(options) {
			this.MR=JSON.parse(options.MR)
			this.getItems()
		},
		methods: {
			returnI(){
				this.visible=false
			},getItems(){
				let opts={
	                 	url: '/medical_record/getDrugNonDrugAndResult/'+this.MR.id,
	                    method: 'GET'
	             };
	             
		    	http.httpTokenRequest(opts, null).then(res => {
					let result=res.data.data
					console.log(result)
					this.prescriptions=result[1]
					this.prescriptions.forEach(item =>{
						item.note='总数量:'+item.drugQuantity+'      	      已退数量:'+item.returnNum+'        	    单价:'+item.drugPrice
					})
					this.inspections=result[0]
					this.inspections.forEach(item => {
						item.note='数量: '+item.nonDrugQuantity+'     	      单价: '+item.nonDrugPrice
					})

	            },error => {
					uni.showToast({
						title: '网络错误，请稍后重试',
						duration: 2000,
						icon:'none'
					});  
				})			
			},checkResult(item){
				this.result.text=item.text
				this.result.img=item.pic
				this.result.name=item.nonDrugName
				uni.navigateTo({
					url:'inspectionResult?r='+JSON.stringify(this.result)
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