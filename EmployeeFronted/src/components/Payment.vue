<template>
    <div>
        <a-table
                :rowSelection="isDoctor?null:{hideDefaultSelections: true,columnTitle: '选择',selectedRowKeys: selectedRowKeys,onSelect: onSelect}"
                :columns="columns"
                :pagination="false"
                :dataSource="payments" rowKey="id">
            <template slot="name" slot-scope="text,record">
                <span v-if="record.paymentType.type===1">{{record.application.nonDrug.name}}</span>
                <span v-else-if="record.paymentType.type===2">{{record.prescription.drug.name}}</span>
                <span v-else-if="record.paymentType.type===3">{{record.user.realName}} 医生</span>
                <span v-else>{{record.paymentType.name}}</span>
            </template>
            <template slot="time" slot-scope="text">
                {{new Date(text).toLocaleString()}}
            </template>
            <template slot="action" slot-scope="text,record">
                <div v-if="record.paymentType.type!==3">
                    <!--形成订单-->
                    <div v-if="record.state===1201" class="action">
                        <a @click="selectedRowKeys = [record.id];showPay=true">缴费</a>
                    </div>
                    <!--订单已缴费-->
                    <div v-else-if="record.state===1202" class="action">
                        <a-popconfirm title='确定重打吗?' @confirm="invoiceId = record.invoiceId;showInvoice=true">
                            <a>重打发票</a>
                        </a-popconfirm>
                        <a-popconfirm title='确定退费吗?' @confirm="showRetreat=true;retreatPayment =record">
                            <a>退费</a>
                        </a-popconfirm>
                    </div>
                    <div v-else-if="record.state===1204" class="action">
                        <a-popconfirm title='确定退费吗?' @confirm="retreatWithTake(record)">
                            <a>退费</a>
                        </a-popconfirm>
                    </div>
                    <div v-else-if="record.state===1205">
                        <a-popconfirm title='确定重打吗?' @confirm="invoiceId = record.invoiceId;showInvoice=true">
                            <a>重打发票</a>
                        </a-popconfirm>
                    </div>
                    <div v-else-if="record.state===1207">
                        <a-popconfirm title='确定重打吗?' @confirm="invoiceId = record.invoiceId;showInvoice=true">
                            <a>重打发票</a>
                        </a-popconfirm>
                    </div>
                </div>
                <div v-else>
                    <!--订单已缴费-->
                    <div v-if="record.state===1202" class="action">
                        <a-popconfirm title='确定重打吗?' @confirm="invoiceId = record.invoiceId;showInvoice=true">
                            <a>重打发票</a>
                        </a-popconfirm>
                        <!--                        <a-popconfirm title='确定退费吗?' @confirm="showRetreat=true;retreatPayment =record">-->
                        <!--                            <a>退费</a>-->
                        <!--                        </a-popconfirm>-->
                    </div>
                    <!--                    <div v-else-if="record.state===1204" class="action">-->
                    <!--                        <a>退费</a>-->
                    <!--                    </div>-->
                    <div v-else-if="record.state===1205">
                        <a-popconfirm title='确定重打吗?' @confirm="invoiceId = record.invoiceId;showInvoice=true">
                            <a>重打发票</a>
                        </a-popconfirm>
                    </div>
                </div>
            </template>
        </a-table>
        <a-modal :confirmLoading="loading" v-if="showPay" :visible="showPay" title="缴费" @ok="pay"
                 @cancel="showPay =false">
            <a-form layout="inline" style="text-align: center">
                <a-form-item label="缴费类型">
                    <a-select v-model="settlementType">
                        <a-select-option v-for="settlement in settlementTypes" :key="settlement.id">{{settlement.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-modal>
        <a-modal :confirmLoading="loading" v-if="showRetreat" :visible="showRetreat" title="退费" @ok="retreatWithoutTake"
                 @cancel="showRetreat =false">
            <a-form layout="inline" style="text-align: center">
                <a-form-item label="数量">
                    <a-input-number v-model="retreatQuantity" :min="1" :max="retreatPayment.quantity">
                    </a-input-number>
                </a-form-item>
            </a-form>
        </a-modal>

        <a-modal
                v-if="showInvoice"
                v-model="showInvoice"
                :closable="false"
                :maskClosable="false"
                :bodyStyle="{padding: '5px'}"
                width="80%"
                @ok="$print($refs.print)">
            <invoice ref="print" :invoiceId="invoiceId"></invoice>
        </a-modal>
    </div>
</template>

<script>

    import Invoice from '../components/InvoiceTemplate'

    export default {
        name: "Payment",
        components: {
            invoice: Invoice
        },
        props: ['patientId', 'isDoctor'],
        data: () => ({
            loading: false,
            columns: [
                {
                    title: '名称',
                    align: 'center',
                    scopedSlots: {customRender: 'name'},
                },
                {
                    title: '类别',
                    dataIndex: 'paymentType.name',
                    align: 'center'
                }, {
                    title: '单价',
                    dataIndex: 'unitPrice',
                    align: 'center'
                }, {
                    title: '数量',
                    dataIndex: 'quantity',
                    align: 'center'
                }, {
                    title: '创建时间',
                    dataIndex: 'createTime',
                    align: 'center',
                    scopedSlots: {customRender: 'time'},
                }, {
                    title: '状态',
                    dataIndex: 'stateVariable.name',
                    align: 'center'
                }, {
                    title: '操作',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                }
            ],
            selectedRowKeys: [],
            settlementTypes: [],
            settlementType: null,
            type: null,
            showPay: false,
            showInvoice: false,
            invoiceId: null,
            showRetreat: false,
            retreatPayment: null,
            retreatQuantity: 1
        }),
        methods: {
            retreatWithTake (record) {
                let that = this
                this.loading = true
                this.$api.post("/payment/retreatDrugFee/" + record.id, null,
                    res => {
                        that.loading = false
                        if (res.code === '100') {
                            that.invoiceId = res.data.id
                            that.showInvoice = true
                            that.$message.success("退费成功")
                            that.selectedRowKeys = []
                            that.$emit("reload")
                        } else {
                            that.$message.error(res.msg)
                        }
                    },
                    () => {
                        that.loading = false
                        that.$message.error("网络错误")
                    }
                )
            },
            retreatWithoutTake () {
                let that = this
                this.loading = true
                this.$api.post("/payment/produceRetreatPayment",
                    {paymentId: this.retreatPayment.id, quantity: this.retreatQuantity},
                    res => {
                        that.loading = false
                        if (res.code === '100') {
                            that.invoiceId = res.data.id
                            that.showRetreat = false
                            that.showInvoice = true
                            that.$message.success("退费成功")
                            that.selectedRowKeys = []
                            that.$emit("reload")
                        } else {
                            that.$message.error(res.msg)
                        }
                    },
                    () => {
                        that.loading = false
                        that.$message.error("网络错误")
                    }
                )
            },
            onSelect (select, selected) {
                if (selected) {
                    if (select.paymentType.type === 3) {
                        this.$message.error("不可选择挂号单")
                    } else {
                        if (select.state !== 1201) {
                            this.$message.error("仅缴费选项支持多选")
                        } else {
                            this.selectedRowKeys.push(select.id)
                        }
                        // if (this.selectedRowKeys.length === 0) {
                        //     this.type = select.state
                        //     this.selectedRowKeys.push(select.id)
                        // } else {
                        //     if (this.type === select.state) {
                        //         this.selectedRowKeys.push(select.id)
                        //     } else {
                        //         this.$message.error("请选择同一状态的缴费单")
                        //     }
                        // }
                    }
                } else {
                    if (this.selectedRowKeys.length === 1) {
                        this.selectedRowKeys = []
                    } else {
                        this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(select), 1)
                    }
                }
            },
            pay () {
                if (this.settlementType === null) {
                    this.$message.error("请选择缴费类型")
                    return
                }
                let that = this
                this.loading = true
                this.$api.post('/payment/pay', {
                    paymentIdList: this.selectedRowKeys,
                    settlementType: this.settlementType
                }, res => {
                    that.loading = false
                    if (res.code === '100') {
                        if (res.data.failId.length === 0) {
                            that.$message.success("已选订单全部缴费成功")
                        } else {
                            that.$message.info(res.data.failId.length + " 单缴费失败")
                        }
                        if (res.data.successId.length > 0) {
                            that.invoiceId = res.data.invoice.id
                            that.showPay = false
                            that.showInvoice = true
                        }

                    }
                    that.selectedRowKeys = []
                    that.$emit("reload")
                }, () => {
                    that.loading = false
                    that.$message.error("网络异常")
                })
            },
            getSettlementType () {
                let that = this
                this.$api.get("/constant_variable/getSettlementType", null,
                    res => {
                        if (res.code === "100") {
                            that.settlementTypes = res.data
                            that.settlementType = that.settlementTypes[0].id
                        } else
                            that.$message.error(res.msg)
                    }, () => {
                        that.$message.error("网络异常")
                    })
            },
        },
        mounted () {
            this.getSettlementType()
            if (this.isDoctor) {
                this.columns = [
                    {
                        title: '名称',
                        align: 'center',
                        scopedSlots: {customRender: 'name'},
                    },
                    {
                        title: '类别',
                        dataIndex: 'paymentType.name',
                        align: 'center'
                    }, {
                        title: '单价',
                        dataIndex: 'unitPrice',
                        align: 'center'
                    }, {
                        title: '数量',
                        dataIndex: 'quantity',
                        align: 'center'
                    }, {
                        title: '状态',
                        dataIndex: 'stateVariable.name',
                        align: 'center'
                    }
                ]
            }
        },
        watch: {
            selectedRowKeys: function (newVal) {
                if (newVal.length > 0) {
                    this.$emit("payButton", true)
                } else {
                    this.$emit("payButton", false)
                }
            }
        },
        computed: {
            payments () {
                return this.$store.state.payments
            },

        }
    }
</script>

<style scoped>

    .action a {
        margin-right: 10px;
    }
</style>