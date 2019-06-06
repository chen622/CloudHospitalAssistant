<template>
    <a-row type="flex" align="middle" justify="center" class="charge">
        <a-col span="20">
            <a-card hoveracble title="查询" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding:'5px 0'}">
                <a-row type="flex" align="top" justify="start" style="margin: 5px 0 10px 0;">
                    <a-col span="3">
                        <p style="font-size: 20px">病历信息查询:</p>
                    </a-col>
                    <a-col span="5">
                        <a-input-search placeholder="病历号" @search="onSearch" enterButton></a-input-search>
                    </a-col>
                </a-row>
                <a-row type="flex" align="top" justify="space-between">
                    <a-col span="3">
                        <p style="font-size: 20px">患者信息确认：</p>
                    </a-col>
                    <a-col span="6">
                        <a-form :form="form" layout="inline">
                            <a-form-item label="姓名">
                                <a-input v-decorator="['username',{rules:[{required:false,message:''}]}]" placeholder="姓名"></a-input>
                            </a-form-item>
                        </a-form>
                    </a-col>
                    <a-col span="7">
                        <a-form :form="form" layout="inline" >
                            <a-form-item label="身份证号">
                                <a-input v-decorator="['username',{rules:[{required:false,message:''}]}]" placeholder="身份证号"></a-input>
                            </a-form-item>
                        </a-form>
                    </a-col>
                    <a-col span="8">
                        <a-form :form="form" layout="inline">
                            <a-form-item label="家庭住址" :label-col="{span:8}":wrapper-col="{span:15}">
                                <a-textarea v-decorator="['家庭住址',{rules:[{required:false,message:''}]}]" placeholder="家庭住址" autosize style="width: 300px"/>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
                <p style="font-size: 20px">患者消费信息:</p>
                <temple>
                    <a-table :columns="columns" :dataSource="data" :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
                        <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                        <span slot="customTitle">姓名</span>
                    </a-table>
                    <a-button type="primary">收费结算</a-button>
                </temple>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>
    import ARow from "ant-design-vue/es/grid/Row";
    import ACol from "ant-design-vue/es/grid/Col";
    import AFormItem from "ant-design-vue/es/form/FormItem";

    export default {
        data () {
            return {
                form:this.$form.createForm(this),
                columns : [{
                    title:'病历号',
                    dataIndex:'id',
                    key: 'id',
                    scopedSlots:{customRender:'id'}
                },{
                    title:'姓名',
                    dataIndex: 'name',
                    key: 'name',
                    scopedSlots: { customRender: 'name' },
                }, {
                    title: '项目名称',
                    dataIndex: 'Project',
                    key: 'Project',
                    scopedSlots:{customRender:'Project'}
                }, {
                    title: '单价',
                    key: 'price',
                    dataIndex: 'price',
                    scopedSlots: { customRender: 'price' },
                }, {
                    title: '数量',
                    key: 'Number',
                    dataIndex:'Number',
                    scopedSlots: { customRender: 'Number' },
                },{
                    title:'开立时间',
                    key:'date',
                    dataIndex:'date',
                    scopedSlots:{customRender:'date'}
                },{
                    title:'状态',
                    key:'state',
                    dataIndex:'state',
                    scopedSlots:{customRender:'state'}
                }],
                data:[{
                    key:'1',
                    id:'600615',
                    name:'john Brown',
                    Project: 'C型臂术中透视',
                    price:150,
                    Number:1,
                    date:'2019-04-01 16:12:05',
                    state:'已开立'
                },{
                    key:'2',
                    id:'600615',
                    name:'john Brown',
                    Project: '气脑造影',
                    price:80,
                    Number:1,
                    date:'2019-04-01 16:14:34',
                    state:'已开立'
                },{
                    key:'3',
                    id:'600615',
                    name:'john Brown',
                    Project: '肠镜',
                    price:200,
                    Number:1,
                    date:'2019-04-01 16:20:25',
                    state:'已开立'
                }],
                selectedRowKeys: [],
                loading:false,


            }
        },
        components: {
            ARow,
            ACol,
            AFormItem
        },
        computed: {


        },
        methods: {
            onSelectChange(selectedRowKeys){
                console.log('selectedRowKeys changed: ',selectedRowKeys);
                this.selectedRowKeys = selectedRowKeys
            }

        },
    };
</script>
<style>
    .charge{
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>
