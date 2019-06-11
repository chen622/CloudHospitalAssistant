<template>
    <a-card hoveracble title="查询" :headStyle="{fontSize: '30px'}" :body-style="{padding: 0}"
            class="info-search">
        <a-form :form="form" layout="inline" style="margin: 20px">
            <p class="form-header">患者查询:</p>
            <a-form-item label="身份证号" hideRequiredMark="false">
                <a-input placeholder="身份证号"
                         v-decorator="['id',{rules: [{required: true, message: '请输入患者身份证号', trigger: 'blur'}]}]"></a-input>
            </a-form-item>
            <p class="form-header">信息确认:</p>
            <a-form-item label="姓名">
                <a-input v-decorator="['realName']"
                         placeholder="姓名"></a-input>
            </a-form-item>
            <a-form-item label="手机号">
                <a-input v-decorator="['phone',{rules: [{type: 'number',message: '手机号非法', trigger: 'blur'}]}]"
                         placeholder="身份证号"></a-input>
            </a-form-item>
            <a-form-item>
                <a-button type="primary" @click="searchPatient">查询患者</a-button>
            </a-form-item>
        </a-form>
        <a-table :loading="load.loadPatient" :columns="columns" :dataSource="patient" rowKey="id"
                 :pagination="{defaultPageSize: 10}">
            <template slot="sex" slot-scope="text">
                <a-tag v-if="text === false" color="pink">女</a-tag>
                <a-tag v-else color="blue">男</a-tag>
            </template>
            <template slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                    <a-button type="primary" @click="$emit('selectPatient',record)">
                        选择
                    </a-button>
                </div>
            </template>
        </a-table>
    </a-card>
</template>

<script>
    export default {
        name: "SearchPatient",
        data: () => ({
            form: null,
            load: {
                loadPatient: true,
            },
            columns: [
                {
                    title: '身份证号',
                    dataIndex: 'identityId',
                    width: '15%',
                    align: 'center'
                }, {
                    title: '姓名',
                    dataIndex: 'realName',
                    width: '10%',
                    align: 'center'
                }, {
                    title: '电话',
                    dataIndex: 'phone',
                    width: '15%',
                    align: 'center'
                }, {
                    title: '年龄',
                    dataIndex: 'age',
                    width: '10%',
                    align: 'center'
                }, {
                    title: '性别',
                    dataIndex: 'sex',
                    width: '10%',
                    align: 'center',
                    scopedSlots: {customRender: 'sex'},
                }, {
                    title: '操作',
                    dataIndex: 'action',
                    width: '40%',
                    scopedSlots: {customRender: 'action'},
                    align: 'center'
                }],
            patient: [],
        }),
        methods: {
            getAllPatient () {
                let that = this
                this.load.loadPatient = true
                this.$api.get("/patient/getAll", null,
                    res => {
                        if (res.code === "100") {
                            that.patient = res.data
                            that.load.loadPatient = false
                        }
                    }, () => {
                        that.$message.error("网络异常")
                    })
            }
            ,
            searchPatient () {
                let that = this
                this.form.validateFields((err) => {
                    if (!err) {
                        this.load.loadPatient = true
                        this.$api.post("/patient/searchByMulti", this.form.getFieldsValue(),
                            res => {
                                if (res.code === "100") {
                                    that.patient = res.data
                                    that.load.loadPatient = false
                                } else {
                                    that.$message.error(res)
                                }
                            }, () => {
                                that.$message.error("网络异常")
                            }
                        )
                    }
                })
            }
        },
        mounted () {
            this.form = this.$form.createForm(this)
            this.getAllPatient()

        }
    }
</script>

<style scoped>
    .info-search {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .form-header {
        font-size: 20px;
        font-weight: bold;
        margin: 6px 0;
    }
</style>