<template>
    <a-row type="flex" align="middle" justify="center">
        <a-col span="20">
            <searchPatient @selectPatient="selectPatient"></searchPatient>
            <a-card v-if="showRegister" hoveracble title="挂号" :headStyle="{fontSize: '30px'}" class="info-search">
                <a-form :form="registration" layout="inline">
                    <a-form-item label="科室选择">
                        <a-select style="width: 200px;"
                                  v-decorator="['department',{initialValue: departmentKind[0].departments[0].id,rules: [{required: true, message: '请输入患者身份证号'}]}]">
                            >
                            <a-select-opt-group v-for="kind in departmentKind" :key="kind.id">
                                <span slot="label">{{kind.kindName}}</span>
                                <a-select-option v-for="dept in kind.departments" :key="dept.id">{{dept.name}}
                                </a-select-option>
                            </a-select-opt-group>
                        </a-select>
                    </a-form-item>
                    <a-form-item>
                        <a-button @click="searchDoctor" icon="search" type="primary">
                            搜索
                        </a-button>
                    </a-form-item>
                    <a-form-item label="医生选择" v-if="showDoctor&&!load.loadDoctor">
                        <a-select style="width: 200px;"
                                  v-decorator="['scheduleId',{initialValue: doctor[0].id,rules: [{required: true, message: '请选择挂号医生'}]}]">
                            >
                            <a-select-option v-for="doc in doctor" :key="doc.id">
                                {{doc.user.realName}}（{{doc.doctor.titleName}}）
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item v-if="showDoctor&&!load.loadDoctor">
                        <a-button @click="register" type="danger">
                            挂号
                        </a-button>
                    </a-form-item>
                </a-form>
                <a-list :loading="load.loadDoctor" v-if="showDoctor" itemLayout="horizontal"
                        :dataSource="doctor" style="margin-top: 10px;padding: 0 20px">
                    <template slot="header"><span class="form-header">可用医生</span></template>
                    <a-list-item slot="renderItem" slot-scope="item">
                        <span slot="actions" class="doctor-sub">{{item.constantVariable.name}}</span>
                        <span slot="actions" class="doctor-sub">{{item.registrationType.name}}</span>
                        <span slot="actions" class="doctor-sub">{{item.registrationType.price}}元</span>
                        <a-list-item-meta>
                            <div slot="description">
                                <p style="font-weight: normal;color: black;margin-bottom: 5px">
                                    医生职称:{{item.doctor.titleName}}</p>
                                <a-tag color="green" v-if="item.isValid"> 有余号</a-tag>
                                <a-tag color="red" v-else> 已挂满</a-tag>
                            </div>
                            <span slot="title" style="font-size: 30px;line-height: 40px">{{item.user.realName}}</span>
                            <a-avatar style="width: 100px;height: 100px" slot="avatar"
                                      :src="item.user.avatar"/>
                        </a-list-item-meta>
                    </a-list-item>
                </a-list>
            </a-card>
        </a-col>
        <a-modal
                v-model="load.register"
                :footer="false"
                :closable="false"
                :maskClosable="false"
                :bodyStyle="{textAlign: 'center'}"
        >
            <a-spin tip="正在挂号..." size="large">
            </a-spin>
        </a-modal>
        <a-modal
                v-if="showPayment"
                v-model="showPayment"
                title="缴费清单"
                :closable="false"
                :maskClosable="false"
                :bodyStyle="{textAlign: 'center'}"
        >
            <a-form>
                <a-form-item :label-col="{ span: 5,offset: 7 }"
                             :wrapper-col="{ span: 5}">
                    <span slot="label" class="payment">缴费类型</span>
                    <span class="payment">挂号费</span>
                </a-form-item>
                <a-form-item :label-col="{ span: 5,offset: 7 }"
                             :wrapper-col="{ span: 5}">
                    <span slot="label" class="payment">金额</span>
                    <span class="payment">{{payment.unitPrice}} 元</span>
                </a-form-item>
                <a-form-item :label-col="{ span: 5,offset: 7 }"
                             :wrapper-col="{ span: 5}">
                    <span slot="label" class="payment">缴费类别</span>
                    <a-select v-model="settle">
                        <a-select-option v-for="type in settlementType" :key="type.id">{{type.name}}</a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
            <template slot="footer">
                <a-button type="primary" @click="pay">缴费</a-button>
            </template>
        </a-modal>
        <a-modal
                v-if="showInvoice"
                v-model="showInvoice"
                :closable="false"
                :maskClosable="false"
                :bodyStyle="{padding: '5px'}"
                width="80%"
                @ok="$print($refs.print)">
            <invoice ref="print" :invoiceId="invoiceId"></invoice>
        </a-modal>
    </a-row>
