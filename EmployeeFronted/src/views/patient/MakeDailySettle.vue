<template>
    <div>
        <a-row type="flex" align="middle" justify="center" class="info-card">
            <a-col span="20">
                <a-card hoveracble title="收费员日结" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                    <a-tabs size="large">
                        <a-tab-pane tab="日结统计" key="1">
                            <a-row type="flex" align="middle" justify="center" class="search-card">
                                <a-col span="6" type="flex" align="top" justify="start">
                                    <a-date-picker showTime
                                                   :format="time.timeFormat"
                                                   v-model="startTime"
                                                   disabled
                                                   placeholder="起始时间">
                                    </a-date-picker>
                                </a-col>
                                <a-col span="6" type="flex" align="top" justify="start">
                                    <a-date-picker showTime
                                                   :format="time.timeFormat"
                                                   v-model="endTime"
                                                   placeholder="截止时间（可选填）">
                                    </a-date-picker>
                                </a-col>
                                <a-col span="4" type="flex" align="top" justify="start">
                                    <a-select :defaultValue="allAdmin[self].realName" style="width: 120px"
                                              @change="handleAdminChange">
                                        <a-select-option v-for="(admin,index) in allAdmin" :key="index">
                                            {{admin.realName}}
                                        </a-select-option>
                                    </a-select>
                                </a-col>
                                <a-col span="5" type="flex" align="top" justify="start">
                                    <a-button type="primary" @click="statistics">日结统计</a-button>
                                </a-col>
                            </a-row>

                            <div v-if="settle!=null">
                                <a-card class="settle-card" :bordered="false">
                                    <p style="font-size: 20px">日结单信息</p>
                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 5px 0 10px 0;"></a-row>
                                    <a-row type="flex" align="middle" justify="start" class="search-card">
                                        <a-col span="6">有效发票共计
                                            <a-tag style="width: 40px;">{{settle.normalInvoiceAmount}}</a-tag>
                                            张
                                        </a-col>
                                        <a-col span="6">重打发票共计
                                            <a-tag style="width: 40px;">{{settle.anewInvoiceAmount}}</a-tag>
                                            张
                                        </a-col>
                                    </a-row>
                                    <a-row type="flex" align="middle" justify="start" class="search-card">
                                        <a-col span="6">医药费
                                            <a-tag style="width: 40px;">{{settle.drugFee}}</a-tag>
                                        </a-col>
                                        <a-col span="6">挂号费
                                            <a-tag style="width: 40px;">{{settle.registrationFee}}</a-tag>
                                        </a-col>
                                        <a-col span="6">检查费
                                            <a-tag style="width: 40px;">{{settle.inspectionFee}}</a-tag>
                                        </a-col>
                                        <a-col span="6">处置费
                                            <a-tag style="width: 40px;">{{settle.dealFee}}</a-tag>
                                        </a-col>
                                    </a-row>
                                </a-card>
                            </div>


                        </a-tab-pane>

                        <a-tab-pane tab="日结历史查询" key="2" class="tab">
                        </a-tab-pane>
                    </a-tabs>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script>

    import moment from 'moment'

    export default {
        data: () => ({
            time: {
                timeFormat: 'YYYY-MM-DD hh:mm:ss'
            },
            startTime: null,
            endTime: new moment(),
            allAdmin: [],
            currentAdmin: null,
            self: null,
            settle: null,
        }),
        filters: {
            formatTime: function (value) {
                let date = new Date(value);
                let y = date.getFullYear();
                let MM = date.getMonth() + 1;
                MM = MM < 10 ? ('0' + MM) : MM;
                let d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                let h = date.getHours();
                h = h < 10 ? ('0' + h) : h;
                let m = date.getMinutes();
                m = m < 10 ? ('0' + m) : m;
                let s = date.getSeconds();
                s = s < 10 ? ('0' + s) : s;
                return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
            }
        },
        methods: {
            initialize() {
                let that = this
                this.$api.get("/daily_settle/initialize", null, res => {
                    if (res.code === '100') {
                        that.allAdmin = res.data.userList
                        that.self = res.data.self
                        that.currentAdmin = that.allAdmin[that.self]
                    } else if (res.code === '502') {
                        that.$message.error(res.message)
                    }
                }, () => {
                    that.$message.error("网络异常")
                })
            },
            handleAdminChange(value) {
                this.currentAdmin = this.allAdmin[value]
            },
            statistics() {
                let request = {
                    admin: this.currentAdmin.id,
                    endDate: this.endTime
                }
                let that = this
                this.$api.post("/daily_settle/produceSettleInfo", request, res => {
                    if (res.code === '100') {
                        that.settle = res.data.dailySettle
                        that.startTime = new moment(that.settle.startDate)
                    } else if (res.code === '502') {
                        that.$message.error(res.message)
                    } else if (res.code === '513') {
                        that.$message.error(res.message)
                    }
                }, () => {
                    that.$message.error("网络异常")
                })
            }
        },
        mounted() {
            this.initialize()
        }
    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .search-card {
        padding: 10px 0 10px 10px;
        text-align: center;
    }

    .settle-card {
        margin: 1% 0 0 0;
    }
</style>