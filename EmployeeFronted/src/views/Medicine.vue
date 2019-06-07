<template>
    <a-row type="flex" align="middle" justify="center" class="info-medicine" style="width:1500px">
        <a-col span="20">
            <a-card hoveracble title="药品管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                <a-row type="flex" align="top" justify="space-between" style="margin:5px 0 10px 0;">
                    <a-col span="5" style="margin-left:20px;" >
                        <a-input-search placeholder="请输入药品助记码" @search="onSearch" enterButton></a-input-search>
                    </a-col>
                    <a-col span="5"  >
                        <a-input-search placeholder="请输入药品名称" @search="onSearch" enterButton></a-input-search>
                    </a-col>
                    <a-col span="3" >
                        <a-button  @click="add" type="primary"><a-icon type="edit" />新增药品</a-button>
                    </a-col>
                    <a-col span="3" >
                        <a-button  @click="insert" type="primary"><a-icon type="plus-circle"/>导入药品</a-button>
                    </a-col>

                    <a-col span="3"  >
                        <a-button @click="saveAll" type="primary"><a-icon type="plus-circle"/>保存所有更改</a-button>
                    </a-col>
                </a-row>


            <a-table :columns="columns" :dataSource="data" bordered  :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChange}">
                <template v-for="coll in  ['code', 'name', 'standard','packageCompany','price','drugType','factory','spell']" :slot="coll" slot-scope="text, record">
                <div :key="coll">
                    <a-input
                    v-if="record.editable"
                    style="margin: -5px 0"
                    :value="text"
                    @change="e => handleChange(e.target.value, record.id, coll)"
                    />
                    <template v-else>{{text}}</template>
                </div>
                </template>
                
                <template slot="formulation" slot-scope="text,record">
                    <a-select
                    v-if="record.editable"
                    defaultVaule="散剂"
                    style="width:100px"
                    @change="e => formChange(e,record.key)"
                    >
                     <a-select-option v-for="d in formulation" :key="d.key">{{d.value}}</a-select-option>
                    </a-select>
                     <template v-else >{{text}}</template>
                </template>
               
                
                <template slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                    <span v-if="record.editable">
                    <a @click="() => saveRow(record.id)">保存</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title='确定取消吗?' @confirm="() => cancel(record.id)">
                        <a style="color:red">取消</a>
                    </a-popconfirm>
                    </span>
                    <span v-else>
                    <a @click="() => edit(record.id)">编辑</a>
                      <a-divider type="vertical" />
                     <a @click="() => delete(record.id)" style="color:red">删除</a>
                    </span>
                </div>
                </template>
            </a-table>

            </a-card>
        </a-col>
    </a-row>
</template>


<script>
import { constants } from 'crypto';

    export default {
        data() {
            return {
                saveformulation:[],
                formulation:[],
                columns:[{
                    title:'编码',
                    dataIndex: 'code',
                    key:'code',
                    sorter:true,
                    scopedSlots:{customRender:'id'}
                },{
                    title:'名称',
                    dataIndex: 'name',
                    key:'name',
                    sorter:true,
                    scopedSlots:{customRender:'name'}
                },{
                    title:'规格',
                    dataIndex: 'standard',
                    key:'standard',
                    sorter:true,
                    scopedSlots:{customRender:'standard'}
                },{
                    title:'单位',
                    dataIndex: 'packageCompany',
                    key:'packageCompany',
                    
                    sorter:true,
                    scopedSlots:{customRender:'packageCompany'}
                },{
                    title:'生产厂家',
                    dataIndex: 'factory',
                    key:'factory',
                    sorter:true,
                    scopedSlots:{customRender:'factory'}
                },{
                    title:'单价',
                    dataIndex: 'price',
                    key:'price',
                    sorter:true,
                    scopedSlots:{customRender:'price'}
                },{
                    title:'剂型',
                    dataIndex: 'formulation',
                    key:'formulation',
                    sorter:true,
                    scopedSlots:{customRender:'formulation'}
                },{
                    title:'药品类型',
                    dataIndex: 'drugType',
                    key:'drugType',
                    sorter:true,
                    scopedSlots:{customRender:'drugType'}
                },{
                    title:'拼音',
                    dataIndex: 'spell',
                    key:'spell',
                    sorter:true,
                    scopedSlots:{customRender:'spell'}
                },{
                    title:'操作',
                    key:'action',
                    dataIndex:'action',
                    width: '10%',
                    scopedSlots:{customRender:'action'}
                }],
                data:[]

            }
        },
        components: {

        },
        computed:{

        },created(){
            this.getData()
        },
        methods: {
            getData(){
                console.log("??")
                let that = this
                this.$api.get("/drug/getAllDrug", null,
                    res => {
                        if (res.code === "100") {
                           this.data=res.data
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
            onSearch(value){
                alert(value)
            },
            add(value){

            },
            insert(value){

            },
            handleChange (value, key, column) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },
            saveRow (key) {
                let that=this
                console.log(key)
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                console.log(target)
                 if (target) {
                    delete target.editable
                    this.data = newData
                    this.cacheData = newData.map(item => ({ ...item }))

                    this.$api.post("/drug/modify", target,
                            res => {
                                if (res.code === "100") {
                                    constants.log("成功")
                                    that.$message.success("更新成功！")
                                } else {
                                     console.log(res.msg)
                                    that.$message.error(res.msg)
                                }
                              
                            }, () => {
                            that.$message.error("网络异常！")
                        })

                }
                
            },
            cancel (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
                    delete target.editable
                    this.data = newData
                }
            }, 
             getForm () {
                this.$api.get("/constant_variable/getForm", null,
                
                res => {
                let a=res.data 
                let i=0
                for(i =0;i< a.name.length; i++){              
                this.formulation.push({
                    value:a.name[i],
                    key: a.id[i]
                    });
                }
                }, res => {
                this.$message.error(res)
                })
                },
            edit (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    this.data = newData
                }
                this.getForm();
            },formChange(value,key){
                console.log(key)
                let i=0
                let name=""
                for(;i<this.formulation.length;i++){
                    if(this.formulation[i].key==value){
                        name=this.formulation[i].value;
                        break;            
                    }
                }
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    target.editable = true
                    target.formulation=name
                    this.data = newData
                }
            },delete(key){

            }
        }
    }
    
</script>

<style scoped>
    .info-medicine{
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>