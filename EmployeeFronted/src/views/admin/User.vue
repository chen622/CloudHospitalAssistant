<template>
    <a-row type="flex" align="middle" justify="center" class="info-card">

        <a-col span="20">
            <a-card hoverable title="用户管理" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding: '5px 0'}">

                <a-row type="flex" align="middle" justify="center" style="margin: 5px 0 10px 0">
                    <a-col span="5">
                        <a-input-search
                                placeholder="通过真实姓名搜索"
                                @search="onSearch"
                                enterButton></a-input-search>
                    </a-col>
                    <a-button @click="showCreateUser = true" type="primary" style="margin-left: 10px">
                        <a-icon type="plus-circle"/>
                        新建
                    </a-button>
                </a-row>

                <a-table :columns="columns" :dataSource="data" :pagination="{defaultPageSize: 20}" rowKey="id"
                         :loading="userLoading">
                    <template v-for="col in ['username', 'realName','type','dept.name']" :slot="col"
                              slot-scope="text, record">
                        <div :key="col">
                            <a-input
                                    v-if="record.editable"
                                    style="margin: -5px 0"
                                    :value="text"
                                    @change="e => handleChange(e.target.value, record.id, col)"/>
                            <template v-else>{{text}}</template>
                        </div>
                    </template>
                    <template slot="createTime" slot-scope="text">
                        <div>
                            <template>{{new Date(text).toLocaleString()}}</template>
                        </div>
                    </template>
                    <template slot="delete" slot-scope="text">
                        <div>
                            <a-tag v-if="text === false" color="green">未被删除</a-tag>
                            <a-tag v-else color="red">已删除</a-tag>
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
        <a-modal
                title="创建用户"
                :visible="showCreateUser"
                @ok="addUser"
                :confirmLoading="creating"
                @cancel="showCreateUser = false">
            <a-form :form="newUser">
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="用户名">
                    <a-input v-decorator="['username',{rules: rules.username}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="真实姓名">
                    <a-input v-decorator="['realName',{rules: rules.realName}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="初始密码">
                    <a-input v-decorator="['password',{rules: rules.password}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="身份证号">
                    <a-input v-decorator="['identifyId',{rules: rules.identifyId}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="个人描述">
                    <a-input v-decorator="['selfDescription',{rules: rules.selfDescription}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="科室信息">
                    <a-cascader v-decorator="['department',{rules: rules.department}]"
                                :fieldNames="{ label: 'name', value: 'id', children: 'children' }"
                                :options="option.departmentKinds" :loadData="loadData" placeholder="选择科室"/>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }"
                             label="用户类别">
                    <a-select v-model="userType">
                        <a-select-option v-for="type in option.userTypes" :key="type.id">
                            {{type.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <div v-if="isDoctor">
                    <a-form-item :label-col="{ span: 6 }"
                                 :wrapper-col="{ span: 16 }"
                                 label="医生职称">
                        <a-select v-decorator="['titleId',{rules: rules.titleId}]">
                            <a-select-option v-for="title in option.titles" :key="title.id">
                                {{title.name}}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </div>
            </a-form>
        </a-modal>
    </a-row>
</template>

<script>


    export default {
        name: "DepartmentEdit",
        data: () => ({
            columns: [
                {
                    title: '用户名',
                    dataIndex: 'username',
                    width: '15%',
                    scopedSlots: {customRender: 'username'},
                    align: 'center'
                }, {
                    title: '真实姓名',
                    dataIndex: 'realName',
                    width: '15%',
                    scopedSlots: {customRender: 'realName'},
                    align: 'center'
                }, {
                    title: '加入时间',
                    dataIndex: 'createTime',
                    width: '15%',
                    scopedSlots: {customRender: 'createTime'},
                    align: 'center',
                }, {
                    title: '用户类别',
                    width: '10%',
                    dataIndex: 'type',
                    scopedSlots: {customRender: 'type'},
                    align: 'center',
                }, {
                    title: '所在科室',
                    dataIndex: 'dept.name',
                    width: '10%',
                    scopedSlots: {customRender: 'dept.name'},
                }, {
                    title: '已被删除',
                    dataIndex: 'delete',
                    width: '10%',
                    scopedSlots: {customRender: 'delete'},
                }, {
                    title: '操作',
                    dataIndex: 'operation',
                    width: '25%',
                    scopedSlots: {
                        customRender: 'operation'
                    },
                }
            ],
            data: [
                {
                    id: 1,
                    username: "hishis",
                    realName: "ccm",
                    createTime: 1558794732000,
                    isDelete: false,
                    type: {
                        name: "管理员"
                    },
                    dept: {
                        name: "其他"
                    }
                }
            ],
            userLoading: true,
            cacheData: null,
            showCreateUser: true,
            userType: 601,
            option: {
                userTypes: [],
                departmentKinds: [],
                titles: []
            },
            creating: false,
            newUser: null,
            rules: {
                username: [{required: true, message: '请输入用户名'}],
                realName: [{required: true, message: '请输入真实姓名'}],
                password: [{required: true, message: '请输入密码'}],
                identifyId: [{required: true, message: '请输入身份证号'}],
                selfDescription: [{required: true, message: '请输入自述'}],
                titleId: [{required: true, message: '请选择职称'}]
            }
        }),
        methods: {
            addUser () {
                this.newUser.validateFields((err) => {
                    if (!err) {
                        let that = this
                        that.creating = true
                        let data = this.newUser.getFieldsValue()
                        data.typeId = this.userType
                        data.departmentId = data.department[2]
                        this.$api.post('/user/register', data,
                            res => {
                                if (res.code === '100') {
                                    that.$message.success("创建成功")
                                    that.getUser()
                                } else {
                                    that.$message.error(res.msg)
                                }
                                that.creating = false
                            }, () => {
                                that.creating = false
                            })
                    }
                })
            },
            getUseType () {
                let that = this
                this.$api.get('/constant_variable/getType/' + 6, null,
                    res => {
                        if (res.code === '100') {
                            that.option.userTypes = res.data
                        } else {
                            that.$message.success(res.data)
                        }
                    }, () => {
                    }
                )
            },
            getTitle () {
                let that = this
                this.$api.get('/constant_variable/getType/' + 7, null,
                    res => {
                        if (res.code === '100') {
                            that.option.titles = res.data
                        } else {
                            that.$message.success(res.data)
                        }
                    }, () => {
                    }
                )
            },
            getUser () {
                this.userLoading = true
                let that = this
                this.$api.get('/user/findAll', null,
                    res => {
                        if (res.code === '100') {
                            that.data = res.data
                            that.cacheData = res.data.map(item => ({...item}))
                            that.userLoading = false
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            loadData (selectedOptions) {
                let targetOption = selectedOptions[selectedOptions.length - 1];
                targetOption.loading = true;

                let that = this
                this.$api.get('/department/getByKind/' + targetOption.id, null,
                    res => {
                        targetOption.loading = false;
                        if (res.code === '100') {
                            targetOption.children = res.data
                        } else {
                            targetOption.children = []
                        }
                        that.option.departmentKinds = [...that.option.departmentKinds]
                    }, () => {
                    })
            },
            getDepartmentKind () {
                let that = this
                this.$api.get("/department_kind/getAll", null,
                    res => {
                        if (res.code === '100')
                            res.data.type.forEach(
                                type => {
                                    res.data.departmentKinds[type.id].forEach(kind => {
                                        kind.name = kind.kindName
                                        kind.isLeaf = false
                                    })
                                    type.children = res.data.departmentKinds[type.id]
                                    that.option.departmentKinds.push(type)
                                }
                            )
                    }, () => {
                    }
                )
            },
            onSearch (value) {
                if (value === undefined || value === null || value.length === 0) {
                    this.getUser()
                } else {
                    let that = this
                    that.userLoading = true
                    this.$api.get('/user/findUser/' + value, null,
                        res => {
                            if (res.code === '100') {
                                that.data = res.data
                            } else {
                                that.$message.error(res.msg)
                            }
                            that.userLoading = false
                        }, () => {
                            that.userLoading = false
                        })
                }
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
        computed: {
            isDoctor () {
                return this.userType === 602 || this.userType === 603;
            }
        },
        mounted () {
            this.getUser()
            this.getUseType()
            this.getTitle()
            this.getDepartmentKind()
            this.newUser = this.$form.createForm(this)
        }
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