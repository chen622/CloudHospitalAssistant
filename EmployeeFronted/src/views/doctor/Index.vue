<template>
    <a-row type="flex" align="top" justify="space-around" class="info-card">
        <template-drawer :type="drawerType" :showDrawer="showDrawer" :MRT="tempMRT"
                         @changeDrawer="e=>changeDrawer(e)" @useTemplate="useTemplate"
                         ref="templateDrawer"></template-drawer>
        <a-col :span="showList?6:3" :xl="showList?6:2" :style="showList?'':'text-align: center'">
            <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}">
                <span slot="title" style="font-size: 22px">患者列表
                    <a-button @click="getPatient" type="primary" shape="circle" icon="reload"
                              style="float: right;"></a-button>
                </span>
                <a-collapse defaultActiveKey="2" :bordered="false">
                    <a-collapse-panel :header="'待诊患者('+patient.waitPatient.length+')'" key="1">
                        <a-list :loading="load.patient" itemLayout="horizontal" :dataSource="patient.waitPatient"
                                style="overflow: auto;max-height: 400px">
                            <a-list-item slot="renderItem" slot-scope="item" @click="selectPatient(0,item)">
                                <a-list-item-meta>
                                <span slot="title"
                                      style="font-size: 20px;line-height: 25px">{{item.patient.realName}}</span>
                                    <span slot="description">
                                        <span>年龄: {{item.age}}岁</span>
                                        <span>性别: {{item.patient.sex?'男':'女'}}</span>
                                    </span>
                                </a-list-item-meta>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                    <a-collapse-panel :header="'在诊患者('+patient.inPatient.length+')'" key="2">
                        <a-list :loading="load.patient" itemLayout="horizontal" :dataSource="patient.inPatient"
                                style="overflow: auto;max-height: 400px">
                            <a-list-item slot="renderItem" slot-scope="item" @click="selectPatient(1,item)">
                                <a-list-item-meta>
                                <span slot="title"
                                      style="font-size: 20px;line-height: 25px">{{item.patient.realName}}</span>
                                    <span slot="description">
                                        <span>年龄: {{item.age}}岁</span>
                                        <span>性别: {{item.patient.sex?'男':'女'}}</span>
                                    </span>
                                </a-list-item-meta>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                    <a-collapse-panel header="诊毕患者" key="3">
                        <a-list :loading="load.patient" itemLayout="horizontal" :dataSource="patient.outPatient"
                                style="overflow: auto;height: 400px">
                            <a-list-item slot="renderItem" slot-scope="item">
                                <a-list-item-meta>
                                <span slot="title"
                                      style="font-size: 20px;line-height: 25px">{{item.patient.realName}}</span>
                                    <span slot="description">
                                        <span>年龄: {{item.age}}岁</span>
                                        <span>性别: {{item.patient.sex?'男':'女'}}</span>
                                    </span>
                                </a-list-item-meta>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                </a-collapse>
            </a-card>
            <a-affix v-else :offsetTop="50">
                <a-button type="primary" @click="showList = true">患者列表</a-button>
            </a-affix>
        </a-col>
        <a-col :span="showList?16:21" :xl="showList?16:22">
            <a-card :body-style="{padding: 0}">
                <div slot="title" v-if="currentPatient">
                    <span style="font-size: 22px">{{currentPatient.patient.realName}}</span>
                    <span style="margin: 0 10px">年龄: {{currentPatient.age}}岁</span>
                    <span>性别: {{currentPatient.patient.sex?'男':'女'}}</span>
                    <a-button @click="refreshMR" type="primary" shape="circle" icon="reload"
                              style="float: right;"></a-button>
                </div>
                <span slot="title" style="font-size: 22px" v-else>当前患者</span>
                <a-tabs defaultActiveKey="1" tabPosition="top" :style="{padding: '0 10px 20px 10px'}"
                        @prevClick="callback"
                        @nextClick="callback">
                    <a-tab-pane tab="病历首页" key="1">
                        <a-row type="flex" align="middle" justify="center">
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button style="width: 80%" @click="loadTemp">载入暂存信息</a-button>
                            </a-col>
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button type="primary" style="width: 80%" @click="saveTemp">暂存</a-button>
                            </a-col>
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button type="danger" style="width: 80%" @click="resetForm">清空当前页面</a-button>
                            </a-col>
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button style="width: 80%" @click="changeDrawer(true,1)">常用与模板</a-button>
                            </a-col>
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button style="width: 80%" @click="saveTemplate">存为模板</a-button>
                            </a-col>
                        </a-row>
                        <a-form :form="record">
                            <a-divider>病历内容</a-divider>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 5}">
                                <span slot="label" class="form-label">是否怀孕</span>
                                <a-select v-decorator="['isPregnant',{initialValue: 'false'}]">
                                    <a-select-option value="false">未怀孕</a-select-option>
                                    <a-select-option value="true">怀孕</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
                                <span slot="label" class="form-label">主诉</span>
                                <a-textarea placeholder="患者自述病况" :autosize="{ minRows: 2}"
                                            v-decorator="['selfDescription',{rules: rules.selfDescription}]"></a-textarea>
                            </a-form-item>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
                                <span slot="label" class="form-label">现病史</span>
                                <a-textarea placeholder="现病史" :autosize="{ minRows: 2}"
                                            v-decorator="['currentSymptom',{rules: rules.currentSymptom}]"></a-textarea>
                            </a-form-item>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
                                <span slot="label" class="form-label">现病治疗情况</span>
                                <a-textarea placeholder="现病治疗情况" :autosize="{ minRows: 2}"
                                            v-decorator="['previousTreatment',{rules: rules.previousTreatment}]"></a-textarea>
                            </a-form-item>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
                                <span slot="label" class="form-label">既往史</span>
                                <a-textarea placeholder="既往史" :autosize="{ minRows: 2}"
                                            v-decorator="['historySymptom',{rules: rules.historySymptom}]"></a-textarea>
                            </a-form-item>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
                                <span slot="label" class="form-label">过敏史</span>
                                <a-textarea placeholder="过敏史" :autosize="{ minRows: 2}"
                                            v-decorator="['allergyHistory',{rules: rules.allergyHistory}]"></a-textarea>
                            </a-form-item>
                            <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
                                <span slot="label" class="form-label">体格检查</span>
                                <a-textarea placeholder="体格检查" :autosize="{ minRows: 2}"
                                            v-decorator="['bodyExamination',{rules: rules.bodyExamination}]"></a-textarea>
                            </a-form-item>

                            <diagnose :isFinial="false"></diagnose>

                            <a-form-item style="text-align: center;margin-top: 20px">
                                <a-button type="primary" @click="submitRecord">{{patientType===0?'提交病历':'更新病历'}}
                                </a-button>
                            </a-form-item>

                        </a-form>
                    </a-tab-pane>
                    <a-tab-pane tab="检查申请" key="2">
                        <inspection v-if="currentPatient!=null" :registrationId="currentPatient.id"
                                    @refresh="refreshMR"
                                    @changeDrawer="(boo,type) => changeDrawer(boo,type)"></inspection>
                        <h1 v-else>请选择患者</h1>
                    </a-tab-pane>
                    <a-tab-pane tab="门诊确诊" key="3">
                        <diagnose v-if="currentPatient!=null" :isFinial="true"
                                  :registrationId="currentPatient.id"></diagnose>
                        <h1 v-else>请选择患者</h1>
                    </a-tab-pane>
                    <!--                    <a-tab-pane tab="处置申请" key="4">-->
                    <!--                        <a-row type="flex" align="middle" justify="space-around">-->
                    <!--                            <a-col span="3">-->
                    <!--                                <a-button type="primary" style="width: 100%">暂存</a-button>-->
                    <!--                            </a-col>-->
                    <!--                            <a-col span="3">-->
                    <!--                                <a-button type="primary" style="width: 100%">清空当前页面</a-button>-->
                    <!--                            </a-col>-->
                    <!--                        </a-row>-->

                    <!--                    </a-tab-pane>-->
                    <a-tab-pane tab="药物处方" key="5">
                        <a-divider>处方内容</a-divider>
                        <prescription v-if="currentPatient!=null" :registrationId="currentPatient.id"
                                      :isInspection="true" @refresh="refreshMR"></prescription>
                        <h1 v-else>请选择患者</h1>
                    </a-tab-pane>
                    <a-tab-pane tab="费用查询" key="6">
                        <a-divider>费用详情</a-divider>
                        <payment v-if="currentPatient!=null" :patientId="currentPatient.patient.id"
                                 :isDoctor="true"></payment>
                        <h1 v-else>请选择患者</h1>
                    </a-tab-pane>
                </a-tabs>
            </a-card>
        </a-col>
    </a-row>
