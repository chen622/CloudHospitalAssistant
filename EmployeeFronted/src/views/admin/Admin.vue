<template>
    <a-row type="flex" align="middle" justify="center">
        <a-col span="20">
            <a-card hoverable class="info-card" title="医院用户管理" :headStyle="{fontSize: '30px'}">
                <a-collapse defaultActiveKey="1" :bordered="false">
                    <a-collapse-panel key="1"
                                      style="background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden">
                        <template slot="header">
                            科室信息管理
                            <a-icon type="question-circle-o"/>
                        </template>
                        <p>{{text}}</p>
                    </a-collapse-panel>
                    <a-collapse-panel header="This is panel header 2" key="2"
                                      style="background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden">
                        <p>{{text}}</p>
                    </a-collapse-panel>
                </a-collapse>
            </a-card>
        </a-col>

        <a-col span="20">
            <a-card hoverable class="info-card" title="医疗基本信息管理" :headStyle="{fontSize: '30px'}">
                <a-collapse defaultActiveKey="1" :bordered="false">
                    <a-collapse-panel key="1" :style="customStyle">
                        <template slot="header">
                            科室信息管理
                            <a-icon type="question-circle-o"/>
                        </template>
                        <p>{{text}}</p>
                    </a-collapse-panel>
                    <a-collapse-panel header="This is panel header 2" key="2" :style="customStyle">
                        <p>{{text}}</p>
                    </a-collapse-panel>
                    <a-collapse-panel header="This is panel header 3" key="3" :style="customStyle">
                        <p>{{text}}</p>
                    </a-collapse-panel>
                </a-collapse>
            </a-card>
        </a-col>
    </a-row>

</template>

<script>

    export default {
        name: "DepartmentEdit",
        data: () => ({
            customStyle: 'background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden'
        }),
        methods: {

        }
    }
</script>

<style scoped>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>