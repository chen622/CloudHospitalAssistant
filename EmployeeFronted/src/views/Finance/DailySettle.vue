<template>
    <a-row type="flex" align="top" justify="space-around" class="info-card">
        <a-col span="23">
            <a-col :span="showList?6:3" :xl="showList?6:2" :style="showList?'':'text-align: center'">
                <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}" >
                    <span slot="title" style="font-size: 22px">收费员列表</span>
                    <a-collapse defaultActiveKey="1" :bordered="false">
                        <a-collapse-panel header="收费员查询" key="1">
                            <a-row type="flex" align="top" justify="start"
                                   style="margin: 5px 0 10px 0;">
                                <a-input-search
                                        placeholder="收费员编号"
                                        @search="selectTollKeeper"
                                        enterButton></a-input-search>
                            </a-row>
                        </a-collapse-panel>

                        <a-collapse-panel header="全部收费员" key="2">
                            <a-list :loading="load.tollKeeper" itemLayout="horizontal"
                                    :dataSource="tollKeeper"
                                    style="overflow: auto;max-height: 400px">
                                <a-list-item slot="renderItem" slot-scope="item"
                                             @click="selectTollKeeper(item.id)">
                                    <a-list-item-meta>
                                                <span slot="description" style="font-size: 16px;line-height: 20px;">
                                                    <span>{{item.id}}</span>
                                                    <span style="padding-left: 40%">{{item.realName}}</span>
                                                </span>
                                    </a-list-item-meta>
                                </a-list-item>
                            </a-list>
                        </a-collapse-panel>
                    </a-collapse>
                </a-card>
            </a-col>

            <a-col :span="showList?17:21" :xl="showList?17:22">
                <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}" style="margin-left: 5%">
                    <span slot="title" style="font-size: 22px">日结表信息</span>
                    <a-card :loading="load.record" :body-style="{minHeight: '10px'}" :bordered='false' style="text-align: center">
                        <span v-if="settleTableList.length == 0">无未核对日结单</span>
                        <a-collapse :bordered="false" style="margin: 0 3% 0 3%; font-size: 16px"
                                    v-for="(i,index) in settleTableList" :key="i.id">
                            <a-collapse-panel :key="index">
                                <template slot="header">{{i.dailySettle.endDate | formatDate}}<span> 日结单</span>
                                    <a-button style="margin-left: 60%;" @click="check(i.dailySettle.id)">核对通过</a-button>
                                </template>
                                <div class="invoice-box">
                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0; font-size: 16px;line-height: 20px;">
                                        <a-col span="5">统计日期</a-col>
                                        <a-col span="16">
                                            <a-tag class="tag-date">{{i.dailySettle.startDate | formatTime}}</a-tag>
                                            &#12288;&#12288;至&#12288;&#12288;
                                            <a-tag class="tag-date">{{i.dailySettle.endDate | formatTime}}</a-tag>
                                        </a-col>
                                    </a-row>

                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0;">
                                        <a-col span="7">制表人：{{i.dailySettle.makeUser.realName}}</a-col>
                                        <a-col span="7">收费员：{{currentTollKeeper.realName}}</a-col>
                                        <a-col span="10">制表时间&#12288;<a-tag class="tag-date">{{i.dailySettle.makeDate |
                                            formatTime}}
                                        </a-tag>
                                        </a-col>
                                    </a-row>

                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0;">
                                        <a-col span="3">所有发票号</a-col>
                                        <a-col span="16">
                                            <a-tag color="blue" v-for="id in i.normalInvoiceId" :key="id">{{id}}</a-tag>
                                        </a-col>
                                        <a-col span="4">共计
                                            <a-tag color="blue">{{i.dailySettle.normalInvoiceAmount}}</a-tag>
                                            张
                                        </a-col>
                                    </a-row>

                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0;">
                                        <a-col span="3">重打发票号</a-col>
                                        <a-col span="16">
                                            <a-tag color="blue" v-for="id in i.anewInvoiceId" :key="id">{{id}}</a-tag>
                                        </a-col>
                                        <a-col span="4">共计
                                            <a-tag color="blue">{{i.dailySettle.anewInvoiceAmount}}</a-tag>
                                            张
                                        </a-col>
                                    </a-row>

                                    <table>
                                        <tr class="heading">
                                            <td>缴费类型</td>
                                            <td>总额</td>
                                        </tr>

                                        <tr class="details">
                                            <td>检验费</td>
                                            <td>{{i.dailySettle.inspectionFee}}</td>
                                        </tr>

                                        <tr class="details">
                                            <td>药费</td>
                                            <td>{{i.dailySettle.drugFee}}</td>
                                        </tr>

                                        <tr class="details">
                                            <td>挂号费</td>
                                            <td>{{i.dailySettle.registrationFee}}</td>
                                        </tr>

                                        <tr class="details">
                                            <td>其他费用</td>
                                            <td>{{i.dailySettle.otherFee}}</td>
                                        </tr>

                                        <tr class="total">
                                            <td></td>
                                            <td>
                                                合计： {{i.dailySettle.totalFee}}
                                            </td>
                                        </tr>
                                    </table>

                                </div>
                            </a-collapse-panel>
                        </a-collapse>
                    </a-card>
                </a-card>
            </a-col>

        </a-col>
    </a-row>
