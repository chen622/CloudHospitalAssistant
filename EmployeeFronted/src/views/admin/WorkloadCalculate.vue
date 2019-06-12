<template>
    <div>
        <a-row type="flex" align="middle" justify="center" class="info-card">
            <a-col span="23">
                <a-card hoveracble title="工作量统计" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                    <a-tabs>
                        <a-tab-pane tab="科室工作量统计" key="1">
                            <a-row type="flex" align="middle" justify="center" class="search-card">
                                <a-col span="5" type="flex" align="top" justify="start">
                                    <a-date-picker
                                            showTime
                                            format="YYYY-MM-DD hh:mm:ss"
                                            placeholder="起始日期"
                                            style="width: 90%"
                                            v-model="start"></a-date-picker>
                                </a-col>
                                <a-col span="5" type="flex" align="top" justify="start">
                                    <a-date-picker
                                            showTime
                                            v-model="end"
                                            format="YYYY-MM-DD hh:mm:ss"
                                            placeholder="截止日期"
                                            style="width: 90%"></a-date-picker>
                                </a-col>

                                <a-col span="8" type="flex" align="top" justify="start">
                                    <a-radio-group @change="onChange" v-model="value" buttonStyle="solid">
                                        <a-radio-button value="a">临床科室统计</a-radio-button>
                                        <a-radio-button value="b">医技科室统计</a-radio-button>
                                    </a-radio-group>
                                </a-col>
                            </a-row>

                            <a-row style="padding: 2% 3% 0 3%; ">
                                <template>
                                    <a-table :columns="columns" :dataSource="dataSource" :scroll="{ x: 2410, y:300}"
                                       :rowKey="dataSource => dataSource.department.id"/>
                                </template>
                            </a-row>
                        </a-tab-pane>

                        <a-tab-pane tab="门诊医生工作量统计" key="2">
                            <a-row type="flex" align="middle" justify="center" class="search-card">
                                <a-col span="5" type="flex" align="top" justify="start">
                                    <a-date-picker
                                            showTime
                                            format="YYYY-MM-DD hh:mm:ss"
                                            placeholder="起始日期"
                                            style="width: 90%"
                                            v-model="start"></a-date-picker>
                                </a-col>
                                <a-col span="5" type="flex" align="top" justify="start">
                                    <a-date-picker
                                            showTime
                                            v-model="end"
                                            format="YYYY-MM-DD hh:mm:ss"
                                            placeholder="截止日期"
                                            style="width: 90%"></a-date-picker>
                                </a-col>

                                <a-col span="8" type="flex" align="top" justify="start">
                                    <a-button type="primary" @click="getDoctor">统计门诊医生工作量</a-button>
                                </a-col>
                            </a-row>

                            <a-row style="padding: 2% 3% 0 3%; ">
                                <template>
                                    <a-table :columns="columns" :dataSource="dataSource" :scroll="{ x: 2410, y:300}"
                                             :rowKey="dataSource => dataSource.doctor.id"/>
                                </template>
                            </a-row>

                        </a-tab-pane>
                    </a-tabs>
                </a-card>
            </a-col>
        </a-row>

    </div>
</template>

<script>
    export default {
        data: () => ({
            columns: [],
            dataSource: [],
            start: null,
            end: null,
            value: null,
        }),
        methods: {
            getClinicDepartmentWorkLoad() {
                if (this.start==null){
                    this.$message.error("请选择起始时间")
                    return
                }
                let request = {
                    start: this.start? this.start.utc().valueOf(): this.start,
                    end: this.end? this.end.utc().valueOf(): this.end
                }
                let that = this
                this.$api.post("/department/departmentClinicWorkload", request, res => {
                    if (res.code === '100') {
                        that.columns = res.data.columns
                        that.dataSource = res.data.data
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                }, () => {
                    that.$message.error("网络异常")
                })
            },

            getTechniqueDepartmentWorkLoad() {
                if (this.start==null){
                    this.$message.error("请选择起始时间")
                    return
                }
                let request = {
                    start: this.start? this.start.utc().valueOf(): this.start,
                    end: this.end? this.end.utc().valueOf(): this.end
                }
                let that = this
                this.$api.post("/department/departmentTechniqueWorkload", request, res => {
                    if (res.code === '100') {
                        that.columns = res.data.columns
                        that.dataSource = res.data.data
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                }, () => {
                    that.$message.error("网络异常")
                })
            },

            onChange() {
                if (this.value === "a")
                    this.getClinicDepartmentWorkLoad()
                else if (this.value == "b")
                    this.getTechniqueDepartmentWorkLoad()
            },

            getDoctor() {
                if (this.start==null){
                    this.$message.error("请选择起始时间")
                    return
                }
                let request = {
                    start: this.start? this.start.utc().valueOf(): this.start,
                    end: this.end? this.end.utc().valueOf(): this.end
                }
                let that = this
                this.$api.post("/doctor/getDoctorWorkload", request, res => {
                    if (res.code === '100') {
                        that.columns = res.data.columns
                        that.dataSource = res.data.data
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                }, () => {
                    that.$message.error("网络异常")
                })
            }
        }
    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .search-card {
        padding: 10px 0 10px 10px;
        text-align: center;
    }

</style>