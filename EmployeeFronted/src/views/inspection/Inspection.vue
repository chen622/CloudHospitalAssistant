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
                        <a-input v-model="username" placeholder="姓名">{{username}}</a-input>
                    </a-form-item>
                    <a-form-item label="身份证号">
                        <a-input v-model="userid" placeholder="身份证号">{{userid}}</a-input>
                    </a-form-item>
                    <a-form-item label="联系方式">
                        <a-textarea v-model="address" placeholder="联系方式" autosize style="width:300px">{{address}}
                        </a-textarea>
                    </a-form-item>
                </a-form>
                <br/>
                <p style="font-size: 20px">项目明细</p>
                <a-table :columns="columns" :dataSource="data" rowKey="id">
                    <a slot="patient.username" slot-scope="text,record" href="javascript:;"
                       @click="visible=true; CurrentPatient=record">{{text}}</a>
                    <template slot="application.createTime" slot-scope="text">{{new Date(text).toLocaleDateString()}}
                    </template>
                    <span slot="action" slot-scope="text, record">
                       <a-upload name="smfile" :multiple="true" accept="image/*" action="https://sm.ms/api/upload">
                           <a-button type="primary">结果录入</a-button>
                       </a-upload>
                    </span>
                </a-table>
                <a-modal title="项目信息确认" v-model="visible" @ok="handleok" okText="执行确认" cancelText="取消执行">
                    <p>病历号: {{id}}</p>
                    <p>姓名: {{username}}</p>
                    <p>项目名称: {{projectName}}</p>
                    <p>状态: {{state}}</p>
                </a-modal>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>

    import axios from 'axios'

    export default {
        name: 'inspection',
        data() {
            return {
                form: this.$form.createForm(this),
                visible: false,
                id: '',
                username: '',
                userid: '',
                address: '',
                projectName: '',
                state: '',
                columns: [{
                    title: '病历号',
                    dataIndex: 'patientId',
                    scopedSlots: {customRender: 'patientId'}
                }, {
                    title: '姓名',
                    dataIndex: 'patient.username',
                    scopedSlots: {customRender: 'patient.username'}
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
                    dataIndex: 'state',
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
            handleok() {
                this.visible = false
                let that = this
                that.$api.post("/inspection_application/confirmApplication/" + this.CurrentPatient.id, this.CurrentPatient.id,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("执行操作成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, res => {
                        that.$message.error(res)
                    })
                console.log(this.CurrentPatient.id)
                console.log(this.CurrentPatient)
            },
            onSearch(value) {
                if (value === null || value === undefined) {
                    value === null
                }
                let that = this
                this.$api.get("/inspection_application/selectPatientInformationByNameOrId/id/" + value, null,
                    res => {
                        if (res.code === "100") {
                            that.data = res.data
                            this.username = this.data[0].user.realName
                            this.userid = this.data[0].user.identifyId
                            console.log(res.data)
                            this.address = this.data[0].patient.phoneNumber
                            this.id = this.data[0].patientId
                            this.projectName = this.data[0].paymentType.name
                            this.state = this.data[0].state
                        } else {
                            that.$message.error(res)
                        }
                    }, res => {
                        that.$message.error(res)
                    })

            },
            getPatient() {
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