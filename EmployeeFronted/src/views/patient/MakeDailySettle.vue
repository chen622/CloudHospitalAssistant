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
                                <a-col span="6" type="flex" align="top" justify="middle" style="font-size: 16px">
                                    <a-form layout="inline" :label-col="{ span: 2 }" :wrapper-col="{span: 2}">
                                        <a-form-item label="收费员">
                                            <a-input disabled :placeholder="currentAdmin.realName">
                                            </a-input>
                                        </a-form-item>
                                    </a-form>
                                </a-col>
                                <a-col span="5" type="flex" align="top" justify="start">
                                    <a-button type="primary" @click="statistics">日结统计</a-button>
                                </a-col>
                            </a-row>

                            <div v-if="settle!=null">
                                <a-card class="settle-card" :bordered="false" :loading="load.cardLoad">
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
                                    <a-row type="flex" align="middle" justify="end" class="search-card">
                                        <a-button @click="confirm" style="margin-right: 10%">结算报账</a-button>
                                    </a-row>
                                </a-card>
                            </div>
                        </a-tab-pane>

                        <a-tab-pane tab="日结历史查询" key="2" class="tab">
                            <a-row>
                                <a-col span="4" style="margin-right: 20px">
                                    <a-menu
                                            mode="vertical"
                                            v-for="(i, index) in allSettle" :key="i.id"
                                            @click="handleClick(i)"
                                    >
                                        <a-menu-item :key="index" >
                                            {{i.endDate | formatTime}}
                                        </a-menu-item>
                                    </a-menu>
                                </a-col>
                                <a-col span="19" style="margin-left: 10px">
                                    <a-table :columns="historyColumns" :dataSource="historyData" :loading="load.tableLoad">
                                        <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                                        <template slot="state" slot-scope="text">
                                            <a-tag color="red" style="font-size:15px"
                                                   v-if="text=='red'">红冲
                                            </a-tag>
                                            <a-tag color="blue" style="font-size:15px"
                                                   v-if="text=='valid'">有效
                                            </a-tag>
                                            <a-tag color="orange" style="font-size:15px"
                                                   v-if="text=='anew'">重打
                                            </a-tag>
                                        </template>
                                    </a-table>
                                </a-col>
                            </a-row>
                        </a-tab-pane>
                    </a-tabs>
                </a-card>
            </a-col>
        </a-row>
    </div>
</template>

<script>

    import moment from 'moment'
    import ARow from "ant-design-vue/es/grid/Row";
    import ACol from "ant-design-vue/es/grid/Col";

    export default {
        components: {ACol, ARow},
        data: () => ({
            time: {
                timeFormat: 'YYYY-MM-DD hh:mm:ss'
            },
            load: {
                cardLoad: false,
                tableLoad: false
            },
            startTime: null,
            endTime: new moment(),
            currentAdmin: null,
            settle: null,
            allSettle: [],
            currentSettle: null,
            historyData: [],
            historyColumns: [{
                title: '发票号',
                dataIndex: 'invoice.id',
                key: 'invoice.id',
                scopedSlots: {customRender: 'invoice.id'}
            },
                {
                    title: '发票张数',
                    dataIndex: 'number',
                    key: 'number',
                    scopedSlots: {customRender: 'number'}
                },
                {
                    title: '应收张数',
                    dataIndex: 'shouldNumber',
                    key: 'shouldNumber',
                    scopedSlots: {customRender: 'shouldNumber'}
                },
                {
                    title: '患者姓名',
                    dataIndex: 'invoice.payment.patient.realName',
                    key: 'invoice.payment.patient.realName',
                    scopedSlots: {customRender: 'invoice.payment.patient.realName'}
                },
                {
                    title: '发票总额',
                    dataIndex: 'invoice.priceAmount',
                    key: 'invoice.priceAmount',
                    scopedSlots: {customRender: 'invoice.priceAmount'}
                },
                {
                    title: '结算类别',
                    dataIndex: 'invoice.payment.stateVariable.name',
                    key: 'invoice.payment.stateVariable.name',
                    scopedSlots: {customRender: 'invoice.payment.stateVariable.name'}
                },
                {
                    title: '状态',
                    dataIndex: 'state',
                    key: 'state',
                    scopedSlots: {customRender: 'state'}
                },
            ]
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
                        that.currentAdmin = res.data
                    } else if (res.code === '502') {
                        that.$message.error(res.message)
                    }
                }, () => {
                    that.$message.error("网络异常")
                })
            },
            initializeChoice() {
                let that = this
                this.$api.get("/daily_settle/getAll", null, res => {
                    if (res.code === '100') {
                        that.allSettle = res.data;
                    } else if (res.code === '502') {
                        that.$message.error(res.message)
                    }
                }, () => {
                    that.$message.error("网络异常")
                })
            },
            statistics() {
                let request = {
                    admin: this.currentAdmin.id,
                    endDate: this.endTime
                }
                this.load.cardLoad = true
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
                    that.load.cardLoad = false
                }, () => {
                    that.$message.error("网络异常")
                    that.load.cardLoad = false
                })
            },
            confirm() {
                let request = {
                    dailySettle: this.settle
                }
                this.load.cardLoad = true
                let that = this
                this.$api.post("/daily_settle/makeTable", request, res => {
                    if (res.code === '100') {
                        that.settle = null
                        that.$message.success("建表成功")
                    } else if (res.code === '502') {
                        that.$message.error(res.message)
                    }
                    that.load.cardLoad = false
                }, () => {
                    that.$message.error("网络异常")
                    that.load.cardLoad = false
                })
            },
            searchHistory() {
                let that = this
                this.load.tableLoad = true
                this.$api.get("/daily_settle/dailyHistory/" + this.currentSettle.id, null, res => {
                    if (res.code === '100') {
                        that.historyData = res.data
                    } else if (res.code === '502') {
                        that.$message.error(res.message)
                    }
                    that.load.tableLoad = false
                }, () => {
                    that.$message.error("网络异常")
                    that.load.tableLoad = false
                })
            },
            handleClick(s) {
                this.currentSettle = s
                this.searchHistory()
            },
        },
        mounted() {
            this.initialize()
            this.initializeChoice()
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