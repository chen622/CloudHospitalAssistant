<template>
    <a-table :columns="columns" :dataSource="payments" rowKey="id">
        <template slot="name" slot-scope="text,record">
            <span v-if="record.paymentType.type===1">{{record.application.nonDrug.name}}</span>
            <span v-else-if="record.paymentType.type===2">{{record.prescription.drug.name}}</span>
            <span v-else-if="record.paymentType.type===3">{{record.user.realName}} 医生</span>
            <span v-else>{{record.paymentType.name}}</span>
        </template>
        <template slot="action" slot-scope="text,record">

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
                    title: '状态',
                    dataIndex: 'stateVariable.name',
                    align: 'center'
                }, {
                    title: '操作',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                }
            ],
        }),
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
            }
        }
    }
</script>

<style scoped>

</style>