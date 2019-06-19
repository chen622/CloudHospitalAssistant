<template>
    <div>
        <a-divider>项目内容</a-divider>
        <a-row type="flex" align="middle" justify="center">
            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                <a-button type="primary" style="width: 80%">删除暂存</a-button>
            </a-col>
            <a-col :xl="4" :md="6" :sm="9" :xs="12" style="text-align: center">
                <a-button type="primary" style="width: 80%" @click="showAddInspection = true">增加</a-button>
            </a-col>
        </a-row>
        <a-table :dataSource="$store.state.inspections" rowKey="nonDrug.id" :columns="inspectionsColumns"
                 :pagination="false">
            <template slot="temp" slot-scope="text,record">
                {{record.temp?'暂存':'开立'}}
            </template>
            <template slot="action" slot-scope="text,record,index">
                <div class="action" v-if="$store.state.inspections.length && record.temp">
                    <a-popconfirm

                            title="确定开立？"
                            @confirm="() => saveInspection(record,index)">
                        <a>开立</a>
                    </a-popconfirm>
                    <a-popconfirm
                            title="确定删除？"
                            @confirm="() => deleteInspection(index)">
                        <a>删除</a>
                    </a-popconfirm>
                </div>
                <div class="action" v-if="$store.state.inspections.length && !record.temp">
                    <div v-if="record.state===1206||record.state===1207">
                        <span>已退费</span>
                    </div>
                    <div v-else>
                        <a v-if="record.results&&record.results.length> 0"
                           @click="showResultMethod(record.results)">查看结果</a>
                        <span v-else>无结果</span>
                    </div>
                </div>
                <a-modal title="结果" v-if="showResult" v-model="showResult">
                    <div v-for="(result,index) in results" :key="index">
                        <a-divider>{{index+1}}</a-divider>
                        <img :src="result.picture" style="width: 100%"/>
                    </div>

                </a-modal>
            </template>
        </a-table>
        <a-divider>项目用药内容</a-divider>
        <prescription :registrationId="registrationId" :isInspection="true" @refresh="refreshMR"></prescription>
        <a-modal v-if="showAddInspection" :visible="showAddInspection" @ok="addInspection"
                 @cancel="showAddInspection = false">
            <template slot="title">添加新检查</template>
            <a-form>
                <a-form-item label="类别" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select @change="selectNoDrugsType" :defaultValue="nonDrugsTypes[0].name">
                        <a-select-option v-for="(item,index) in nonDrugsTypes" :key="index">{{item.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="项目" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-select @change="selectNonDrugs">
                        <a-select-option v-for="(item,index) in nonDrugs" :key="index">{{item.name}}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="次数" :labelCol="{span: 5}" :wrapperCol="{span: 18}">
                    <a-input-number :disabled="newInspection==null" :defaultValue="1" :min="1" @change="changeAmount"/>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script>
    import Prescription from './doctor/Prescription'

    export default {
        name: "Inspection",
        props: ['registrationId'],
        components: {prescription: Prescription},
        data: () => ({
            showAddInspection: false,
            newInspection: null,
            nonDrugs: [],
            nonDrugsTypes: [],
            showResult: false,
            results: [],
            inspectionsColumns: [
                {
                    title: '项目名称',
                    dataIndex: 'nonDrug.name',
                    align: 'center',
                },
                {
                    title: '执行科室',
                    dataIndex: 'department.name',
                    align: 'center',
                },
                {
                    title: '单价',
                    dataIndex: 'nonDrug.price',
                    align: 'center',
                },
                {
                    title: '数量',
                    dataIndex: 'quantity',
                    align: 'center',
                },
                {
                    title: '状态',
                    dataIndex: 'temp',
                    align: 'center',
                    scopedSlots: {customRender: 'temp'}
                },
                {
                    title: '操作',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                },
            ]
        }),
        methods: {
            showResultMethod (results) {
                this.showResult = true
                this.results = results
            },
            saveInspection (record, index) {
                let data = {
                    isDisposal: false,
                    registrationId: this.registrationId,
                    template: {
                        applications: [record]
                    }
                }
                let that = this
                this.$api.post('/inspection_application/saveInspection', data,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("开立成功")
                            that.deleteInspection(index)
                        }
                    }, () => {
                    }
                )
            },
            deleteInspection (index) {
                this.$store.commit('removeInspections', index)
                this.saveTempInspection()
            },
            getNonDrug () {
                let that = this
                this.$api.get("/non_drug/getTypeAndNonDrug/", null,
                    res => {
                        if (res.code === '100') {
                            that.nonDrugsTypes = res.data
                            that.nonDrugs = res.data[0].nonDrugs
                        }
                    }, () => {
                    })
            },
            selectNonDrugs (index) {
                this.newInspection = this.nonDrugs[index]
                this.newInspection.quantity = 1
            },
            selectNoDrugsType (index) {
                this.nonDrugs = this.nonDrugsTypes[index].nonDrugs
            },
            addInspection () {
                if (this.newInspection === null) {
                    this.$message.info("请选择具体项目")
                } else {
                    let data = {
                        temp: true,
                        department: this.newInspection.department,
                        nonDrug: this.newInspection,
                        nonDrugId: this.newInspection.id,
                        feeTypeId: this.newInspection.feeTypeId,
                        quantity: this.newInspection.quantity
                    }
                    this.$store.commit('addInspections', data)
                    this.newInspection = null
                    this.showAddInspection = false
                    this.saveTempInspection()
                }
            },
            changeAmount (value) {
                this.newInspection.quantity = value
            },
            saveTempInspection () {
                let that = this
                let data = {
                    registrationId: this.registrationId,
                    inspections: []
                }
                this.$store.state.inspections.forEach(inspection => {
                    if (inspection.temp) {
                        data.inspections.push(inspection)
                    }
                })
                this.$api.post("/inspection_application/saveTemporaryInspection", data,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("暂存成功")
                            that.refreshMR()
                        }
                    }, () => {
                    })
            },
            refreshMR () {
                this.$emit("refresh")
            }
        },
        mounted () {
            this.getNonDrug()
        }
    }
</script>

<style scoped>
    .ant-divider-inner-text {
        margin: 10px 0;
        font-weight: bold;
        font-size: 22px;
        color: #096dd9;
    }

    .action a {
        margin-right: 20px;
    }
</style>