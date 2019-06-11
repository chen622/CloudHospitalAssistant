<template>
    <div>
        <a-row type="flex" align="middle" justify="center" class="info-medicine">
            <a-col span="23">
                <a-card hoveracble title="门诊财务管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                    <a-tabs>
                        <a-tab-pane tab="费用科目管理" key="1">

                        </a-tab-pane>


                        <a-tab-pane tab="日结核对" key="2">
                            <a-col :span="showList?6:3" :xl="showList?6:2" :style="showList?'':'text-align: center'">
                                <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}">
                                    <span slot="title" style="font-size: 22px">收费员列表
                                        <!--                                        <a-button @click="getPatient" type="primary" shape="circle" icon="reload"-->
                                        <!--                                            style="float: right;"></a-button>-->
                                    </span>
                                    <a-collapse defaultActiveKey="1" :bordered="false">
                                        <a-collapse-panel header="收费员查询" key="1">
                                            <a-row type="flex" align="top" justify="start"
                                                   style="margin: 5px 0 10px 0;">
                                                <a-input-search
                                                        placeholder="收费员编号"
                                                        @search="onSearch"
                                                        enterButton
                                                />
                                            </a-row>
                                            <a-row type="flex" align="top" justify="start"
                                                   style="margin: 5px 0 10px 0;">
                                                <a-date-picker
                                                        :mode="mode1"
                                                        showTime
                                                        format="YYYY-MM-DD hh:mm:ss"
                                                        @openChange="handleOpenChange1"
                                                        @panelChange="handlePanelChange1"
                                                        style="width: 80%"
                                                />
                                            </a-row>
                                        </a-collapse-panel>
                                        <a-collapse-panel header="全部收费员" key="2">
                                            <!--                                            <a-list :loading="load.patient" itemLayout="horizontal"-->
                                            <!--                                                    :dataSource="patient.waitPatient"-->
                                            <!--                                                    style="overflow: auto;max-height: 400px">-->
                                            <!--                                                <a-list-item slot="renderItem" slot-scope="item"-->
                                            <!--                                                             @click="selectPatient(0,item)">-->
                                            <a-list-item-meta>
                                                        <span slot="title" style="font-size: 17px;line-height: 25px;">
                                                            <span>123</span>
                                                            <span style="padding-left: 40%">真名</span>
                                                        </span>
                                            </a-list-item-meta>
                                            <!--                                                </a-list-item>-->
                                            <!--                                            </a-list>-->
                                        </a-collapse-panel>
                                    </a-collapse>
                                </a-card>
                            </a-col>

                            <a-col :span="showList?17:21" :xl="showList?17:22">
                                <a-card v-if="showList" hoverable :body-style="{padding: '10px 0 0 0'}" style="margin-left: 3%">
                                    <span slot="title" style="font-size: 22px">日结表信息

                                    </span>
                                </a-card>
                            </a-col>

                            <a-col :span="showList?16:21" :xl="showList?16:22">

                            </a-col>

                        </a-tab-pane>


                        <a-tab-pane tab="工作量统计" key="3">


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
            load: {
                patient: true
            },

            showList: true,

        })

    }
</script>

<style scoped>


</style>