</template>
<script>

    import Invoice from '../../components/InvoiceTemplate'
    import SearchPatient from '../../components/SearchPatient'

    export default {
        components: {
            searchPatient: SearchPatient,
            invoice: Invoice
        },
        data () {
            return {
                load: {
                    loadDoctor: true,
                    register: false
                },
                departmentKind: [],
                settlementType: [],
                registration: this.$form.createForm(this),
                showRegister: false,
                showDoctor: false,
                showPayment: false,
                showInvoice: false,
                invoiceId: null,
                doctor: [],
                requestObject: {
                    patientId: null,
                    scheduleId: null,
                    needBook: null,
                },
                payment: null,
                settle: null,
            }
        },
        methods: {
            selectPatient (record) {
                this.showRegister = true
                this.requestObject.patientId = record.id
            },
            pay () {
                let that = this
                let data = {
                    paymentId: this.payment.id,
                    settlementType: this.settle
                }
                if (this.settle === null) {
                    this.$message.error("请选择缴费类别！")
                    return
                }
                this.$api.post("/payment/payRegistration", data,
                    res => {
                        if (res.code === "100") {
                            that.$message.success("缴费成功")
                            that.showPayment = false
                            that.showInvoice = true
                            that.invoiceId = res.data.id
                        } else {
                            that.$message.error("res.msg")
                        }
                    }, () => {
                    })
            },
            register () {
                let that = this
                this.registration.validateFields((err) => {
                    if (!err) {
                        this.$confirm({
                            title: '是否需要病历本?',
                            okText: '需要',
                            cancelText: '不需要',
                            onOk () {
                                that.sendRegister(true)
                            },
                            onCancel () {
                                that.sendRegister(false)
                            },
                            class: 'test',
                        })
                    }
                })
            },
            sendRegister (needBook) {
                this.requestObject.scheduleId = this.registration.getFieldsValue().scheduleId
                this.requestObject.needBook = needBook
                this.load.register = true
                let that = this
                this.$api.post('/registration/registration', this.requestObject,
                    res => {
                        that.load.register = false
                        if (res.code === "100") {
                            that.payment = res.data
                            that.$message.success("挂号成功，请缴费！")
                            that.showPayment = true
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                        that.load.register = false
                    })
            },
            searchDoctor () {
                let that = this
                this.load.loadDoctor = true
                this.registration.validateFields((err) => {
                    if (!err) {
                        this.$api.get("/job_schedule/getSchedule/" + this.registration.getFieldsValue().department, null,
                            res => {
                                if (res.code === "100") {
                                    if (res.data.schedule.length > 0) {
                                        that.showDoctor = true
                                        that.doctor = res.data.schedule
                                    } else {
                                        that.$message.info("该科室暂无值班医生")
                                    }
                                    that.load.loadDoctor = false

                                } else {
                                    that.$message.error(res.msg)
                                }
                            }, () => {
                            })
                    }
                })
            }
            ,
            getSettlementType () {
                let that = this
                this.$api.get("/constant_variable/getSettlementType", null,
                    res => {
                        if (res.code === "100") {
                            that.settlementType = res.data
                            that.settle = that.settlementType[0].id
                        } else
                            that.$message.error(res.msg)
                    }, () => {
                    })
            },
            getDepartment () {
                let that = this
                this.$api.get("/department_kind/getClinical", null,
                    res => {
                        if (res.code === "100")
                            that.departmentKind = res.data
                    }, () => {
                    })
            }
        }
        ,
        mounted () {
            this.getDepartment()
            this.getSettlementType()
        }
    }
    ;
</script>
<style>
    .info-search {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .form-header {
        font-size: 20px;
        font-weight: bold;
        margin: 6px 0;
    }

    .editable-row-operations a {
        margin-right: 20px;
    }

    .doctor-sub {
        font-size: 18px;
        font-weight: bold;

    }

    .payment {
        font-size: 18px;
        font-weight: bold;
    }
</style>
