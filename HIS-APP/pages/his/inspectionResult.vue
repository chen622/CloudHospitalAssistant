<template>
			
	<view class="page" >
		<view class="uni-list" style="width: 99%;margin-top: 10upx;margin-left: 0.5%;"  >
			<uni-card 
			 is-full="true"
			 :title="result.name" 
			 thumbnail="http://img-cdn-qiniu.dcloud.net.cn/new-page/uni.png" 
			 >
				<view class="uni-flex uni-column" style="width: 100%;margin-top: 10upx;" >
					<text class="example-title">{{result.text}}</text>
					<view >
						<image :src="result.img"></image>
					</view>
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
		},onLoad(options){
			this.result=JSON.parse(options.r)
		},
		data() {
			return {
				result:{}
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
