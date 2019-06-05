<template>
    <a-row type="flex" align="middle" justify="center" class="info-search">
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
                <a-table :columns="columns" :dataSource="data">
                    <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                    <span slot="customTitle">姓名</span>
                    <span slot="action" slot-scope="text, record">
                <a href="javascript:;">退号</a>
                <a-divider type="vertical" />
                <a href="javascript:;">删除</a>
              </span>
                </a-table>
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
                    title: '身份证号',
                    dataIndex: 'IdNumber',
                    key: 'IdNumber',
                    scopedSlots:{customRender:'IdNumber'}
                }, {
                    title: '挂号日期',
                    key: 'date',
                    dataIndex: 'date',
                    scopedSlots: { customRender: 'date' },
                }, {
                    title: '挂号午别',
                    key: 'NoonBreak',
                    dataIndex:'NoonBreak',
                    scopedSlots: { customRender: 'NoonBreak' },
                },{
                    title:'看诊科室',
                    key:'office',
                    dataIndex:'office',
                    scopedSlots:{customRender:'offfice'}
                },{
                    title:'看诊状态',
                    key:'state',
                    dataIndex:'state',
                    scopedSlots:{customRender:'state'}
                },{
                    title:'操作',
                    key:'action',
                    dataIndex:'action',
                    scopedSlots:{customRender:'action'}
                }],
                data:[{
                    key:'1',
                    id:'1',
                    name:'john Brown',
                    IdNumber: 22222222,
                    date:'2019-5-6',
                    NoonBreak:'全天',
                    office:'咽喉科',
                    state:'确诊'
                }]
            }
        },
        components: {
            ARow,
            ACol,
            AFormItem
        },
        methods: {
            getInfo(){
                let that = this
                this.$api.get("",null,
                res=>{
                    if (res.code === ""){
                        that.data = res.data

                    }
                    else {
                        that.$message.error(res)
                    }
                })

            },
            onSearch(value){
                alert(value)
            },

        },
    };
</script>
<style>
.info-search{
    margin-top: 40px;
    margin-bottom: 20px;
}
</style>
