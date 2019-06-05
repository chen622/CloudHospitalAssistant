<template>
    <a-row type="flex" align="middle" justify="center">
        <a-col span="20">
            <a-card hoverable class="info-card" title="医院用户管理" :headStyle="{fontSize: '30px'}">
            </a-card>
        </a-col>

    </a-row>
</template>

<script>

    export default {
        name: "DepartmentEdit",
        data: () => ({
            customStyle: 'background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden'
        }),
        methods: {}
    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>