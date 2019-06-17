<template>
    <div>
        <a-divider>{{isFinial?'最终诊断':'初步诊断'}}</a-divider>
        <a-modal
                v-if="showDisease"
                title="添加诊断"
                :visible="showDisease"
                @ok="addDiagnose"
                @cancel="showDisease=false"
        >
            <a-form-item label="一级疾病" :labelCol="{span: 8}" :wrapperCol="{span: 16}">
                <a-select :defaultValue="disease.first[0].id" placeholder="请选择一级疾病类型"
                          style="width: 100%" @change="selectFirst">
                    <a-select-option v-for="item in disease.first"
                                     :key="item.id">
                        {{item.name}}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="二级疾病" :labelCol="{span: 8}" :wrapperCol="{span: 16}">
                <a-select placeholder="请选择二级疾病类型"
                          style="width: 100%" @change="selectSecond">
                    <a-select-option v-for="(item,index) in disease.second"
                                     :key="index">
                        {{item.name}}
                    </a-select-option>
                </a-select>
            </a-form-item>
        </a-modal>
        <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}">
            <span slot="label" class="form-label">诊断类型</span>
            <a-radio-group style="white-space: nowrap;text-align: center"
                           buttonStyle="solid" defaultValue="true" @change="changeType">
                <a-radio-button value="false">中医诊断</a-radio-button>
                <a-radio-button value="true">西医诊断</a-radio-button>
            </a-radio-group>
            <a-button style="float: right;margin: 0 10px" type="danger" v-if="isFinial"
                      @click="submit">提交诊断
            </a-button>
            <a-button style="float: right;margin: 0 10px" type="primary"
                      @click="showDisease = true">添加诊断
            </a-button>

        </a-form-item>
        <a-table bordered :dataSource="diagnose" :columns="diseaseColumns" rowKey="id"
                 :pagination="false">
            <template slot="name" slot-scope="text">
                <span>{{text}}</span>
            </template>
            <template slot="action" slot-scope="text,record,index">
                <a-popconfirm
                        v-if="diagnose.length && record.temp"
                        title="确定删除？"
                        @confirm="() => deleteDiagnose(index)">
                    <a href="javascript:;">删除</a>
                </a-popconfirm>
            </template>
        </a-table>
    </div>
</template>

<script>
    export default {
        name: "Diagnose",
        props: ['isFinial', 'registrationId'],
        data: () => ({
            disease: {
                first: [],
                second: []
            },
            showDisease: false,
            newDiagnose: null,
            diseaseColumns: [
                {
                    title: 'ICD编码',
                    dataIndex: 'icdId',
                    align: 'center'
                },
                {
                    title: '疾病名称',
                    dataIndex: 'name',
                    align: 'center'
                }, {
                    title: '操作',
                    align: 'center',
                    scopedSlots: {customRender: 'action'},
                }
            ],
        }),
        methods: {
            submit () {
                let data = {
                    registrationId: this.registrationId,
                    diagnoses: this.$store.state.finalDiagnose
                }
                if (this.$store.state.finalDiagnose.length === 0) {
                    this.$message.error("请添加疾病")
                    return
                }
                let that = this
                this.$api.post('/medical_record/finalDiagnose', data,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("提交成功")
                        }
                    }, () => {
                        that.$message.error("网络异常")
                    })
            },
            changeType (value) {
                this.$store.commit('changeDiagnoseType', value.target.value)
            },
            addDiagnose () {
                if (this.diagnose === null) {
                    this.$message.error("请选择疾病类型")
                } else {
                    for (let diagnose of this.diagnose) {
                        if (diagnose.id === this.newDiagnose.id) {
                            this.$message.info("请勿添加重复疾病")
                            return
                        }
                    }
                    this.newDiagnose.temp = true
                    this.$store.commit('addDisease', {isFinial: this.isFinial, disease: this.newDiagnose})
                    this.showDisease = false
                    this.newDiagnose = null
                }
            },
            deleteDiagnose (index) {
                this.$store.commit("removeDisease", {isFinial: this.isFinial, index: index})
            },
            selectFirst (value) {
                let that = this
                this.$api.get("/disease_second/getDiseaseByType/" + value, null,
                    res => {
                        if (res.code === "100") {
                            that.disease.second = res.data
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })

            },
            selectSecond (value) {
                this.newDiagnose = this.disease.second[value]
            },
            getDiseaseFirst () {
                let that = this
                this.$api.get("/disease_first/getType", null,
                    res => {
                        if (res.code === "100") {
                            that.disease.first = res.data
                            that.selectFirst(res.data[0].id)
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                        that.$message.error("网络异常")
                    })
            },
        },
        computed: {
            diagnose () {

                if (this.isFinial) {
                    return this.$store.state.finalDiagnose
                } else {
                    return this.$store.state.diagnose
                }
            }
        },
        mounted () {
            this.getDiseaseFirst()
        }
    }
</script>

<style scoped>

</style>