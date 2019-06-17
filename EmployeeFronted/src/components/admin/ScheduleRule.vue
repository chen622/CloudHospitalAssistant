<template>
    <a-card :body-style="{padding: 0}">
        <a-row type="flex" align="middle" justify="center" style="margin: 20px 0">
            <a-col span="4">
                <a-button style="width: 80%" type="primary">新增</a-button>
            </a-col>
            <a-col span="4">
                <a-button style="width: 80%" type="danger">生成排班</a-button>
            </a-col>
        </a-row>
        <a-table bordered :columns="columns" :dataSource="rules" rowKey="id">
            <template slot="period" slot-scope="text,record">
                <a-select :defaultValue="record.period" v-if="record.editable">
                    <a-select-option v-for="period in types.period" :key="period.id">
                        {{period.name}}
                    </a-select-option>
                </a-select>
                <span v-else>{{text}}</span>
            </template>
            <template slot="day" slot-scope="text,record">
                <a-input-number v-if="record.editable" :defaultValue="record.day" :min="1" :max="7"></a-input-number>
                <span v-else>周{{castNumber(text)}}</span>
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
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </template>
        </a-table>
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
                },
                {
                    title: '医师职称',
                    dataIndex: 'doctor.title.name',
                },
                {
                    title: '号别',
                    dataIndex: 'registrationType.name',

                },
                {
                    title: '午别',
                    dataIndex: 'periodVariable.name',
                    scopedSlots: {customRender: 'period'}
                }, {
                    title: '日别',
                    dataIndex: 'day',
                    scopedSlots: {customRender: 'day'}
                },
                {
                    title: '操作',
                    scopedSlots: {customRender: 'action'}
                }
            ],
            rules: [],
            cacheRules: [],
            types: {
                period: []
            }
        }),
        methods: {
            edit (id) {
                const newData = [...this.rules]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    target.editable = true
                    this.rules = newData
                }
            },
            cancel (id) {
                const newData = [...this.rules]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    Object.assign(target, this.cacheRules.filter(item => id === item.id)[0])
                    delete target.editable
                    this.rules = newData
                }
            },
            remove () {

            },
            save (record) {
                let that = this
                this.$api.post("/schedule_rule/modify", record,
                    res => {
                        if (res.code === '100') {
                        }
                    },
                    () => {
                    })

            },
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
            },
            getRule () {
                let that = this
                this.$api.get("/schedule_rule/getByDepartmentId/" + this.department[2], null,
                    res => {
                        if (res.code === "100") {
                            that.rules = res.data
                            that.cacheRules = res.data.map(item => ({...item}))
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                        that.$messasge.error("网络异常")
                    })
            },
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
            if (this.department) {
                this.getRule()
            }
        }
    }
</script>

<style scoped>

</style>