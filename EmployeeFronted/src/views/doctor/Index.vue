<template>
    <a-row type="flex" align="middle" justify="space-around" class="info-card">
        <a-col span="6">
            <a-card title="患者列表" :body-style="{padding: '10px 0 0 0'}">
                <a-collapse defaultActiveKey="1" :bordered="false">
                    <a-collapse-panel header="待诊患者" key="1">
                        <a-list :loading="load.patient" itemLayout="horizontal" :dataSource="waitPatient"
                                style="overflow: auto;height: 400px">
                            <a-list-item slot="renderItem" slot-scope="item">
                                <a-list-item-meta>
                                <span slot="title"
                                      style="font-size: 20px;line-height: 25px">{{item.patient.realName}}</span>
                                    <span slot="description">
                                        <p>年龄: {{item.patient.age}}</p>
                                        <p>性别: {{item.patient.sex}}</p>
                                    </span>
                                </a-list-item-meta>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                    <a-collapse-panel header="已诊患者" key="2">
                        <a-list :loading="load.patient" itemLayout="horizontal" :dataSource="waitPatient"
                                style="overflow: auto;height: 400px">
                            <a-list-item slot="waitPatient" slot-scope="item, index">
                                <a-list-item-meta
                                        description="Ant Design, a design language for background applications, is refined by Ant UED Team"
                                >
                                    <a slot="title" href="https://vue.ant.design/">{{item.title}}</a>
                                    <a-avatar slot="avatar"
                                              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"/>
                                </a-list-item-meta>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                </a-collapse>
            </a-card>
        </a-col>
        <a-col span="16">
            <a-card title="患者列表">

            </a-card>
        </a-col>
    </a-row>
</template>

<script>
    export default {
        name: "Index",
        data: () => ({
            load: {
                patient: true
            },
            waitPatient: []
        }),
        methods: {
            getPatient () {
                let that = this
                this.$api.get("/doctor/getAllRegistration", null,
                    res => {
                        if (res.code === "100") {
                            that.waitPatient = res.data.wait
                            that.load.patient = false
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            }
        },
        mounted () {
            this.getPatient()
        }
    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .ant-list-item-meta-description > span > p {
        margin: 2px 0;
    }
</style>