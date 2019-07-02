<template>
    <a-row type="flex" align="middle" justify="center" class="charge">
        <a-col span="20">
            <search-patient @selectPatient="selectPatient"></search-patient>
        </a-col>
        <a-col span="20" v-if="patient!=null">
            <a-card :body-style="{padding: 0}">
                <template slot="title">
                    <span>{{patient.realName}}费用信息</span>
                </template>
                <a-form layout="inline" style="margin-left: 20px">
                    <a-form-item label="范围选择">
                        <a-range-picker v-model="timeRange" showTime format="YYYY-MM-DD HH:mm:ss"/>
                    </a-form-item>
                    <a-form-item>
                        <a-button @click="search" type="primary">搜索</a-button>
                    </a-form-item>
                    <a-form-item>
                        <a-button v-if="payButton" type="danger" @click="$refs.payment.showPay=true">缴费</a-button>
                    </a-form-item>
                    <payment ref="payment" @reload="search" @payButton="type => payButton = type"></payment>
                </a-form>
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
                patient: null,
                timeRange: null,
                payButton: false,
            }
        },
        components: {
            payment: Payment,
            searchPatient: SearchPatient
        },
        methods: {
            selectTime (times) {
                times[0].utc().valueOf()
                times[1].utc().valueOf()
            },
            selectPatient (record) {
                this.patient = record
                this.search()
            },
            search () {
                let data = null
                if (this.timeRange) {
                    data = {
                        start: this.timeRange[0].utc().valueOf(),
                        end: this.timeRange[1].utc().valueOf(),
                        patientId: this.patient.id
                    }
                } else {
                    data = {
                        patientId: this.patient.id
                    }
                }
                let that = this
                this.$api.post("/payment/getAll", data,
                    res => {
                        if (res.code === '100') {
                            that.$store.commit("setPayment", res.data)
                        } else {
                            that.$message.info(res.msg)
                        }
                    }, () => {
                    })
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
