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
                <!--                <p style="font-size: 20px">患者信息</p>-->
                <!--                <a-form :form="form" layout="inline">-->
                <!--                    <a-form-item label="姓名">-->
                <!--                        {{realName}}-->
                <!--                    </a-form-item>-->
                <!--                    <a-form-item label="身份证号">-->
                <!--                        {{userid}}-->
                <!--                    </a-form-item>-->
                <!--                    <a-form-item label="电话">-->
                <!--                        {{phoneNumber}}-->
                <!--                    </a-form-item>-->
                <!--                </a-form>-->
                <!--                <br/>-->
                <p style="font-size: 20px">项目明细</p>
                <a-table :columns="columns" :dataSource="data" rowKey="id">
                    <template slot="application.createTime" slot-scope="text">{{new Date(text).toLocaleDateString()}}
                    </template>
                    <template slot="check" slot-scope="text,record">
                        {{record.application.done?'已完成':(text?(record.application.canceled?'已退费':'已缴费'):'未缴费')}}
                    </template>
                    <span slot="action" slot-scope="text, record" class="action">
                        <p v-if="!record.application.done && record.application.check && !record.application.canceled">
                            <a-divider type="vertical"></a-divider>
                            <a-upload name="pic"
                                      :multiple="true" :beforeUpload="beforeUpload"
                                      :action="$url+'/inspection_application/upload'"
                                      :headers="header"
                                      @change="uploading($event,record)">
                                <a>结果录入</a>
                            </a-upload>
                        </p>
                        <p v-if="record.application.results&&record.application.results.length > 0">
                            <a-divider type="vertical"></a-divider>
                            <a @click="showResultMethod(record.application.results)">查看结果</a>
                        </p>
                        <p v-if="!record.application.done">
                            <a-divider type="vertical"></a-divider>
                            <a @click="changeState(record)" style="color: red">更改状态</a>
                        </p>
                    </span>
                </a-table>
                <a-modal title="结果" v-if="showResult" v-model="showResult" :footer="null" @cancel="showResult = false">
                    <div v-for="(result,index) in results" :key="index">
                        <a-divider>{{index+1}}</a-divider>
                        <img :src="result.picture" style="width: 100%"/>
                    </div>
                </a-modal>
                <a-modal title="项目信息确认" v-if="visible" v-model="visible" @cancel="visible=false"
                         style="text-align: center">
                    <p style="font-size: 20px">姓名: {{currentPatient.user.realName}}</p>
                    <p style="font-size: 20px">项目名称: {{currentPatient.application.nonDrug.name}}</p>
                    <div slot="footer">
                        <a-button @click="handleOk" type="primary">执行确认</a-button>
                        <a-button @click="handleCancel" type="primary">取消执行</a-button>
                    </div>
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
                id: null,
                header: {authorization: sessionStorage.getItem('token')},
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
                        dataIndex: 'application.nonDrug.name',
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
                showResult: false,
                results: [],
                currentPatient: null,
                record: null
            }
        },
        mounted: function () {
            this.getPatient()
        },
        methods: {
            showResultMethod (results) {
                this.showResult = true
                this.results = results
            },
            changeState (record) {
                this.visible = true
                this.currentPatient = record
            },
            handleOk () {
                this.visible = false
                let that = this
                that.$api.post("/inspection_application/confirmApplication/" + this.currentPatient.application.id, null,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("执行操作成功")
                            that.onSearch('')

                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            handleCancel () {
                this.visible = false
                let that = this
                that.$api.post("/inspection_application/cancelApplication/" + this.currentPatient.application.id, null,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("执行操作成功")
                            that.onSearch('')
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            beforeUpload (file) {
                let that = this
                return new Promise((resolve, reject) => {
                    const type = file.type
                    if (type !== "image/jpeg") {
                        that.$message.error("上传文件格式必须为图片")
                        return reject(false)
                    } else {
                        return resolve(true)
                    }
                })
            },
            uploading (event, record) {
                if (event.file.response) {
                    if (event.file.response.code === '100') {
                        let data = {
                            picture: event.file.response.data,
                            text: '',
                            inspectionApplicationId: record.application.id
                        }
                        this.$message.success("上传成功")
                        this.submitRecord(data)
                    } else {
                        this.$message.error("上传失败")
                    }
                }

            },
            submitRecord (data) {
                let that = this
                this.$api.post("/inspection_application/entryApplicationResult", data,
                    res => {
                        if (res.code === '100') {
                            that.$message.success('提交成功')
                            that.onSearch('')
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                        that.$message.error("网络错误")
                    })
            },
            onSearch (value) {
                let that = this
                if (value === null || value === '') {
                    this.$api.get("/inspection_application/selectPatientInformationByNameOrId/", null,
                        res => {
                            if (res.code === "100") {
                                that.data = res.data
                            } else {
                                that.$message.error(res)
                            }
                        }, () => {
                        })

                } else {
                    this.$api.get("/inspection_application/selectPatientInformationByNameOrId/id/" + value, null,
                        res => {
                            if (res.code === "100") {
                                that.data = res.data
                            } else {
                                that.$message.error(res)
                            }
                        }, () => {
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
                    }, () => {
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

    .action p {
        margin: 0;
    }
</style>