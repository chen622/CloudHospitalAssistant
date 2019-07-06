<template>
    <a-row type="flex" justify="center" style="margin: 20px 0">
        <a-col :xl="18">
            <a-card style="margin: 20px 0;text-align: center" hoverable bordered>
                <!--                <img-->
                <!--                        alt="example"-->
                <!--                        src="../../assets/calendar.jpg"-->
                <!--                        slot="cover"-->
                <!--                />-->
                <a-alert
                        message="选择科室后可查看对应信息"
                        type="info"
                        showIcon
                />
                <a-form layout="inline" style="margin: 20px 0">
                    <a-row style="width: 100%">
                        <a-col span="14" offset="6">
                            <a-form-item :label-col="{ span: 4}"
                                         :wrapper-col="{ span: 14 }" style="width: 100%">
                                <span slot="label" style="font-size: 22px;font-weight: bold">科室</span>
                                <a-cascader v-model="department"
                                            :fieldNames="{ label: 'name', value: 'id', children: 'children' }"
                                            :options="departmentKinds" :loadData="loadData" placeholder="选择科室"/>

                            </a-form-item>
                        </a-col>
                        <a-col :span="4">
                            <a-button type="primary" @click="search">
                                查询
                            </a-button>
                        </a-col>
                    </a-row>
                </a-form>
            </a-card>
        </a-col>

        <a-col :xl="22">
            <a-tabs type="card" size="large" :tabBarStyle="{margin: 0}">
                <a-tab-pane tab="排班结果" key="1">
                    <schedule :department="department" ref="result"></schedule>
                </a-tab-pane>
                <a-tab-pane tab="排班规则" key="2">
                    <rule :department="department" ref="rule" @refresh="search"></rule>
                </a-tab-pane>
            </a-tabs>
        </a-col>
    </a-row>

</template>

<script>
    import Rule from '../../components/admin/ScheduleRule'
    import Schedule from '../../components/admin/Schedule'
    import moment from 'moment'


    export default {
        name: "Schedule",
        components: {
            rule: Rule,
            schedule: Schedule
        },
        data: () => ({
            departmentKinds: [],
            department: null
        }),
        methods: {
            search () {
                if (this.department === null) {
                    this.$message.info("请选择科室后查看")
                } else {
                    if (this.$refs.result) {
                        this.$refs.result.selectDate(moment())
                    }
                    if (this.$refs.rule) {
                        this.$refs.rule.getRule()
                    }
                }
            },
            loadData (selectedOptions) {
                let targetOption = selectedOptions[selectedOptions.length - 1];
                targetOption.loading = true;

                let that = this
                this.$api.get('/department/getByKind/' + targetOption.id, null,
                    res => {
                        targetOption.loading = false;
                        if (res.code === '100') {
                            targetOption.children = res.data
                        } else {
                            targetOption.children = []
                        }
                        that.departmentKinds = [...that.departmentKinds]
                    }, () => {
                    })
            },
            getKinds () {
                let that = this
                this.$api.get("/department_kind/getAll", null,
                    res => {
                        if (res.code === '100')
                            res.data.type.forEach(
                                type => {
                                    res.data.departmentKinds[type.id].forEach(kind => {
                                        kind.name = kind.kindName
                                        kind.isLeaf = false
                                    })
                                    type.children = res.data.departmentKinds[type.id]
                                    that.departmentKinds.push(type)
                                }
                            )
                    }, () => {
                    }
                )

            }
        },
        mounted () {
            this.getKinds()
        }
    }
</script>

<style scoped>

</style>