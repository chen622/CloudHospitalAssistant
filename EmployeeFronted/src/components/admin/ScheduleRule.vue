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
        props: ['department'],
        data: () => ({
            columns: [
                {
                    title: '',
                    dataIndex: ''
                }
            ]
        }),
        methods: {
            getRule () {
                this.$api.get("/schedule_rule/getByDepartmentId/" + this.department[2], null,
                    res => {
                        alert(res)
                    }, () => {
                    })
            }
        },
    }
</script>

<style scoped>

</style>