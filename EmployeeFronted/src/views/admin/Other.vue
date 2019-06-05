<template>
    <a-row type="flex" align="middle" justify="center" class="info-card">

        <a-col span="20">
            <a-card hoverable title="科室管理" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding: '5px 0'}">
                <!--            <a-card hoverable class="info-card" title="医疗基本信息管理" :headStyle="{fontSize: '30px'}">-->
                <!--                <a-collapse defaultActiveKey="1" :bordered="false">-->
                <!--                    <a-collapse-panel key="1" :style="customStyle">-->
                <!--                        <template slot="header">-->
                <!--                            科室信息管理-->
                <!--                            <a-icon type="question-circle-o"/>-->
                <!--                        </template>-->
                <!--                        <p>{{text}}</p>-->
                <!--                    </a-collapse-panel>-->
                <!--                    <a-collapse-panel header="This is panel header 2" key="2" :style="customStyle">-->
                <!--                        <p>{{text}}</p>-->
                <!--                    </a-collapse-panel>-->
                <!--                    <a-collapse-panel header="This is panel header 3" key="3" :style="customStyle">-->
                <!--                        <p>{{text}}</p>-->
                <!--                    </a-collapse-panel>-->
                <!--                </a-collapse>-->
                <a-row type="flex" align="middle" justify="center" style="margin: 5px 0 10px 0">
                    <a-col span="5">
                        <a-input-search
                                placeholder="通过科室名称搜索"
                                @search="onSearch"
                                enterButton></a-input-search>
                    </a-col>
                </a-row>

                <a-table :columns="columns" :dataSource="data" :pagination="{defaultPageSize: 20}" rowKey="id"
                         :loading="deptLoading">
                    <template v-for="col in ['name', 'code']" :slot="col" slot-scope="text, record">
                        <div :key="col">
                            <a-input
                                    v-if="record.editable"
                                    style="margin: -5px 0"
                                    :value="text"
                                    @change="e => handleChange(e.target.value, record.id, col)"/>
                            <template v-else>{{text}}</template>
                        </div>
                    </template>
                    <template slot="operation" slot-scope="text, record">
                        <div class='editable-row-operations'>
                            <span v-if="record.editable">
                                <a-popconfirm title='确定保存次修改吗?' @confirm="() => save(record.id)">
                                    <a>保存</a>
                                </a-popconfirm>
                            <a @click="() => cancel(record.id)">取消</a>
                            </span>
                            <span v-else>
                                <a @click="() => edit(record.id)">编辑</a>
                                <a-popconfirm title='确定删除该科室吗?' @confirm="() => remove(record.id)">
                                    <a style="color: #f5222d;">删除</a>
                                </a-popconfirm>
                            </span>
                        </div>
                    </template>
                </a-table>
            </a-card>
        </a-col>
    </a-row>
</template>

<script>
    export default {
        name: "Other",
        data: () => ({
            columns: [
                {
                    title: '名称',
                    dataIndex: 'name',
                    width: '20%',
                    scopedSlots: {customRender: 'name'},
                    align: 'center'
                }, {
                    title: '科室编号',
                    dataIndex: 'code',
                    width: '20%',
                    scopedSlots: {customRender: 'code'},
                    align: 'center'
                }, {
                    title: '科室分类',
                    dataIndex: 'departmentKind.constantVariable.name',
                    width: '20%',
                    scopedSlots: {customRender: 'departmentKind.constantVariable.name'},
                    align: 'center',
                    sorter: (a, b) => a.departmentKind.constantVariable.id - b.departmentKind.constantVariable.id
                }, {
                    title: '科室类别',
                    width: '20%',
                    dataIndex: 'departmentKind.kindName',
                    scopedSlots: {customRender: 'departmentKind.kindName'},
                    align: 'center',
                    sorter: (a,b) => a.departmentKind.id - b.departmentKind.id

                }, {
                    title: '操作',
                    dataIndex: 'operation',
                    width: '20%',
                    scopedSlots: {customRender: 'operation'},
                }
            ],
            data: [
                {
                    id: 1,
                    name: "123",
                    code: "xasxa",
                    departmentKind: {
                        kindName: "123",
                        constantVariable: {
                            name: "cla"
                        }
                    }
                }
            ],
            cacheData: null,
            deptLoading: true
        }),
        methods: {
            getDepartment () {
                let that = this
                this.$api.get("/department/get", null,
                    res => {
                        if (res.code === "100") {
                            that.data = res.data
                            that.cacheData = res.data.map(item => ({...item}))
                            that.deptLoading = false
                        } else {
                            that.$message.error(res)
                        }
                    },
                    res => {
                        that.$message.error(res)
                    })
            },
            onSearch (value) {
                alert(value)
            },
            handleChange (value, key, column) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },
            edit (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    this.data = newData
                }
            },
            save (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    delete target.editable
                    this.data = newData
                    this.cacheData = newData.map(item => ({...item}))
                }
            },
            cancel (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
                    delete target.editable
                    this.data = newData
                }
            },
        },
        mounted () {
            this.getDepartment()
        },
    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .editable-row-operations a {
        margin-right: 8px;
    }

    .ant-table {
        font-size: 16px !important;
    }
</style>