</template>


<script>
    export default {
        data: () => ({
            load: {
                tollKeeper: true,
                record: false
            },
            tollKeeper: [],
            currentTollKeeper: null,
            showList: true,
            settleTableList: [],
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
            },
            formatDate: function (value) {
                let date = new Date(value);
                let y = date.getFullYear();
                let MM = date.getMonth() + 1;
                MM = MM < 10 ? ('0' + MM) : MM;
                let d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                return y + '-' + MM + '-' + d;
            }
        },
        methods: {
            getTollKeeper() {
                let that = this
                this.$api.get("/user/getAllTollKeeper", null, res => {
                    if (res.code === '100') {
                        that.tollKeeper = res.data
                        that.load.tollKeeper = false
                    }
                }, res => {
                })
            },

            selectTollKeeper(id) {
                this.load.record = true
                let that = this
                this.$api.get("/daily_settle/getSettleInfo/" + id, null, res => {
                    if (res.code === '100') {
                        that.settleTableList = res.data.settleList
                        that.currentTollKeeper = res.data.admin
                    } else if (res.code === '502'){
                        that.$message.error(res.message)
                    }
                    that.load.record = false
                }, () => {
                    that.load.record = false
                })
            },

            check(settleId) {
                let that = this
                this.$api.post("/daily_settle/check/" + settleId, null, res => {
                    if (res.code === '100') {
                        that.selectTollKeeper(that.currentTollKeeper.id)
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                }, () => {
                })
            }
        },
        mounted() {
            this.getTollKeeper()
        }

    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .invoice-box {
        max-width: 800px;
        margin: auto;
        padding: 20px;
        border: 1px solid #eee;
        box-shadow: 0 0 10px rgba(0, 0, 0, .15);
        font-size: 16px;
        line-height: 24px;
        font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
        color: #555;
    }

    .invoice-box table {
        width: 95%;
        line-height: inherit;
        text-align: left;
    }

    .invoice-box table td {
        padding: 5px;
        vertical-align: top;
    }

    .invoice-box table tr td:nth-child(2) {
        text-align: right;
    }

    .invoice-box table tr.heading {
        background: #eee;
        border-bottom: 1px solid #ddd;
        font-weight: bold;
    }

    .invoice-box table tr.details {
        padding-bottom: 20px;
    }

    .details td:nth-child(2) {
        text-align: right;
    }

    .invoice-box table tr.total td:nth-child(2) {
        border-top: 2px solid #eee;
        font-weight: bold;
    }

    .tag-date {
        font-size: 16px;
    }


</style>