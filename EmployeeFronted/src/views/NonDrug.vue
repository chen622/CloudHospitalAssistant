<template>
    <a-card>
    <a-upload name="file" :multiple="true" action="http://localhost:8078/non_drug/excelIn" :headers="headers" @change="handleChange" :beforeUpload="beforeUpload"  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    >
        <a-button>
            <a-icon type="upload" /> Click to Upload
        </a-button>
    </a-upload>
        <br/>
    <a href="http://localhost:8078/non_drug/excelOut" download="">下载</a>
    </a-card>
</template>

<script>
    export default {
        data () {
            return {
                headers: {
                    authorization: sessionStorage.getItem("token"),
                }
            }
        },
        methods: {
            handleChange(info) {
                if (info.file.status !== 'uploading') {
                    console.log(info.file, info.fileList);
                }
                if (info.file.status === 'done') {
                    this.$message.success(`${info.file.name} file uploaded successfully`);
                } else if (info.file.status === 'error') {
                    this.$message.error(`${info.file.name} file upload failed.`);
                }
            },
            beforeUpload(file){
                let that = this
                return new Promise((resolve,reject) =>{
                    const is2M = file.size / 1024 / 1024 < 2
                    if (!is2M){
                        that.$message.error("上传文件大小不能超过2M")
                        return reject(false)
                    }
                    else {
                        return resolve(true)
                    }
                })
            },
        },
    }
</script>

<style>

</style>