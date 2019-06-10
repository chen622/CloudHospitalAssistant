<template>
    <div>
        <a-row type="flex" align="middle" justify="center">
            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                <a-button type="primary" style="width: 80%">暂存</a-button>
            </a-col>
            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                <a-button type="primary" style="width: 80%" @click="showAddInspection = true">增加</a-button>
            </a-col>
        </a-row>
        <a-table :dataSource="inspections">
            <template slot="action" slot-scope="text,record,index">
                <a-popconfirm
                        v-if="inspections.length"
                        title="确定删除？"
                        @confirm="() => deleteInspection(index)">
                    <a href="javascript:;">删除</a>
                </a-popconfirm>
            </template>
        </a-table>
        <a-modal v-if="showAddInspection">
            <template slot="title">添加新检查</template>
            <a-select v-model="newInspection">
                <a-select-option v-for="(item,index) in inspections" :key="index"></a-select-option>
            </a-select>
        </a-modal>
    </div>
</template>

<script>
    export default {
        name: "Inspection",
        data: () => ({
            showAddInspection: false,
            newInspection: null,
            inspections: [],
            inspectionsColumns: [
                {
                    title: '项目名称',
                    dataIndex: 'name',
                    align: 'center',
                },
                {
                    title: '执行科室',
                    dataIndex: 'dept.name',
                    align: 'center',
                },
                {
                    title: '状态',
                    align: 'center',
                    scopedSlots: {customRender: 'state'},
                },
                {
                    title: '价格',
                    dataIndex: 'name',
                    align: 'center',
                },
                {
                    title: '状态',
                    align: 'center',
                    scopedSlots: {customRender: 'state'},
                },
            ]
        }),
        methods: {
            deleteInspection (index) {
                this.inspections.splice(index, 1)
            },
            // getNodrug
        }
    }
</script>

<style scoped>

</style>