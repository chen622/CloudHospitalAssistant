<template>
    <a-row type="flex" align="middle" justify="center" class="charge">
        <a-col span="20">
            <search-patient @selectPatient="selectPatient"></search-patient>
        </a-col>
        <a-col span="20">
            <a-card :body-style="{padding: 0}">
                <template slot="title">
                    <span>费用信息</span>
                </template>
                <a-form layout="inline" style="margin-left: 10px">
                    <a-form-item label="范围选择">
                        <a-range-picker v-model="timeRange" showTime format="YYYY-MM-DD HH:mm:ss"/>
                    </a-form-item>
                </a-form>
                <payment></payment>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>
    import SearchPatient from '../../components/SearchPatient'
    import Payment from '../../components/Payment'

    export default {
        data () {
            return {
                patientId: null,
                timeRange: null
            }
        },
        components: {
            payment: Payment,
            searchPatient: SearchPatient
        },
        methods: {
            selectPatient (record) {
                this.patientId = record.id

            },
            getPayment () {
                this.$api.get("/")
            }
        },
    };
</script>
<style>
    .charge {
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>
