<template>
    <a-row type="flex" align="middle" justify="center" class="info-card">
        <a-modal
                v-if="newDepartment.isCreating"
                :confirmLoading="newDepartment.createLoading"
                :visible="newDepartment.isCreating"
                @cancel="newDepartment.isCreating = false"
                @ok="addDepartment"
                title="创建科室">
            <a-form :form="newDepartment.item">
                <a-form-item label="科室名称">
                    <a-input v-decorator="['name',{rules: rules.name}]"
                             placeholder="请输入对应名称"/>
                </a-form-item>
                <a-form-item label="科室编号">
                    <a-input v-decorator="['code',{rules: rules.code}]"
                             placeholder="请输入科室对应编号"/>
                </a-form-item>
                <a-form-item label="科室分类">
                    <a-select
                            v-decorator="['typeId',{initialValue: departmentKind.type[0].id,rules: rules.typeId}]"
                            @change="changeKind">
                        <a-select-option v-for="t in departmentKind.type" :key="t.id">{{t.name}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="科室类别">
                    <a-select
                            v-decorator="['kindId',{initialValue: departmentKinds[0].id,rules: rules.kindId}]"
                            style="width: 120px">
                        <a-select-option v-for="kind in departmentKinds" :key="kind.id">{{kind.kindName}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-modal>

        <a-col span="20">
            <a-card hoverable title="科室管理" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding: '5px 0'}">
                <a-row type="flex" align="middle" justify="center" style="margin: 5px 0 10px 0">
                    <a-col span="5">
                        <a-input-search
                                placeholder="通过科室名称搜索"
                                @search="onSearch"
                                enterButton></a-input-search>
                    </a-col>
                    <a-button @click="showCreate" type="primary" style="margin-left: 10px">
                        <a-icon type="plus-circle"/>
                        新建
                    </a-button>
                </a-row>

                <a-table :columns="deptColumns" :dataSource="departments" :pagination="{defaultPageSize: 10}"
                         rowKey="id"
                         :loading="loading.department">
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
            departmentKind: [],//所有选项
            departmentKinds: [],//第二个选项
            cacheData: null,
            newDepartment: {
                item: null,
                isCreating: false,
                createLoading: false,
            },
            loading: {
                department: true
            },
            deptColumns: [
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
                    sorter: (a, b) => a.departmentKind.id - b.departmentKind.id

                }, {
                    title: '操作',
                    dataIndex: 'operation',
                    width: '20%',
                    scopedSlots: {customRender: 'operation'},
                }
            ],
            departments: [],
            rules: {
                kindId: [{required: true, message: '请选择科室类别'}],
                typeId: [{required: true, message: '请选择科室分类'}],
                name: [{required: true, message: '请输入科室名称', trigger: 'blur'}],
                code: [{required: true, message: '请输入科室对应编号', trigger: 'blur'}]
            }
        }),
        methods: {
            showCreate () {
                this.newDepartment.isCreating = true
                this.newDepartment.item = this.$form.createForm(this)
            },
            changeKind (key) {
                this.departmentKinds = this.departmentKind.departmentKinds[parseInt(key)]
            },
            addDepartment () {
                this.newDepartment.createLoading = true
                let that = this
                this.newDepartment.item.validateFields((err) => {
                    if (!err) {
                        this.$api.post("/department/add", this.newDepartment.item.getFieldsValue(),
                            res => {
                                if (res.code === "100") {
                                    that.$message.success("创建成功！")
                                    that.newDepartment.isCreating = false
                                    that.getDepartment()
                                } else {
                                    that.$message.error(res.msg)
                                }
                                that.newDepartment.createLoading = false

                            }, res => {
                                that.$message.error(res)
                                that.newDepartment.createLoading = false
                            })
                    }
                })
            },
            getDepartment () {
                let that = this
                that.loading.department = true
                this.$api.get("/department/get", null,
                    res => {
                        if (res.code === "100") {
                            that.departments = res.data
                            that.cacheData = res.data.map(item => ({...item}))
                            that.loading.department =false
                        } else {
                            that.$message.error(res)
                        }
                    },
                    res => {
                        that.$message.error(res)
                    })
            },
            getDepartmentKind () {
                let that = this
                this.$api.get("/department_kind/getAll", null,
                    res => {
                        if (res.code === "100") {
                            that.departmentKind = res.data
                            that.departmentKinds = res.data.departmentKinds[res.data.type[0].id]
                        } else {
                            that.$message.error(res)
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
            onSearch (value) {
                if (value === null || value === undefined)
                    value = null
                let that = this
                that.loading.department = true
                this.$api.get("/department/getDepartmentList/" + value, null,
                    res => {
                        if (res.code === "100") {
                            that.departments = res.data
                            that.cacheData = res.data.map(item => ({...item}))
                            that.loading.department = false
                        } else {
                            that.$message.error(res)
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
            handleChange (value, key, column) {
                const newData = [...this.departments]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target[column] = value
                    this.departments = newData
                }
            },
            edit (key) {
                const newData = [...this.departments]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    this.departments = newData
                }
            },
            save (key) {
                const newData = [...this.departments]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    delete target.editable
                    let that = this
                    this.$api.post("/department/modify", target,
                        res => {
                            if (res.code === '100') {
                                that.$message.success("修改成功")
                                that.getDepartment()
                            } else {
                                that.$message.error(res.msg)
                            }
                        },
                        () => {
                            that.$message.error("网络异常！")
                        })
                }
            },
            remove (key) {
                let that = this
                this.$api.post("/department/delete/" + key, null,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("删除成功")
                            that.getDepartment()
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                        that.$message.error("网络异常！")
                    })
            },
            cancel (key) {
                const newData = [...this.departments]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
                    delete target.editable
                    this.departments = newData
                }
            },
        },
        mounted () {
            this.getDepartment()
            this.getDepartmentKind()
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