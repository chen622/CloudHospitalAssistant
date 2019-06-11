<template>
    <div>
        <a-row type="flex" align="middle" justify="center">
            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                <a-button type="primary" style="width: 80%">删除暂存</a-button>
            </a-col>
            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                <a-button type="primary" style="width: 80%" @click="showAddPrescription = true">增加</a-button>
            </a-col>
        </a-row>
        <a-table :dataSource="prescriptions" rowKey="drug.id" :columns="prescriptionColumns" :pagination="false">
            <template slot="temp" slot-scope="text,record">
                {{record.temp?'暂存':'开立'}}
            </template>
            <template slot="action" slot-scope="text,record,index">
                <div class="action" v-if="prescriptions.length && record.temp">
                    <a-popconfirm

                            title="确定开立？"
                            @confirm="() => savePrescriptions(record,index)">
                        <a>开立</a>
                    </a-popconfirm>
                    <a-popconfirm
                            title="确定删除？"
                            @confirm="() => deletePrescription(index)">
                        <a>删除</a>
                    </a-popconfirm>
                </div>
            </template>
        </a-table>
        <a-modal v-if="showAddPrescription" :visible="showAddPrescription" @ok="addPrescription"
                 @cancel="showAddPrescription = false">
            <template slot="title">添加新检查</template>
            <a-form>
                <a-form-item label="类别" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select @change="selectDrugsType" :defaultValue="drugsTypes[0].name">
                        <a-select-option v-for="(item,index) in drugsTypes" :key="index">{{item.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="项目" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select @change="selectDrugs">
                        <a-select-option v-for="(item,index) in drugs" :key="index">{{item.name}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="数量" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-input-number v-model="newP.amount" :disabled="newPrescription==null" :min="1"/>
                </a-form-item>
                <a-form-item label="频率" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-input v-model="newP.frequency" :disabled="newPrescription==null"/>
                </a-form-item>
                <a-form-item label="天数" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-input-number v-model="newP.days" :disabled="newPrescription==null" :min="1"/>
                </a-form-item>
                <a-form-item label="备注" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-input v-model="newP.note" :disabled="newPrescription==null"/>
                </a-form-item>
                <a-form-item label="用法" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select v-model="newP.useage" :disabled="newPrescription==null">
                        <a-select-option v-for="item in useage" :key="item.id">{{item.name}}</a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script>
    import AFormItem from "ant-design-vue/es/form/FormItem";

    export default {
        name: "Prescription",
        components: {AFormItem},
        props: ['isInspection', 'registrationId'],
        data: () => ({
            prescriptions: [],
            newP: {
                amount: 1,
                useage: null,
                frequency: null,
                days: 1,
                note: null,
                needSkinTest: false,
            },
            newPrescription: null,
            showAddPrescription: false,
            drugsTypes: [],
            drugs: [],
            useage: [],
            prescriptionColumns: [
                {
                    title: '药品名称',
                    dataIndex: 'drug.name',
                    align: 'center',
                }, {
                    title: '数量',
                    dataIndex: 'amount',
                    align: 'center'
                },
                {
                    title: '价格',
                    dataIndex: 'drug.price',
                    align: 'center',
                },
                {
                    title: '状态',
                    dataIndex: 'temp',
                    align: 'center',
                    scopedSlots: {customRender: 'temp'}
                },
                {
                    title: '频率',
                    dataIndex: 'frequency',
                    align: 'center'
                },
                {
                    title: '操作',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                },]
        }),
        methods: {
            savePrescriptions (record, index) {
                let that = this
                let data = {
                    isDisposal: false,
                    registrationId: this.registrationId,
                    template: {
                        prescriptions: [record]
                    }
                }
                this.$api.post("/inspection_application/saveInspection", data,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("开立成功")
                            that.deletePrescription(index)
                        }
                    }, () => {
                        that.$message.error("网络异常")
                    })
            },
            addPrescription () {
                if (this.newPrescription === null || this.newP.useage === null) {
                    this.$message.info("有待选项")
                } else {
                    let data = {
                        drug: this.newPrescription,
                        drugId: this.newPrescription.id,
                        amount: this.newP.amount,
                        usageId: this.newP.useage,
                        frequency: this.newP.frequency,
                        days: this.newP.days,
                        note: this.newP.note,
                        needSkinTest: this.newP.needSkinTest,
                        feeTypeId: this.newPrescription.feeTypeId,
                        temp: true
                    }
                    this.$store.commit("addInspectionPrescriptions", data)
                    this.newPrescription = null
                    this.showAddPrescription = false
                    this.newP.useage = {
                        amount: 1,
                        useage: null,
                        frequency: null,
                        days: 1,
                        note: null,
                        needSkinTest: false,
                    }
                    this.saveTempInspection()
                }
            },
            deletePrescription (index) {
                this.$store.commit('removeInspectionPrescriptions', index)
                this.saveTempInspection()
            },
            saveTempInspection () {
                let that = this
                let data = {
                    registrationId: this.registrationId,
                    prescriptions: this.$store.state.inspectionPrescriptions
                }
                this.$api.post("/inspection_application/saveTemporaryInspectionDrug", data,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("暂存成功")
                            that.refreshMR()
                        }
                    }, () => {
                        that.$message.error("网络异常")

                    })
            },
            getUseage () {
                let that = this
                this.$api.get("/constant_variable/getType/" + 4, null,
                    res => {
                        if (res.code === "100") {
                            that.useage = res.data
                        }
                    }, () => {
                        that.$message.error("网络异常")
                    })
            },
            selectDrugsType (index) {
                this.drugs = this.drugsTypes[index].drugs
            },
            selectDrugs (index) {
                this.newPrescription = this.drugs[index]
            },
            getAllDrug () {
                let that = this
                this.$api.get("/drug/getAllDrug", null,
                    res => {
                        if (res.code === "100") {
                            that.drugsTypes = res.data
                            that.drugs = res.data[0].drugs
                        }
                    }, () => {
                        that.$message.error('网络异常')
                    }
                )
            },
            refreshMR () {
                this.$emit('refresh')
            }
        },
        mounted () {
            this.getAllDrug()
            this.getUseage()
            if (this.isInspection) {
                this.prescriptions = this.$store.state.inspectionPrescriptions
            } else {
                this.prescriptions = this.$store.state.prescription
            }
        }
    }
</script>

<style scoped>
    .action a {
        margin-right: 20px;
    }
</style>