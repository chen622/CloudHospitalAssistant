<template>
    <a-row type="flex" align="middle" justify="center" class="Inspection">
        <a-col span="20">
            <a-card hoverable title="医技检查" :headStyle="{fontSize:'30px'}">
                <p style="font-size: 20px">患者查询</p>
                <a-row type="flex" align="top" justify="start" style="margin: 5px 0 10px 0;">
                    <a-col>
                        <a-form layout="inline">
                            <a-form-item label="病历号">
                                <a-input-search placeholder="病历号" @search="onSearch" enterButton
                                                v-model="id"></a-input-search>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
                <p style="font-size: 20px">患者信息</p>
                <a-form :form="form" layout="inline">
                    <a-form-item label="姓名">
                        {{realName}}
                    </a-form-item>
                    <a-form-item label="身份证号">
                        {{userid}}
                    </a-form-item>
                    <a-form-item label="电话">
                        {{phoneNumber}}
                    </a-form-item>
                </a-form>
                <br/>
                <p style="font-size: 20px">项目明细</p>
                <a-table :columns="columns" :dataSource="data" rowKey="id">
                    <a slot="patient.realName" slot-scope="text,record" href="javascript:;"
                       @click="visible=true; CurrentPatient=record">{{text}}</a>
                    <template slot="application.createTime" slot-scope="text">{{new Date(text).toLocaleDateString()}}
                    </template>
                    <template slot="check" slot-scope="text">{{text?'已诊':'待诊'}}</template>
                    <span slot="action" slot-scope="text, record">
                       <a-upload name="smfile" :multiple="true" accept="image/*" action="https://sm.ms/api/upload"
                                 @change="uploading">
                           <a-button type="primary">结果录入</a-button>
                       </a-upload>
                    </span>
                </a-table>
                <a-modal title="项目信息确认" v-model="visible" @ok="handleOk" @cancel="handleCancel" okText="执行确认"
                         cancelText="取消执行">
                    <p>病历号: {{id}}</p>
                    <p>姓名: {{username}}</p>
                    <p>项目名称: {{projectName}}</p>
                    <p>状态:{{state?'交费':'未交费'}}</p>
                </a-modal>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>

    export default {
        name: 'inspection',
        data () {
            return {
                form: this.$form.createForm(this),
                visible: false,
                id: '',
                realName: '',
                phoneNumber: '',
                projectName: '',
                state: '',
                columns: [
                    {
                        title: '病历号',
                        dataIndex: 'patientId',
                    }, {
                        title: '姓名',
                        dataIndex: 'patient.realName',
                        scopedSlots: {customRender: 'patient.realName'}
                    }, {
                        title: '项目名称',
                        dataIndex: 'paymentType.name',
                    }, {
                        title: '单价',
                        dataIndex: 'unitPrice',
                    }, {
                        title: '数量',
                        dataIndex: 'application.quantity',
                    }, {
                        title: '开立时间',
                        dataIndex: 'application.createTime',
                        scopedSlots: {customRender: 'application.createTime'}
                    }, {
                        title: '状态',
                        dataIndex: 'application.check',
                        scopedSlots: {customRender: 'check'}
                    }, {
                        title: '执行科室',
                        dataIndex: 'application.nonDrug.department.name',
                    }, {
                        title: '操作',
                        dataIndex: 'action',
                        key: 'action',
                        scopedSlots: {customRender: 'action'}
                    }],
                data: [],
                CurrentPatient: null,
                file: '',
                src: ''


            }
        },
        mounted: function () {
            this.getPatient()
        },
        methods: {
            handleOk () {
                this.visible = false
                let that = this
                that.$api.post("/inspection_application/confirmApplication/" + this.CurrentPatient.application.id, this.CurrentPatient.application.id,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("执行操作成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
            handleCancel () {
                this.visible = false
                let that = this
                that.$api.post("/inspection_application/cancelApplication/" + this.CurrentPatient.application.id, this.CurrentPatient.application.id,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("执行操作成功")
                        } else {
                            that.$message.error(res)
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
            uploading (event) {
                if (event.file.status === 'done') {
                    console.log(event.file.response.data.url)
                }

            },
            onSearch (value) {
                let that = this
                if (value === null || value === undefined) {
                    this.$api.get("/inspection_application/selectPatientInformationByNameOrId/", null,
                        res => {
                            if (res.code === "100") {
                                that.data = res.data
                            } else {
                                that.$message.error(res)
                            }
                        }, res => {
                            that.$message.error(res)
                        })

                } else {
                    this.$api.get("/inspection_application/selectPatientInformationByNameOrId/id/" + value, null,
                        res => {
                            if (res.code === "100") {
                                that.data = res.data
                            } else {
                                that.$message.error(res)
                            }
                        }, res => {
                            that.$message.error(res)
                        })
                }
            },
            getPatient () {
                let that = this
                this.$api.get("/inspection_application/selectPatientInformationByNameOrId", null,
                    res => {
                        if (res.code === "100") {
                            that.data = res.data
                        } else {
                            that.$message.error(res)
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            }

        }
    }
</script>

<style scoped>
    .Inspection {
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>