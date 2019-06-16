<template>
    <a-card>
        <a-calendar @change="selectDate"/>
    </a-card>
</template>

<script>
    export default {
        name: "Schedule",
        methods: {
            selectDate (date) {
                this.$api.get("/job_schedule/getScheduleWithMonth/2/" + date, null,
                    res => {
                    }, () => {
                    })
            }
        }
    }
</script>

<style scoped>

</style>