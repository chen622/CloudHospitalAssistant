<template>
    <a-row type="flex" align="middle" justify="center" class="info-card">

        <a-col span="20">
            <a-card hoverable title="用户管理" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding: '5px 0'}">
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
                                placeholder="通过用户名或真实姓名搜索"
                                @search="onSearch"
                                enterButton></a-input-search>
                    </a-col>
                    <a-button @click="addUser" type="primary" style="margin-left: 10px">
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
            cacheData: null
        }),
        methods: {
            addUser () {

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
            this.getUser()
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