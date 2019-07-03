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
                <span v-else-if="record.paymentType.type===3">挂号医生：{{record.user.realName}}</span>
                <span v-else>{{record.paymentType.name}}</span>
            </template>
            <template slot="time" slot-scope="text">
                {{new Date(text).toLocaleString()}}
            </template>
            <template slot="action" slot-scope="text,record">

                <span v-if="record.paymentType.type!==3&&record.state===1201">
                                        <a-divider type="vertical"></a-divider>
                    <a @click="selectedRowKeys = [record.id];showPay=true">缴费</a>
                </span>

                <span v-if="showAgain(record)">
                    <a-divider type="vertical"></a-divider>
                    <a-popconfirm title='确定补打吗?' @confirm="againInvoice(record.invoiceId)">
                        <a>补打发票</a>
                    </a-popconfirm>
                </span>

                <span v-if="showAgain(record)">
                    <a-divider type="vertical"></a-divider>

                    <a-popconfirm title='确定重打吗?' @confirm="anewInvoice(record.invoiceId)">
                        <a>重打发票</a>
                    </a-popconfirm>
                </span>

                <span v-if="showRetreatWithout(record)">
                    <a-divider type="vertical"></a-divider>
                    <a-popconfirm title='确定退费吗?' @confirm="retreatWithoutTake(record)">
                        <a>退费</a>
                    </a-popconfirm>
                </span>
                <span v-if="showRetreatWith(record)">
                    <a-divider type="vertical"></a-divider>
                    <a-popconfirm title='确定退费吗?' @confirm="retreatWithTake(record)">
                        <a>退费</a>
                    </a-popconfirm>
                </span>
                <template v-if="record.paymentType === 3">
                    <!--订单已缴费-->
                    <span v-if="record.state===1202&& record.registration.state===802">
                        <a-popconfirm title='确定退号吗?' @confirm="retreatRegistration(record.itemId)">
                            <a style="color: red;">退号</a>
                        </a-popconfirm>
                    </span>
                    <span v-if="record.state===1202&& record.registration.state===801">
                        <a-popconfirm title='确定患者已到吗?' @confirm="arrive(record)">
                            <a style="color: red;">已到</a>
                        </a-popconfirm>
                        <a-divider type="vertical"></a-divider>

                        <a-popconfirm title='确定退号吗?' @confirm="retreatRegistration(record.itemId)">
                            <a style="color: red;">退号</a>
                        </a-popconfirm>
                    </span>
                </template>
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
            retreatPayment: null,
            retreatQuantity: 1
        }),
        methods: {
            arrive (item) {
                let that = this
                this.$api.post("/registration/confirmPre/" + item.registration.id, null,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("确认成功！")
                            that.$emit("reload")

                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            anewInvoice (invoiceId) {
                this.invoiceId = invoiceId;
                let that = this
                this.$api.post("/invoice/anewInvoice/" + invoiceId, null,
                    res => {
                        if (res.code === '100') {
                            that.showInvoice = true
                            that.$message.success("重打成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            againInvoice (invoiceId) {
                this.invoiceId = invoiceId;
                let that = this
                this.$api.post("/invoice/againInvoice/" + invoiceId, null,
                    res => {
                        if (res.code === '100') {
                            that.showInvoice = true
                            that.$message.success("补打成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            retreatRegistration (id) {
                let that = this
                this.$api.post("/registration/retreat/" + id, null,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("退号成功")
                            that.$emit("reload")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
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
                    }
                )
            },
            retreatWithoutTake (record) {
                let that = this
                this.loading = true
                this.$api.post("/payment/produceRetreatPayment",
                    {paymentId: record.id},
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
                    })
            },
            showAgain (record) {
                return (record.state === 1202 || record.state === 1203 || record.state === 1205 || record.state === 1207);
            },
            showRetreatWithout (record) {
                return record.paymentType !== 3 && record.state === 1202;
            },
            showRetreatWith (record) {
                return record.paymentType !== 3 && record.state === 1204;
            }
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