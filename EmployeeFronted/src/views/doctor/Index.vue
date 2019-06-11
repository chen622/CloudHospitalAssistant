<template>
    <a-row type="flex" align="top" justify="space-around" class="info-card">
        <a-col :span="showList?6:3" :xl="showList?6:2" :style="showList?'':'text-align: center'">
            <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}">
                <span slot="title" style="font-size: 22px">患者列表
                    <a-button @click="getPatient" type="primary" shape="circle" icon="reload"
                              style="float: right;"></a-button>
                </span>
                <a-collapse defaultActiveKey="1" :bordered="false">
                    <a-collapse-panel header="待诊患者" key="1">
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
                    <a-collapse-panel header="在诊患者" key="2">
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
                </div>
                <span slot="title" style="font-size: 22px" v-else>当前患者</span>
                <a-tabs defaultActiveKey="1" tabPosition="top" :style="{padding: '0 10px 20px 10px'}"
                        @prevClick="callback"
                        @nextClick="callback">
                    <a-tab-pane tab="病历首页" key="1">
                        <a-row type="flex" align="middle" justify="center">
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button type="primary" style="width: 80%">暂存</a-button>
                            </a-col>
                            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                                <a-button type="primary" style="width: 80%">清空当前页面</a-button>
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
                            <a-divider>初步诊断</a-divider>

                            <diagnose></diagnose>

                            <a-form-item style="text-align: center;margin-top: 20px">
                                <a-button type="primary" @click="submitRecord">{{patientType===0?'提交病历':'更新病历'}}
                                </a-button>
                            </a-form-item>

                        </a-form>
                    </a-tab-pane>
                    <a-tab-pane tab="检查申请" key="2">
                        <inspection></inspection>
                    </a-tab-pane>
                    <a-tab-pane tab="门诊确诊" key="3">
                        <a-row type="flex" align="middle" justify="space-around">
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">暂存</a-button>
                            </a-col>
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">清空当前页面</a-button>
                            </a-col>
                        </a-row>

                    </a-tab-pane>
                    <a-tab-pane tab="处置申请" key="4">
                        <a-row type="flex" align="middle" justify="space-around">
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">暂存</a-button>
                            </a-col>
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">清空当前页面</a-button>
                            </a-col>
                        </a-row>

                    </a-tab-pane>
                    <a-tab-pane tab="药物处方" key="5">
                        <a-row type="flex" align="middle" justify="space-around">
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">暂存</a-button>
                            </a-col>
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">清空当前页面</a-button>
                            </a-col>
                        </a-row>

                    </a-tab-pane>
                    <a-tab-pane tab="费用查询" key="6">
                        <a-row type="flex" align="middle" justify="space-around">
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">暂存</a-button>
                            </a-col>
                            <a-col span="3">
                                <a-button type="primary" style="width: 100%">清空当前页面</a-button>
                            </a-col>
                        </a-row>
                    </a-tab-pane>
                </a-tabs>
            </a-card>
        </a-col>
    </a-row>
</template>

<script>

    import Diagnose from '../../components/Diagnose'
    import Inpsection from '../../components/Inspection'

    export default {
        name: "Index",
        components: {
            'diagnose': Diagnose,
            'inspection': Inpsection
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
                selfDescription: [{required: true, message: '请输入患者自述', trigger: 'blur'}, {}],
                bodyExamination: [{required: true, message: '请输入', trigger: 'blur'}],
                allergyHistory: [{required: true, message: '请输入', trigger: 'blur'}],
                historySymptom: [{required: true, message: '请输入', trigger: 'blur'}],
                previousTreatment: [{required: true, message: '请输入', trigger: 'blur'}],
                currentSymptom: [{required: true, message: '请输入', trigger: 'blur'}],
            },
            record: null,
            showList: true,
            patientType: 0,

        }),
        methods: {
            submitRecord () {
                this.record.validateFields(err => {
                    if (!err) {
                        if (this.$store.state.diagnose.length === 0) {
                            this.$message.info("请指定疾病")
                        } else {
                            let data = {}
                            data.medicalRecord = this.record.getFieldsValue()
                            data.registrationId = this.currentPatient.id
                            data.diagnoses = this.$store.state.diagnose
                            data.medicalRecord.isWesternMedicine = !!this.$store.state.diagnoseType
                            this.createMedicalRecord(data)
                        }
                    }
                })
            },
            createMedicalRecord (data) {
                let that = this

                if (this.patientType === 0) {
                } else {

                }

                this.$api.post("/medical_record/firstDiagnose", data,
                    res => {
                        if (res.code === "100") {
                            that.$store.commit('clearDiagnose')
                            that.$message.success("提交成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                        that.$message.error("网络错误")
                    })
            },
            selectPatient (type, patient) {
                this.patientType = type
                let that = this
                this.$api.get("/medical_record/check/" + patient.id, null, res => {
                    if (res.code === '100') {
                        that.showList = false
                        that.currentPatient = patient
                        that.record.setFieldsValue({
                                'selfDescription': res.data.selfDescription,
                                'bodyExamination': res.data.bodyExamination,
                                'allergyHistory': res.data.allergyHistory,
                                'historySymptom': res.data.historySymptom,
                                'previousTreatment': res.data.previousTreatment,
                                'currentSymptom': res.data.currentSymptom,
                                'isPregnant': res.data.isPregnant.toString()
                            }
                        )
                        let diagnose = []
                        res.data.firstDiagnose.forEach(item => {
                            diagnose.push(item.diseaseSecond)
                        })
                        that.$store.commit("setDiagnose", diagnose)
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
                                        that.$message.error("网络异常")

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
                    }, res => {
                        that.$message.error(res)
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