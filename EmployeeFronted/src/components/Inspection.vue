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
        <a-table :dataSource="inspections" rowKey="id" :columns="inspectionsColumns" :pagination="false">
            <template slot="temp" slot-scope="text,record">
                {{record.temp?'暂存':'开立'}}
            </template>
            <template slot="action" slot-scope="text,record,index">
                <a-popconfirm
                        v-if="inspections.length"
                        title="确定删除？"
                        @confirm="() => deleteInspection(index)">
                    <a href="javascript:;">删除</a>
                </a-popconfirm>
            </template>
        </a-table>
        <a-modal v-if="showAddInspection" :visible="showAddInspection" @ok="addInspection"
                 @cancel="showAddInspection = false">
            <template slot="title">添加新检查</template>
            <a-form>
                <a-form-item label="类别" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select @change="selectNoDrugsType" :defaultValue="nonDrugsTypes[0].name">
                        <a-select-option v-for="(item,index) in nonDrugsTypes" :key="index">{{item.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="项目" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select @change="selectNonDrugs">
                        <a-select-option v-for="(item,index) in nonDrugs" :key="index">{{item.name}}</a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script>
    import AFormItem from "ant-design-vue/es/form/FormItem";

    export default {
        name: "Inspection",
        components: {AFormItem},
        data: () => ({
            showAddInspection: false,
            newInspection: null,
            nonDrugs: [],
            nonDrugsTypes: [],
            inspections: [],
            inspectionsColumns: [
                {
                    title: '项目名称',
                    dataIndex: 'name',
                    align: 'center',
                },
                {
                    title: '执行科室',
                    dataIndex: 'department.name',
                    align: 'center',
                },
                // {
                //     title: '状态',
                //     align: 'center',
                //     scopedSlots: {customRender: 'state'},
                // },
                {
                    title: '价格',
                    dataIndex: 'price',
                    align: 'center',
                },
                {
                    title: '状态',
                    dataIndex: 'temp',
                    align: 'center',
                    scopedSlots: {customRender: 'temp'}
                },
                {
                    title: '操作',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                },
            ]
        }),
        methods: {
            deleteInspection (index) {
                this.inspections.splice(index, 1)
            },
            getNonDrug () {
                let that = this
                this.$api.get("/non_drug/getTypeAndNonDrug/", null,
                    res => {
                        if (res.code === '100') {
                            that.nonDrugsTypes = res.data
                        }
                    }, () => {
                        that.$message.error("网络错误")
                    })
            },
            selectNonDrugs (index) {
                this.newInspection = this.nonDrugs[index]
            },
            selectNoDrugsType (index) {
                this.nonDrugs = this.nonDrugsTypes[index].nonDrugs
            },
            addInspection () {
                if (this.newInspection === null) {
                    this.$message.info("请选择具体项目")
                } else {
                    if (this.inspections.includes(this.newInspection)) {
                        this.$message.info("请不要重复添加")
                    } else {
                        this.newInspection.temp = true
                        this.inspections.push(this.newInspection)
                        this.newInspection = null
                        this.showAddInspection = false
                    }
                }
            }
        },
        mounted () {
            this.getNonDrug()
        }
    }
</script>

<style scoped>

</style>