<template>
    <a-row type="flex" align="middle" justify="center" class="Inspection">
        <a-col span="20">
            <a-card hoverable title="医技检查" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                <p style="font-size: 20px">患者查询</p>
                <a-row type="flex" align="top" justify="start" style="margin: 5px 0 10px 0;">
                    <a-col>
                        <a-form layout="inline">
                            <a-form-item label="病历号">
                                <a-input-search placeholder="病历号" @search="onSearch" enterButton v-model="id"></a-input-search>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
                <p style="font-size: 20px">患者信息</p>
                <a-form :form="form" layout="inline">
                    <a-form-item label="姓名">
                        <a-input v-model="username" placeholder="姓名">{{username}}</a-input>
                    </a-form-item>
                    <a-form-item label="身份证号">
                        <a-input v-model="userid" placeholder="身份证号">{{userid}}</a-input>
                    </a-form-item>
                    <a-form-item label="家庭住址">
                        <a-textarea v-model="address" placeholder="家庭住址" autosize style="width:300px">{{address}}</a-textarea>
                    </a-form-item>
                </a-form>
                <p style="font-size: 20px">项目明细</p>
                <a-table :columns="columns"  :dataSource="data">
                    <a slot="name" slot-scope="text" href="javascript:;" @click="showmodal">{{text}}</a>
                    <span slot="action" slot-scope="text, record">
                       <a-upload name="file" :multiple="true" action="http://www.mocky.io/v2/5cc8019d300000980a055e76" :headers="headers" @change="handleChange">
                           <a-button>结果录入</a-button>
                       </a-upload>
                    </span>
                </a-table>
                <a-modal title="项目信息确认" v-model="visible" @ok="handleok" okText="执行确认" cancelText="取消执行">
                    <p>病历号：{{id}}</p>
                    <p>姓名：{{username}}</p>
                    <p>项目名称: {{data[0].depname}}</p>
                    <p>状态: {{data[0].state}}</p>
                </a-modal>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>
    import ARow from "ant-design-vue/es/grid/Row";
    import AFormItem from "ant-design-vue/es/form/FormItem";
    import ATextarea from "ant-design-vue/es/input/TextArea";
    export default {
        name:'inspection',
        data(){
            return{
                form:this.$form.createForm(this),
                visible: false,
                id:'',
                username:'',
                userid:'',
                address:'',
                headers:{
                    authorization:'authorization-text',
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
                },{
                    title:'状态',
                    dataIndex:'state',
                    key:'state',
                    scopedSlots:{customRender:'state'}
                },{
                    title:'执行科室',
                    dataIndex:'department',
                    key:'departement',
                    scopedSlots:{customRender:'department'}
                },{
                    title:'操作',
                    dataIndex:'action',
                    key:'action',
                    scopedSlots:{customRender:'action'}
                }],
                data:[{
                    key:'1',
                    id:'600615',
                    name:'Jhon Brown',
                    depname:'肠镜',
                    price:200,
                    amount:1,
                    date:'2019-05-25 15:30:26',
                    state:'已开立',
                    department:'肠胃科',
                }]


            }
        },
        components: {
            ARow,
            AFormItem,
            ATextarea
        },
        methods:{
            showmodal(){
                this.visible = true
            },
            handleok(event){
                this.visible = false
            },
            handleChange(info){
                if (info.file.status === 'done'){
                    this.$message.success(`${info.file.name} file upload successfully`);
                }
                else if (info.file.status === 'error'){
                    this.$message.error(`${info.file.name} file upload failed`)
                }
            },
            onSearch(value){

            }
        }
    }
</script>

<style scoped>
.Inspection{
    margin-top: 40px;
    margin-bottom: 20px;
}
</style>