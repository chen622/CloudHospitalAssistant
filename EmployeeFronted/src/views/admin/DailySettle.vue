<template>
        <a-row type="flex" align="top" justify="space-around" class="info-card">
            <a-col span="23">
                    <a-col :span="showList?6:3" :xl="showList?6:2" :style="showList?'':'text-align: center'">
                        <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}">
                                    <span slot="title" style="font-size: 22px">收费员列表
                                        <!--                                        <a-button @click="getPatient" type="primary" shape="circle" icon="reload"-->
                                        <!--                                            style="float: right;"></a-button>-->
                                    </span>
                            <a-collapse defaultActiveKey="1" :bordered="false">
                                <a-collapse-panel header="收费员查询" key="1">
                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 5px 0 10px 0;">
                                        <a-input-search
                                                placeholder="收费员编号"
                                                @search="onSearch"
                                                enterButton
                                        />
                                    </a-row>
                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0;">
                                        <a-date-picker
                                                :mode="mode1"
                                                showTime
                                                format="YYYY-MM-DD hh:mm:ss"
                                                @openChange="handleOpenChange1"
                                                @panelChange="handlePanelChange1"
                                                style="width: 80%"
                                        />
                                    </a-row>
                                </a-collapse-panel>

                                <a-collapse-panel header="全部收费员" key="2">
                                    <a-list :loading="load.tollKeeper" itemLayout="horizontal"
                                            :dataSource="tollKeeper"
                                            style="overflow: auto;max-height: 400px">
                                        <a-list-item slot="renderItem" slot-scope="item"
                                                     @click="selectTollKeeper(item)">
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
                            <a-collapse :bordered="false" style="margin: 0 3% 1% 3%; font-size: 16px" v-for="(i,index) in settleTableList" :key="i.id">
                                <a-collapse-panel :header="i.dailySettle.endDate | formatDate" :key="index">
                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0; font-size: 16px;line-height: 20px;">
                                        <a-col span="5">统计日期</a-col>
                                        <a-col span="16">{{i.dailySettle.startDate | formatDate}}&#12288;&#12288;至&#12288;&#12288;{{i.dailySettle.endDate | formatDate}}</a-col>
                                        <a-col span="3">
                                            <a-button type="primary">核对通过</a-button>
                                        </a-col>
                                    </a-row>

                                    <a-row type="flex" align="top" justify="start"
                                           style="margin: 15px 0 10px 0;">
                                        <a-col span="6">制表人：{{i.dailySettle.makeUser.realName}}</a-col>
                                        <a-col span="6">收费员：{{i.dailySettle.adminUser.realName}}</a-col>
                                        <a-col span="10">制表时间：{{i.dailySettle.makeDate | formatDate}}</a-col>
                                    </a-row>


                                </a-collapse-panel>
                            </a-collapse>
                        </a-card>
                    </a-col>

            </a-col>
        </a-row>
</template>


<script>


    import ACol from "ant-design-vue/es/grid/Col";
    import ARow from "ant-design-vue/es/grid/Row";
    export default {
        components: {ARow, ACol},
        data: () => ({
            load: {
                tollKeeper: true
            },
            tollKeeper:[],
            currentTollKeeper: null,
            showList: true,
            settleTableList: [{
                dailySettle: {
                    adminId: null,
                    anewInvoiceAmount: null,
                    checkId: null,
                    drugFee: null,
                    endDate: null,
                    id: null,
                    inspectionFee: null,
                    makeDate: null,
                    makeId: null,
                    normalInvoiceAmount: null,
                    otherFee: null,
                    registrationFee: null,
                    startDate: null,
                    totalFee: null,
                    makeUser: {
                        realName: null
                    },
                    adminUser: {
                        realName: null
                    }
                },
                normalInvoiceId: [],
                anewInvoiceId: []
            }]
        }),
        filters: {
            formatDate: function (value) {
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
            getTollKeeper() {
                let that = this
                this.$api.get("/user/getAllTollKeeper", null, res => {
                    if (res.code === '100') {
                        that.tollKeeper = res.data
                        that.load.tollKeeper = false
                    }
                }, res => {
                    that.$message.error(res)
                })
            },

            selectTollKeeper(tollKeeper) {
                let that = this
                this.$api.get("/daily_settle/getSettleInfo/" + tollKeeper.id, null, res => {
                    if (res.code === '100') {
                        that.currentTollKeeper = tollKeeper
                        that.settleTableList = res.data
                    }
                }, () => {
                    that.$message.error("网络异常")
                })
            },
        },
        mounted () {
            this.getTollKeeper()
        }

    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

</style>