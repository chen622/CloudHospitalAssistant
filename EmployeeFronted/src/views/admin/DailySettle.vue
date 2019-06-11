<template>
    <div>
        <a-row type="flex"  align="middle" justify="center" class="info-medicine" >
            <a-col span="23">
                <a-card hoveracble title="门诊财务管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                    <a-tabs>
                        <a-tab-pane tab="费用科目管理" key="1">

                        </a-tab-pane>








                        <a-tab-pane tab="日结核对" key="2">
                            <a-row type="flex" align="top" justify="space-around" class="info-card">
<!--                                <a-col :span="showList?3:3" :xl="showList?6:2" style="width:20%;" >-->
                                    <a-card v-if="showList" hoverable :body-style="{padding: '2px 0 0 0'}">
                                    <span slot="title" style="font-size: 22px">
                                        <span style="margin-left:35px">收费员列表</span>
<!--                                         <a-button @click="getPatient" type="primary" shape="circle" icon="reload"-->
<!--                                                   style="float:right;margin-top:12px" size="small"></a-button>-->
                                    </span>
                                    </a-card>
<!--                                </a-col>-->
                            </a-row>
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
        data() {
            return {
                showList: true
            }
        }

    }
</script>

<style scoped>


</style>