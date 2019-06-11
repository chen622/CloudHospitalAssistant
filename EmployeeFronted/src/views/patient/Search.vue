<template>
    <a-row type="flex" align="middle" justify="center" class="SearchInfo">
        <a-col span="20">
            <a-card hoverable title="费用查询" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                <p style="font-size: 20px">患者查询</p>
                <a-row type="flex" align="top" justify="start" style="margin: 5px 0 10px 0;">
                    <a-col>
                        <a-form layout="inline">
                            <a-form-item label="病历号">
                                <a-input-search placeholder="病历号" @search="onSearch" enterButton v-model="id"></a-input-search>
                            </a-form-item>
                        </a-form>
                    </a-col>
                    <a-col>
                        <a-date-picker
                                :disabledDate="disabledStartDate"
                                showTime
                                format="YYYY-MM-DD HH:mm:ss"
                                v-model="startValue"
                                placeholder="开始时间"
                                @openChange="handleStartOpenChange"/>
                        <a-date-picker
                                :disabledDate="disabledEndDate"
                                showTime
                                format="YYYY-MM-DD HH:mm:ss"
                                placeholder="结束时间"
                                v-model="endValue"
                                :open="endOpen"
                                @openChange="handleEndOpenChange"/>
                    </a-col>
                </a-row>
                <p style="font-size: 20px">收费项目明细:</p>
                <a-table :columns="columns"  :dataSource="data">
                    <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                </a-table>
            </a-card>
        </a-col>
    </a-row>
</template>


<script>
    import ARow from "ant-design-vue/es/grid/Row";
    import AFormItem from "ant-design-vue/es/form/FormItem";

    export default {
        name:'search',
        data(){
            return{
                form:this.$form.createForm(this),
                id:'',
                startValue: null,
                endValue: null,
                endOpen: false,
                headers:{
                    authorization:'auhorization-text',
                },
                columns:[{
                    title:'病历号',
                    dataIndex:'id',
                    key:'id',
                    scopedSlots:{customRender:'id'}
                },{
                    title:'姓名',
                    dataIndex:'name',
                    key:'name',
                    scopedSlots:{customRender:'name'}
                },{
                    title:'项目名称',
                    dataIndex:'depname',
                    key:'depname',
                    scopedSlots:{customRender:'depname'}
                },{
                    title:'单价',
                    dataIndex:'price',
                    key:'price',
                    scopedSlots:{customRender:'price'}
                },{
                    title:'数量',
                    dataIndex:'amount',
                    key:'amount',
                    scopedSlots:{customRender:'amount'}
                },{
                    title:'开立时间',
                    dataIndex:'date',
                    key:'date',
                    scopedSlots:{customRender:'date'}
                }],
                data:[{
                    key:'1',
                    id:'600615',
                    name:'Jhon Brown',
                    depname:'肠镜',
                    price:200,
                    amount:1,
                    date:'2019-05-25 15:30:26',
                },{
                    key:'2',
                    id:'600615',
                    name:'Jhon Brown',
                    depname:'肠镜',
                    price:200,
                    amount:1,
                    date:'2019-05-25 15:30:26'
                }

                ]

            }
        },
        components: {
            ARow,
            AFormItem,
        },
        methods:{
            onSearch(value){
                alert(value)
            },
            disabledStartDate (startValue) {
                const endValue = this.endValue;
                if (!startValue || !endValue) {
                    return false;
                }
                return startValue.valueOf() > endValue.valueOf();
            },
            disabledEndDate (endValue) {
                const startValue = this.startValue;
                if (!endValue || !startValue) {
                    return false;
                }
                return startValue.valueOf() >= endValue.valueOf();
            },
            handleStartOpenChange (open) {
                if (!open) {
                    this.endOpen = true;
                }
            },
            handleEndOpenChange (open) {
                this.endOpen = open;
            },
        }

    }
</script>

<style scoped>
.SearchInfo{
    margin-top: 40px;
    margin-bottom: 20px;
}
</style>