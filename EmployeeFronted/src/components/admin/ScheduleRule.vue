<template>
    <div>
        <a-table bordered :columns="columns">
            <span slot="title">排班规则</span>

        </a-table>
    </div>
</template>

<script>
    export default {
        name: "ScheduleRule",
        data:()=>({
            columns: [
                {
                    title: '',
                    dataIndex: ''
                }
            ]
        })
    }
</script>

<style scoped>

</style>