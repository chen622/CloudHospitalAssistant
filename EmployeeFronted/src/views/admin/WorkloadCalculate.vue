<template>
    <div>
    <a-row type="flex" align="middle" justify="center" class="info-medicine">
        <a-col span="23">
            <a-card hoveracble title="工作量统计" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
            </a-card>
        </a-col>
    </a-row>
    <a-row type="flex" align="top" justify="start"
           style="margin: 15px 0 10px 0;">
        <a-date-picker
                showTime
                format="YYYY-MM-DD hh:mm:ss"
                style="width: 80%"></a-date-picker>
    </a-row>
    </div>
</template>

<script>
    export default {
        name: "WorkloadCalculate"
    }
</script>

<style scoped>

</style>