</template>

<script>

    import Diagnose from '../../components/doctor/Diagnose'
    import Inspection from '../../components/Inspection'
    import Prescription from '../../components/doctor/Prescription'
    import Payment from '../../components/Payment'
    import TemplateDrawer from '../../components/doctor/TemplateDrawer'

    export default {
        name: "Index",
        components: {
            'diagnose': Diagnose,
            'inspection': Inspection,
            'prescription': Prescription,
            'payment': Payment,
            'template-drawer': TemplateDrawer
        },
        data: () => ({
            load: {
                patient: true
            },
            patient: {
                waitPatient: [],//仍在排号
                inPatient: [],//在诊
                outPatient: [],
            },
            currentPatient: null,
            rules: {
                selfDescription: [{required: true, message: '请输入患者自述', trigger: 'blur'}],
                bodyExamination: [{required: true, message: '请输入', trigger: 'blur'}],
                allergyHistory: [{required: true, message: '请输入', trigger: 'blur'}],
                historySymptom: [{required: true, message: '请输入', trigger: 'blur'}],
                previousTreatment: [{required: true, message: '请输入', trigger: 'blur'}],
                currentSymptom: [{required: true, message: '请输入', trigger: 'blur'}],
            },
            record: null,
            showList: true,
            patientType: 0,
            showDrawer: false,
            drawerType: null,
            tempMRT: null
        }),
        methods: {
            useTemplate (template) {
                this.record.setFieldsValue({
                        'selfDescription': template.selfDescription,
                        'bodyExamination': template.bodyExamination,
                        'allergyHistory': template.allergyHistory,
                        'historySymptom': template.historySymptom,
                        'previousTreatment': template.previousTreatment,
                        'currentSymptom': template.currentSymptom,
                    }
                )
                this.$store.commit('changeDiagnoseType', template.isWesternMedicine)
                template.firstDiagnose.forEach(item => {
                    item.temp = true
                    this.$store.commit('addDisease', {isFinial: false, disease: item})
                })
                template.finalDiagnose.forEach(item => {
                    item.temp = true
                    this.$store.commit('addDisease', {isFinial: true, disease: item})
                })

            },
            changeDrawer (boo, type) {
                if (type === 1)
                    this.$refs.templateDrawer.getMRT()
                else if (type === 3)
                    this.$refs.templateDrawer.getIT()
                else if (type === 4)
                    this.$refs.templateDrawer.changeDrawer(true, 2)
                this.showDrawer = boo
                this.drawerType = type
            },
            saveTemplate () {
                this.tempMRT = this.record.getFieldsValue()
                this.showDrawer = true
                this.drawerType = 2
                this.$refs.templateDrawer.changeDrawer(true, 1)
            },
            loadTemp () {
                if (this.currentPatient == null) {
                    this.$message.error("请选择病人")
                } else {
                    let that = this
                    this.$api.get('/medical_record/getTemporaryMR/' + this.currentPatient.id, null,
                        res => {
                            if (res.code === "100") {
                                that.$message.success("获取暂存信息成功")
                                that.record.setFieldsValue({
                                        'selfDescription': res.data.selfDescription,
                                        'bodyExamination': res.data.bodyExamination,
                                        'allergyHistory': res.data.allergyHistory,
                                        'historySymptom': res.data.historySymptom,
                                        'previousTreatment': res.data.previousTreatment,
                                        'currentSymptom': res.data.currentSymptom,
                                        'isPregnant': res.data.isPregnant.toString(),
                                    }
                                )
                            } else if (res.cord === '638') {
                                that.$message.info("不存在暂存信息")
                            } else {
                                that.$message.error(res.msg)
                            }
                        }, () => {
                        })
                }
            },
            saveTemp () {
                if (this.currentPatient == null) {
                    this.$message.error("请选择病人")
                } else {
                    let data = {
                        medicalRecord: this.record.getFieldsValue(),
                        registrationId: this.currentPatient.id,
                        diagnoses: this.$store.state.diagnose,
                    }
                    let that = this
                    data.medicalRecord.isWesternMedicine = this.$store.state.diagnoseType
                    this.$api.post('/medical_record/saveTemporaryMR', data,
                        res => {
                            if (res.code === "100") {
                                that.$message.success("暂存成功")
                            } else {
                                that.$message.error(res.msg)
                            }
                        }, () => {
                        })
                }
            },
            submitRecord () {
                if (this.currentPatient == null) {
                    this.$message.error("请选择病人")
                } else {
                    this.record.validateFields(err => {
                        if (!err) {
                            if (this.$store.state.diagnose.length === 0) {
                                this.$message.info("请指定疾病")
                            } else {
                                let data = {}
                                data.medicalRecord = this.record.getFieldsValue()
                                data.registrationId = this.currentPatient.id
                                data.diagnoses = this.$store.state.diagnose
                                data.medicalRecord.isWesternMedicine = this.$store.state.diagnoseType
                                this.createMedicalRecord(data)
                            }
                        }
                    })
                }
            },
            createMedicalRecord (data) {
                let that = this
                let url = ''
                if (this.patientType === 0) {
                    url = '/medical_record/firstDiagnose'
                } else {
                    url = '/medical_record/updateMR'
                    data.medicalRecord.id = this.currentPatient.MRId
                }
                this.$api.post(url, data,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("提交成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            selectPatient (type, patient) {
                this.patientType = type
                this.resetForm()
                let that = this
                this.$api.get("/medical_record/check/" + patient.id, null, res => {
                    if (res.code === '100') {
                        that.showList = false
                        that.currentPatient = patient
                        that.currentPatient.MRId = res.data.id
                        that.refreshMR()
                        that.record.setFieldsValue({
                                'selfDescription': res.data.selfDescription,
                                'bodyExamination': res.data.bodyExamination,
                                'allergyHistory': res.data.allergyHistory,
                                'historySymptom': res.data.historySymptom,
                                'previousTreatment': res.data.previousTreatment,
                                'currentSymptom': res.data.currentSymptom,
                                'isPregnant': res.data.isPregnant.toString(),
                            }
                        )
                        let diagnose = []
                        that.$store.commit('changeDiagnoseType', res.data.isWesternMedicine)
                        res.data.firstDiagnose.forEach(item => {
                            diagnose.push(item.diseaseSecond)
                        })
                        that.$store.commit("setDiagnose", {isFinial: false, disease: diagnose})
                        diagnose = []
                        res.data.finalDiagnose.forEach(item => {
                            diagnose.push(item.diseaseSecond)
                        })
                        that.$store.commit("setDiagnose", {isFinial: true, disease: diagnose})
                    } else {
                        that.$confirm({
                            title: '患者病例信息不存在，是否创建新病例?',
                            onOk () {
                                that.showList = false
                                that.currentPatient = patient
                                that.$api.post("/medical_record/comein/" + patient.id, null,
                                    res => {
                                        if (res.code === '100') {
                                            that.$message.success("创建成功")
                                        } else {
                                            that.$message.error(res.msg)
                                        }
                                    }, () => {
                                    })
                            },
                            onCancel () {

                            },
                        })
                    }
                }, () => {
                    that.$message.error("网络异常")
                })

            },
            resetForm () {
                this.record.resetFields(['selfDescription',
                    'bodyExamination',
                    'allergyHistory',
                    'historySymptom',
                    'previousTreatment',
                    'currentSymptom',
                    'isPregnant'])
                this.$store.commit('clear')
            },
            getPatient () {
                let that = this
                this.$api.get("/doctor/getAllRegistration", null,
                    res => {
                        if (res.code === "100") {
                            that.patient.waitPatient = res.data.wait
                            that.patient.inPatient = res.data.in
                            that.patient.outPatient = res.data.out
                            that.load.patient = false
                        }
                    }, () => {
                    })
            },
            refreshMR () {
                this.$store.commit('setInspections', [])
                this.$store.commit('setInspectionPrescriptions', [])
                this.getTempInspectionAndPrescription()
                this.getInspectionAndPrescription()
                this.getPayments()
            },
            getPayments () {
                let that = this
                this.$api.get("/payment/getByDoctor/" + this.currentPatient.patient.id, null, res => {
                    if (res.code === '100') {
                        that.$store.commit('setPayment', res.data)
                    }
                }, () => {
                })
            },
            getInspectionAndPrescription () {
                let that = this
                this.$api.get("/doctor/getPrescriptionAndInspection/" + this.currentPatient.id, null,
                    res => {
                        if (res.code === '100') {
                            res.data.inspections.forEach(inspection => {
                                inspection.temp = false
                                that.$store.commit('addInspections', inspection)
                            })
                            res.data.prescriptions.forEach(prescription => {
                                prescription.temp = false
                                that.$store.commit('addInspectionPrescriptions', prescription)
                            })
                        }
                    }, () => {
                    })
            },
            getTempInspectionAndPrescription () {
                let that = this
                this.$api.get("/inspection_application/getTemporaryInspection/" + this.currentPatient.id, null,
                    res => {
                        if (res.code === '100') {
                            res.data.applications.forEach(inspection => {
                                inspection.temp = true
                                that.$store.commit('addInspections', inspection)
                            })
                            res.data.prescriptions.forEach(prescription => {
                                prescription.temp = true
                                that.$store.commit('addInspectionPrescriptions', prescription)
                            })
                        }
                    }, () => {
                    })
            },
            callback () {
            }
        },
        mounted () {
            this.getPatient()
            this.record = this.$form.createForm(this)
        }
    }
</script>

<style>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .ant-list-item-meta-description > span > span {
        margin: 0px 4px;
    }

    label {
        width: 100% !important;
    }

    .ant-form-item-label {
        white-space: normal !important;
        line-height: 22px !important;
        padding-right: 2px;
        padding-top: 5px;
    }

    .ant-divider-inner-text {
        font-weight: bold;
        font-size: 22px;
        color: #096dd9;
    }

    .form-label {
        font-weight: bold;
    }
</style>