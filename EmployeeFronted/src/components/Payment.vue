<template>
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
            <!--形成订单-->
            <div v-if="record.state===1201" class="action">
                <a>缴费</a>
            </div>
            <!--订单缴费-->
            <div v-else-if="record.state===1202" class="action">
                <a>退费</a>
            </div>
        </template>
    </a-table>
</template>

<script>
    export default {
        name: "Payment",
        props: ['patientId', 'isDoctor'],
        data: () => ({
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
            type: null
        }),
        methods: {
            onSelect (select, selected) {
                if (selected) {
                    if (this.selectedRowKeys.length === 0) {
                        this.type = select.state
                        this.selectedRowKeys.push(select.id)
                    } else {
                        if (this.type === select.state) {
                            this.selectedRowKeys.push(select.id)
                        } else {
                            this.$message.error("请选择同一状态的缴费单")
                        }
                    }
                } else {
                    if (this.selectedRowKeys.length === 1) {
                        this.selectedRowKeys = []
                    } else {
                        this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(select), 1)
                    }
                }
            },
        },
        mounted () {
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