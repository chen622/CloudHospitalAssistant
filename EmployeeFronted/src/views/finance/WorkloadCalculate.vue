<template>
    <div>
        <a-row type="flex" align="middle" justify="center" class="info-card">
            <a-col span="23">
                <a-card hoveracble title="工作量统计" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                    <a-tabs>
                        <a-tab-pane tab="科室工作量统计" key="1">
                            <a-row type="flex" align="middle" justify="center" class="search-card">
                                <a-col span="10" type="flex" align="top" justify="start">
                                    <a-range-picker showTime
                                                    :format="timeFormat"
                                                    v-model="pickTime">
                                    </a-range-picker>
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
                                    <a-table :columns="departmentColumns" :dataSource="departmentDataSource" :scroll="{ x: departmentScrollX, y:300}"
                                             :rowKey="departmentDataSource => departmentDataSource.department.id" :loading="loading"/>
                                </template>
                            </a-row>
                        </a-tab-pane>

                        <a-tab-pane tab="门诊医生工作量统计" key="2">
                            <a-row type="flex" align="middle" justify="center" class="search-card">
                                <a-col span="10" type="flex" align="top" justify="start">
                                    <a-range-picker showTime
                                                    :format="timeFormat"
                                                    v-model="pickTimeDoctor">
                                    </a-range-picker>
                                </a-col>

                                <a-col span="8" type="flex" align="top" justify="start">
                                    <a-button type="primary" @click="getDoctor">统计门诊医生工作量</a-button>
                                </a-col>
                            </a-row>

                            <a-row style="padding: 2% 3% 0 3%; ">
                                <template>
                                    <a-table :columns="doctorColumns" :dataSource="doctorDataSource" :scroll="{ x: doctorScrollX, y:300}"
                                             :rowKey="doctorDataSource => doctorDataSource.doctor.id" :loading="load"/>
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
            departmentColumns: [],
            doctorColumns: [],
            departmentDataSource: [],
            doctorDataSource: [],
            departmentScrollX: null,
            doctorScrollX: null,
            pickTime: [],
            pickTimeDoctor: [],
            value: null,
            loading: false,
            load: false,
            timeFormat: 'YYYY-MM-DD hh:mm:ss',
        }),
        methods: {
            getClinicDepartmentWorkLoad() {
                if (this.pickTime[0] == null) {
                    this.$message.error("请选择时间")
                    return
                }
                let request = {
                    start: this.pickTime[0] ? this.pickTime[0].utc().valueOf() : this.pickTime[0],
                    end: this.pickTime[1] ? this.pickTime[1].utc().valueOf() : this.pickTime[1]
                }
                let that = this
                this.loading = true
                this.$api.post("/department/departmentClinicWorkload", request, res => {
                    if (res.code === '100') {
                        that.departmentColumns = res.data.columns
                        that.departmentColumns.forEach(column=>{
                            if (column.dataIndex === 'total')
                                column.sorter = (a, b) => a.total - b.total
                        })
                        that.departmentScrollX = (that.departmentColumns.length + 1) * 100
                        that.departmentDataSource = res.data.data
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                    that.loading = false
                }, () => {
                    that.loading = false
                })
            },

            getTechniqueDepartmentWorkLoad() {
                if (this.pickTime[0] == null) {
                    this.$message.error("请选择时间")
                    return
                }
                let request = {
                    start: this.pickTime[0] ? this.pickTime[0].utc().valueOf() : this.pickTime[0],
                    end: this.pickTime[1] ? this.pickTime[1].utc().valueOf() : this.pickTime[1]
                }
                this.loading = true
                let that = this
                this.$api.post("/department/departmentTechniqueWorkload", request, res => {
                    if (res.code === '100') {
                        that.departmentColumns = res.data.columns
                        that.departmentDataSource = res.data.data
                        that.departmentColumns.forEach(column=>{
                            if (column.dataIndex == 'total')
                                column.sorter = (a, b) => a.total - b.total
                        })
                        that.departmentScrollX = (that.departmentColumns.length + 1) * 100
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                    that.loading = false
                }, () => {
                    that.loading = false
                })
            },

            onChange() {
                if (this.value === "a")
                    this.getClinicDepartmentWorkLoad()
                else if (this.value === "b")
                    this.getTechniqueDepartmentWorkLoad()
            },

            getDoctor() {
                if (this.pickTimeDoctor[0] == null) {
                    this.$message.error("请选择时间")
                    return
                }
                let request = {
                    start: this.pickTimeDoctor[0] ? this.pickTimeDoctor[0].utc().valueOf() : this.pickTimeDoctor[0],
                    end: this.pickTimeDoctor[1] ? this.pickTimeDoctor[1].utc().valueOf() : this.pickTimeDoctor[1]
                }
                this.load = true
                let that = this
                this.$api.post("/doctor/getDoctorWorkload", request, res => {
                    if (res.code === '100') {
                        that.doctorColumns = res.data.columns
                        that.doctorColumns.forEach(column=>{
                            if (column.dataIndex === 'total')
                                column.sorter = (a, b) => a.total - b.total
                        })
                        that.doctorScrollX = (that.doctorColumns.length + 1) * 100
                        that.doctorDataSource = res.data.data
                    } else if (res.code === '502')
                        that.$message.error(res.message)
                    that.load = false;
                }, () => {
                    that.load = false;
                })
            }
        },
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