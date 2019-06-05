<template>
    <a-row type="flex" align="middle" justify="space-around" class="info-card">
        <a-col span="6">
            <a-card title="患者列表" :body-style="{padding: '10px 0 0 0'}">
                <a-collapse defaultActiveKey="1" :bordered="false">
                    <a-collapse-panel header="待诊患者" key="1">
                        <a-list itemLayout="horizontal" :dataSource="waitPatient" style="overflow: auto;height: 400px">
                            <a-list-item slot="renderItem" slot-scope="item, index">
                                <a-list-item-meta
                                        description="Ant Design, a design language for background applications, is refined by Ant UED Team">
                                    <a slot="title" href="https://vue.ant.design/">{{item.title}}</a>
                                </a-list-item-meta>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                    <a-collapse-panel header="已诊患者" key="2">
                        <a-list-item slot="waitPatient" slot-scope="item, index">
                            <a-list-item-meta
                                    description="Ant Design, a design language for background applications, is refined by Ant UED Team"
                            >
                                <a slot="title" href="https://vue.ant.design/">{{item.title}}</a>
                                <a-avatar slot="avatar"
                                          src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"/>
                            </a-list-item-meta>
                        </a-list-item>
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
            waitPatient: [
                {
                    title: 'Ant Design Title 1',
                },
                {
                    title: 'Ant Design Title 2',
                },
                {
                    title: 'Ant Design Title 3',
                },
                {
                    title: 'Ant Design Title 4',
                },
            ]
        }),
        methods: {
            getPatient () {
                let that = this
                this.$api.get("/doctor/getAllRegistration", null,
                    res => {
                        if (res === "100") {
                            that.waitPatient = res.data.wait
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
</style>