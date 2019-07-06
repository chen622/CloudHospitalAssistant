<template>
    <a-card :loading="loading" :body-style="{padding: 0}">
        <a-row type="flex" align="middle" justify="center" style="margin: 20px 0">
            <a-col span="4">
                <a-button style="width: 80%" type="primary" @click="showAdd">新增</a-button>
            </a-col>
            <a-col span="4">
                <a-button style="width: 80%" type="danger" @click="showUse">生成排班</a-button>
            </a-col>
        </a-row>
        <a-table bordered :columns="columns" :dataSource="rules" rowKey="id">

            <template slot="registrationType" slot-scope="text,record">
                <a-select :defaultValue="record.registrationTypeId" v-if="record.editable"
                          @select="e => handleChange(e,record.id,'registrationTypeId')">
                    <a-select-option v-for="registration in types.registration" :key="registration.id">
                        {{registration.name}}
                    </a-select-option>
                </a-select>
                <span v-else>{{text}}</span>
            </template>

            <template slot="period" slot-scope="text,record">
                <a-select :defaultValue="record.period" v-if="record.editable"
                          @select="e => handleChange(e,record.id,'period')">
                    <a-select-option v-for="period in types.period" :key="period.id">
                        {{period.name}}
                    </a-select-option>
                </a-select>
                <span v-else>{{text}}</span>
            </template>
            <template slot="day" slot-scope="text,record">
                <div v-if="record.editable">
                    周
                    <a-input-number :defaultValue="record.day" :min="1" :max="7"
                                    @change="e => handleChange(e, record.id, 'day')"></a-input-number>
                </div>
                <span v-else>周{{castNumber(text)}}</span>
            </template>
            <template slot="registrationQuantity" slot-scope="text,record">
                <div v-if="record.editable">
                    <a-input-number :defaultValue="record.registrationQuantity" :min="1" :max="200"
                                    @change="e => handleChange(e, record.id, 'registrationQuantity')"></a-input-number>
                </div>
                <span v-else>{{text}}</span>
            </template>
            <template slot="action" slot-scope="text,record">
                <span v-if="record.editable">
                     <a-popconfirm title='确定保存次修改吗?' @confirm="save(record)">
                                    <a>保存</a>
                     </a-popconfirm>
                    <a-divider type="vertical"/>
                     <a @click="() => cancel(record.id)">取消</a>
                </span>
                <span v-else>
                    <a @click="()=>edit(record.id)">修改</a>
                    <a-divider type="vertical"/>
                    <a-popconfirm
                            v-if="rules.length"
                            title="确认删除吗?"
                            @confirm="() => remove(record.id)">
                        <a style="color: red">删除</a>
                    </a-popconfirm>
                </span>
            </template>
        </a-table>
        <a-modal title="生成排班表" v-if="showUseRule" v-model="showUseRule" @cancel="showUseRule=false" @ok="useRule">
            <a-form>
                <a-form-item label="生成日期" :labelCol="{span:5,offset: 5}" :wrapperCol="{span:14}">
                    <a-range-picker v-model="timeRange"
                                    format="YYYY-MM-DD"></a-range-picker>
                </a-form-item>
            </a-form>
        </a-modal>
        <a-modal title="创建排班规则" v-if="showAddRule" v-model="showAddRule" @cancel="showAddRule = false" @ok="add">
            <a-form :form="ruleForm" style="text-align: center">
                <a-form-item label="医师" :labelCol="{span:5,offset: 5}" :wrapperCol="{span:10}">
                    <a-select v-decorator="['doctorId',{rules: checkRules.doctor}]">
                        <a-select-option v-for="doctor in types.doctor" :key="doctor.id">
                            {{doctor.realName}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="挂号类别" :labelCol="{span:5,offset: 5}" :wrapperCol="{span:10}">
                    <a-select v-decorator="['registrationTypeId',{rules: checkRules.registrationType}]">
                        <a-select-option v-for="registration in types.registration" :key="registration.id">
                            {{registration.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="午别" :labelCol="{span:5,offset: 5}" :wrapperCol="{span:10}">
                    <a-select v-decorator="['period',{rules: checkRules.period}]">
                        <a-select-option v-for="period in types.period" :key="period.id">
                            {{period.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="挂号数量" :labelCol="{span:5,offset: 5}" :wrapperCol="{span:10}">
                    <a-input-number
                            v-decorator="['registrationQuantity', { initialValue: 1,rules: checkRules.registrationQuantity}]"
                            :min="1" :max="200"
                            style="width: 100%"/>
                </a-form-item>
                <a-form-item label="日别" :labelCol="{span:5,offset: 5}" :wrapperCol="{span:10}">
                    <a-select v-decorator="['day',{rules: checkRules.day}]">
                        <a-select-option v-for="i in 7" :key="i">
                            周{{castNumber(i)}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-modal>
    </a-card>
</template>

<script>

    export default {
        name: "ScheduleRule",
        props: ['department'],
        data: () => ({
            columns: [
                {
                    title: '医师姓名',
                    dataIndex: 'user.realName',
                    sorter: (a, b) => a.user.realName - b.user.realName,
                },
                {
                    title: '医师职称',
                    dataIndex: 'doctor.title.name',
                },
                {
                    title: '号别',
                    dataIndex: 'registrationType.name',
                    scopedSlots: {customRender: 'registrationType'}
                },
                {
                    title: '午别',
                    dataIndex: 'periodVariable.name',
                    scopedSlots: {customRender: 'period'}
                }, {
                    title: '日别',
                    dataIndex: 'day',
                    scopedSlots: {customRender: 'day'},
                    sorter: (a, b) => a.day - b.day,
                }, {
                    title: '挂号数量',
                    dataIndex: 'registrationQuantity',
                    scopedSlots: {customRender: 'registrationQuantity'}
                },
                {
                    title: '操作',
                    scopedSlots: {customRender: 'action'}
                }
            ],
            rules: [],
            cacheRules: [],
            types: {
                period: [],
                registration: [],
                doctor: [],
            },
            checkRules: {
                doctor: [{required: true, message: '请选择医生'}],
                period: [{required: true, message: '请选择午别'}],
                registrationType: [{required: true, message: '请选择挂号类别'}],
                registrationQuantity: [{required: true, message: '请输入挂号数量'}],
                day: [{required: true, message: '请选择日别'}]
            },
            ruleForm: null,
            loading: false,
            showAddRule: false,
            showUseRule: false,
            timeRange: []
        }),
        methods: {
            useRule () {
                if (this.timeRange.length === 2) {
                    let that = this
                    let data = {
                        start: this.timeRange[0],
                        end: this.timeRange[1],
                        departmentId: this.department[2]
                    }
                    this.$api.post('/schedule_rule/use', data,
                        res => {
                            if (res.code === '100') {
                                that.$message.success("生成成功, 共生成 " + res.data.length + " 条")
                                that.showUseRule = false
                                that.$emit("refresh")
                            } else {
                                that.$message.error("生成失败")
                            }
                        }, () => {
                        })
                } else {
                    this.$message.info("请选择日期范围")
                }
            },
            add () {
                let that = this
                this.ruleForm.validateFields((err) => {
                    if (!err) {
                        that.$api.post('/schedule_rule/insert', this.ruleForm.getFieldsValue(),
                            res => {
                                if (res.code === '100') {
                                    that.$message.success("创建成功")
                                    that.showAddRule = false
                                    that.getRule()
                                } else {
                                    that.$message.error(res.msg)
                                }
                            }, () => {
                            })

                    }
                })
            },
            showAdd () {
                if (this.department) {
                    this.ruleForm = this.$form.createForm(this)
                    this.getDoctorByDepartment()
                    this.showAddRule = true
                } else {
                    this.$message.info("请先选择科室")
                }
            }
            ,
            showUse () {
                if (this.department) {
                    this.showUseRule = true
                } else {
                    this.$message.info("请先选择科室")
                }
            },
            handleChange (value, id, column) {
                const newData = [...this.rules]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    target[column] = value
                    this.rules = newData
                }
            }
            ,
            edit (id) {
                const newData = [...this.rules]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    target.editable = true
                    this.rules = newData
                }
            }
            ,
            cancel (id) {
                const newData = [...this.rules]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    Object.assign(target, this.cacheRules.filter(item => id === item.id)[0])
                    delete target.editable
                    this.rules = newData
                }
            }
            ,
            remove (id) {
                let that = this
                this.$api.post("/schedule_rule/delete/" + id, null,
                    res => {
                        if (res.code === '100') {
                            that.getRule()
                        } else {
                            that.$message.error("修改失败！" + res.msg)
                        }
                    },
                    () => {
                    })
            }
            ,
            save (record) {
                let that = this
                this.$api.post("/schedule_rule/modify", record,
                    res => {
                        if (res.code === '100') {
                            that.getRule()
                        } else {
                            that.$message.error("修改失败！" + res.msg)
                        }
                    },
                    () => {
                    })
            }
            ,
            getDoctorByDepartment () {
                let that = this
                this.$api.get("/doctor/getByDepartmentId/" + this.department[2], null,
                    res => {
                        if (res.code === '100') {
                            that.types.doctor = res.data
                        } else {
                            that.$message.info("请求医生列表失败")
                        }
                    }, () => {
                    })
            }
            ,
            getRegistrationType () {
                let that = this
                this.$api.get("/registration_type/get", null,
                    res => {
                        if (res.code === '100') {
                            that.types.registration = res.data
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            }
            ,
            getDayType () {
                let that = this
                this.$api.get("/constant_variable/getType/" + 3, null,
                    res => {
                        if (res.code === '100') {
                            that.types.period = res.data
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            }
            ,
            getRule () {
                let that = this
                that.$store.commit('setLoading', true)
                this.$api.get("/schedule_rule/getByDepartmentId/" + this.department[2], null,
                    res => {
                        if (res.code === "100") {
                            that.rules = res.data
                            that.cacheRules = res.data.map(item => ({...item}))
                        } else {
                            that.$message.error(res.msg)
                        }
                        that.$store.commit('setLoading', false)
                    }, () => {
                        that.$messasge.error("网络异常")
                        that.$store.commit('setLoading', false)

                    })
            }
            ,
            castNumber (number) {
                switch (number) {
                    case 1:
                        return '一'
                    case 2:
                        return '二'
                    case 3:
                        return '三'
                    case 4:
                        return '四'
                    case 5:
                        return '五'
                    case 6:
                        return '六'
                    case 7:
                        return '日'
                }
            }
        }, mounted () {
            this.getDayType()
            this.getRegistrationType()
            if (this.department) {
                this.getRule()
            }
        }
    }
</script>

<style scoped>

</style>