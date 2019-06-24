<template>
    <div>
        <a-card hoveracble title="查询" :headStyle="{fontSize: '30px'}" :body-style="{padding: 0}"
                class="info-search">
            <a-form :form="form" layout="inline" style="margin: 20px">
                <p class="form-header">患者查询:</p>
                <a-form-item label="身份证号" hideRequiredMark="false">
                    <a-input placeholder="身份证号"
                             v-decorator="['id']"></a-input>
                </a-form-item>
                <p class="form-header">信息确认:</p>
                <a-form-item label="姓名">
                    <a-input v-decorator="['realName']"
                             placeholder="姓名"></a-input>
                </a-form-item>
                <a-form-item label="手机号">
                    <a-input v-decorator="['phone']"
                             placeholder="手机号"></a-input>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="searchPatient">查询患者</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="danger" @click="showCreate = true">新建患者</a-button>
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
        <a-modal
                title="创建患者"
                :visible="showCreate"
                @ok="createPatient"
                :confirmLoading="load.creating"
                @cancel="showCreate=false">
            <a-form :form="newPatient">
                <a-form-item :label-col="{ span: 8 }"
                             :wrapper-col="{ span: 12 }" label="姓名">
                    <a-input
                            v-decorator="['realName',{rules: [{required: true, message: '请输入患者姓名', trigger: 'blur'}]}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 8 }"
                             :wrapper-col="{ span: 12 }" label="身份证号">
                    <a-input
                            v-decorator="['identityId',{rules: [{required: true, message: '请输入患者身份证号', trigger: 'blur'}]}]"></a-input>
                </a-form-item>
                <a-form-item :label-col="{ span: 8 }"
                             :wrapper-col="{ span: 12 }" label="性别">
                    <a-select v-decorator="['sex',{initialValue: 0}]">
                        <a-select-option :value="0">
                            女
                        </a-select-option>
                        <a-select-option :value="1">
                            男
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :label-col="{ span: 8 }"
                             :wrapper-col="{ span: 12 }" label="电话号码">
                    <a-input
                            v-decorator="['phoneNumber',{rules: [{required: true,message: '请输入患者电话号码', trigger: 'blur'}]}]"></a-input>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script>

    import AFormItem from "ant-design-vue/es/form/FormItem";

    export default {
        name: "SearchPatient",
        components: {AFormItem},
        data: () => ({
            form: null,
            load: {
                loadPatient: true,
                creating: false
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
                    dataIndex: 'phoneNumber',
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
            newPatient: null,
            showCreate: false
        }),
        methods: {
            createPatient () {
                this.newPatient.validateFields((err) => {
                    if (!err) {
                        this.load.creating = true
                        let that = this
                        this.$api.post("/patient/add", this.newPatient.getFieldsValue(),
                            res => {
                                if (res.code === '100') {
                                    that.load.creating = false
                                    that.$message.success("创建成功")
                                    that.showCreate = false
                                    that.getAllPatient()
                                } else {
                                    that.load.creating = false
                                    that.$message.error(res.msg)
                                }
                            }, () => {
                            })
                    }
                })
            },
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
                            }
                        )
                    }
                })
            }
        },
        mounted () {
            this.form = this.$form.createForm(this)
            this.newPatient = this.$form.createForm(this)
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