<template>
    <a-table :columns="columns" :dataSource="payments" rowKey="id">
        <template slot="name" slot-scope="text,record">
            {{record.application != undefined?record.application.nonDrug.name:record.prescription.drug.name}}
        </template>
    </a-table>
</template>

<script>
    export default {
        name: "Payment",
        props: ['patientId'],
        data: () => ({
            columns: [
                {
                    title: 'name',
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
                }
            ],
        }),
        mounted () {

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