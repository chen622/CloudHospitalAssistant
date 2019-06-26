<template name="page-foot">
	<view class="page-share-title">
		
	</view>
</template>

<script>
	export default {
		name: "page-foot",
		props: {
			name: {
				type: String,
				default: ""
			}
		},
		methods: {
			submit() {
				uni.showModal({
					content:"hello uni-app开源地址为https://github.com/dcloudio/uni-app/tree/master/examples，请在这个开源项目上贡献你的代码",
					showCancel:false
				})
			}
		}
	}
</script>
<style>
	.page-share-title {
		text-align: center;
		font-size: 30upx;
		color: #BEBEBE;
		padding: 20upx 0;
	}

	.submit {
		border-bottom: 1upx solid #BEBEBE;
	}
</style>
