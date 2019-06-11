<template>
    <a-row type="flex" align="middle" justify="center" class="charge">
        <a-col span="20">
            <a-card hoverable title="收费" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding:'5px 0'}">
                <p style="font-size: 20px">病历信息查询:</p>
                <a-row type="flex" align="top" justify="start" style="margin: 5px 0 10px 0;">
                    <a-col>
                        <a-form layout="inline">
                            <a-form-item label="病历号">
                                <a-input-search placeholder="病历号" @search="onSearch" enterButton v-model="id"></a-input-search>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
                <p style="font-size: 20px">患者信息确认：</p>
                <a-form :form="form" layout="inline">
                    <a-form-item label="姓名">
                        <a-input v-model="username" placeholder="姓名">{{username}}</a-input>
                    </a-form-item>
                    <a-form-item label="身份证号">
                        <a-input v-model="userid" placeholder="身份证号">{{userid}}</a-input>
                    </a-form-item>
                    <a-form-item label="家庭住址">
                        <a-textarea v-model="address" placeholder="家庭住址" autosize style="width: 300px">{{address}}</a-textarea>
                    </a-form-item>
                </a-form>
                <p style="font-size: 20px">患者消费信息:</p>
                <template>
                    <a-table :columns="columns" :dataSource="data" :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
                        <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                        <span slot="customTitle">姓名</span>
                    </a-table>
                    <a-button type="primary">收费结算</a-button>
                </template>
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
                id:'',
                username:'',
                userid:'',
                address:'',
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
                this.selectedRowKeys = selectedRowKeys
            },
            onSearch(value){
                alert(value)

            },

        },
    };
</script>
<style>
    .charge{
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>
