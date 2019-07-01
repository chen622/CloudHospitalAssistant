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
                                <a-table :columns="departmentColumns" :dataSource="departmentDataSource"
                                         :scroll="{ x: departmentScrollX, y:300}"
                                         :rowKey="departmentDataSource => departmentDataSource.department.id"
                                         :loading="loading"/>
                            </a-row>
                            <div v-if="showDepartment" style="height: 300px;width:100%">
                                <v-chart :options="departmentBar"></v-chart>
                            </div>
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
                                <a-table :columns="doctorColumns" :dataSource="doctorDataSource"
                                         :scroll="{ x: doctorScrollX, y:300}"
                                         :rowKey="doctorDataSource => doctorDataSource.doctor.id" :loading="load"/>
                            </a-row>

                            <div v-if="showDoctor" style="height: 300px;width:100%">
                                <v-chart :options="doctorBar"></v-chart>
                            </div>
                        </a-tab-pane>
                    </a-tabs>
                </a-card>
            </a-col>
        </a-row>

    </div>
</template>

<script>
    import ECharts from 'vue-echarts'
    import 'echarts/lib/chart/bar'
    import 'echarts/lib/component/dataZoom'

    export default {
        components: {
            'v-chart': ECharts
        },
        data: () => ({
            departmentBar: {
                title: {
                    text: '工作量统计'
                },
                dataset: {
                    // Provide data.
                    source: []
                },
                // Declare X axis, which is a category axis, mapping
                // to the first column by default.
                xAxis: {
                    name: '科室名称',
                    type: 'category',
                    axisLabel: {
                        interval: 0
                    }
                },
                // Declare Y axis, which is a value axis.
                yAxis: {
                    name: '金额',
                    type: 'value'
                },
                dataZoom: [
                    {
                        type: 'slider',
                        show: true,
                        start: 0,
                        end: 20,
                        filterMode: 'filter'
                    },
                ],
                // Declare several series, each of them mapped to a
                // column of the dataset by default.
                series: [{type: 'bar'}]
            },
            doctorBar: {
                title: {
                    text: '工作量统计'
                },
                dataset: {
                    // Provide data.
                    source: []
                },
                // Declare X axis, which is a category axis, mapping
                // to the first column by default.
                xAxis: {
                    name: '医生名称',
                    type: 'category',
                    axisLabel: {
                        interval: 0
                    }
                },
                // Declare Y axis, which is a value axis.
                yAxis: {
                    name: '金额',
                    type: 'value'
                },
                dataZoom: [
                    {
                        type: 'slider',
                        show: true,
                        start: 0,
                        end: 20,
                        filterMode: 'filter'
                    },
                ],
                // Declare several series, each of them mapped to a
                // column of the dataset by default.
                series: [{type: 'bar'}]
            },
            departmentColumns: [],
            doctorColumns: [],
            departmentDataSource: [],
            showDepartment: false,
            showDoctor: false,
            doctorDataSource: [],
            departmentScrollX:
                null,
            doctorScrollX:
                null,
            pickTime: [],
            pickTimeDoctor: [],
            value: null,
            loading: false,
            load: false,
            timeFormat: 'YYYY-MM-DD hh:mm:ss',
        }),
        methods: {
            getClinicDepartmentWorkLoad () {
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
                that.$store.commit("setLoading", true)
                this.$api.post("/department/departmentClinicWorkload", request, res => {
                    if (res.code === '100') {
                        that.departmentColumns = res.data.columns
                        that.departmentColumns.forEach(column => {
                            if (column.dataIndex === 'total')
                                column.sorter = (a, b) => a.total - b.total
                        })
                        that.departmentScrollX = (that.departmentColumns.length + 1) * 100
                        that.departmentDataSource = res.data.data
                        let dataset = []
                        res.data.data.forEach(work => {
                            dataset.push([work.department.name, work.total])
                        })
                        that.departmentBar.dataset.source = dataset

                        that.showDepartment = true

                    } else {
                        that.$message.error(res.message)
                    }
                    that.loading = false
                    that.$store.commit("setLoading", false)

                }, () => {
                    that.$store.commit("setLoading", false)
                    that.loading = false
                })
            }
            ,

            getTechniqueDepartmentWorkLoad () {
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
                that.$store.commit("setLoading",true)

                this.$api.post("/department/departmentTechniqueWorkload", request, res => {
                    if (res.code === '100') {
                        that.departmentColumns = res.data.columns
                        that.departmentDataSource = res.data.data
                        that.departmentColumns.forEach(column => {
                            if (column.dataIndex == 'total')
                                column.sorter = (a, b) => a.total - b.total
                        })
                        that.departmentScrollX = (that.departmentColumns.length + 1) * 100
                        let dataset = []
                        res.data.data.forEach(work => {
                            dataset.push([work.department.name, work.total])
                        })
                        that.departmentBar.dataset.source = dataset
                        that.showDepartment = true
                    } else
                        that.$message.error(res.message)
                    that.loading = false
                    that.$store.commit("setLoading",false)
                }, () => {
                    that.loading = false
                    that.$store.commit("setLoading",false)
                })
            },
            onChange () {
                if (this.value === "a")
                    this.getClinicDepartmentWorkLoad()
                else if (this.value === "b")
                    this.getTechniqueDepartmentWorkLoad()
            },
            getDoctor () {
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
                that.$store.commit("setLoading",true)
                this.$api.post("/doctor/getDoctorWorkload", request, res => {
                    if (res.code === '100') {
                        that.doctorColumns = res.data.columns
                        that.doctorColumns.forEach(column => {
                            if (column.dataIndex === 'total')
                                column.sorter = (a, b) => a.total - b.total
                        })
                        that.doctorScrollX = (that.doctorColumns.length + 1) * 100
                        that.doctorDataSource = res.data.data
                        let dataset = []
                        res.data.data.forEach(work => {
                            dataset.push([work.doctor.realName, work.total])
                        })
                        that.doctorBar.dataset.source = dataset
                        that.showDoctor = true
                    } else
                        that.$message.error(res.message)
                    that.load = false;
                    that.$store.commit("setLoading",false)
                }, () => {
                    that.load = false;
                    that.$store.commit("setLoading",false)
                })
            }
        }
        ,
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

    .echarts {
        width: 100%;
        height: 100%;
    }
</style>