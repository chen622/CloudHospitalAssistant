<template>
    <a-table :columns="columns" :dataSource="payments" rowKey="id">
        <template slot="name" slot-scope="text,record">
            {{record.application !== undefined?record.application.nonDrug.name:record.prescription.drug.name}}
        </template>
        <template slot="action" slot-scope="text,record">
            <div v-if="record.state ===1201">

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
                    title: '状态',
                    dataIndex: 'stateVariable.name',
                    align: 'center',
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
                        align: 'center',
                    }
                ]
            }
            console.log(this.datetimeToTimeStamp('2018-9-12 9:11:23'))
            console.log(this.dateToTimeStamp('2018-9-12'))
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