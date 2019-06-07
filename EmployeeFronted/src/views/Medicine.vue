<template>
    <a-row type="flex" align="middle" justify="center" class="info-medicine" style="width:1510px">
        <a-col span="20">
            <a-card hoveracble title="药品管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                <a-row type="flex" align="top" justify="space-between" style="margin:5px 0 10px 0;">
                    <a-col span="5" style="margin-left:20px;" >
                        <a-input-search placeholder="请输入药品助记码" @search="onSearchByCode" enterButton></a-input-search>
                    </a-col>
                    <a-col span="5"  >
                        <a-input-search placeholder="请输入药品名称" @search="onSearchByName" enterButton></a-input-search>
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
                <template v-for="coll in  ['code', 'name', 'standard','packageCompany','price','factory','spell']" :slot="coll" slot-scope="text, record">
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

                <template slot="formulationName" slot-scope="text,record">
                    <a-select
                    :defaultValue="text"
                    style="width:100px"
                     v-if="record.editable"
                    @change="e => formChange(e,record.id)"
                    >
                     <a-select-option v-for="d in formulation" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                     <template v-else >{{text}}</template>
                </template>

                <template slot="drugTypeName" slot-scope="text,record">
                    <a-select
                    :defaultValue="text"
                    style="width:100px"
                    v-if="record.editable"
                    @change="e => drugTypeChange(e,record.id)"
                    >
                     <a-select-option v-for="d in durgTypeList" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                     <template v-else >{{text}}</template>
                </template>

                <template slot="paymentType" slot-scope="text,record">
                    <a-select
                    :defaultValue="text"
                    style="width:100px"
                    v-if="record.editable"
                    @change="e => paymentTypeChange(e,record.id)"
                    >
                     <a-select-option v-for="d in durgTypeList" :key="d.id">{{d.name}}</a-select-option>
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
                     <a @click="() => deleteRow(record.id)" style="color:red">删除</a>
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
                paymentTypeMap:{},
                paymentTypeList:[],
                drugTypeMap:{},
                durgTypeList:[],
                formulationName:{},
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
                    dataIndex: 'formulationName',
                    key:'formulationName',
                    sorter:true,
                    scopedSlots:{customRender:'formulationName'}
                },{
                    title:'药品类型',
                    dataIndex: 'drugTypeName',
                    key:'drugTypeName',
                    sorter:true,
                    width:"8%",
                    scopedSlots:{customRender:'drugTypeName'}
                },{
                    title:'拼音',
                    dataIndex: 'spell',
                    key:'spell',
                    sorter:true,
                    scopedSlots:{customRender:'spell'}
                },{
                    title:'支付类型',
                    dataIndex: 'paymentType',
                    key:'paymentType',
                    sorter:true,
                    scopedSlots:{customRender:'paymentType'}
                },{
                    title:'操作',
                    key:'action',
                    dataIndex:'action',
                    width: '10%',
                    scopedSlots:{customRender:'action'}
                }],
                wholeData:[],
                data:[]

            }
        },
        components: {

        },
        computed:{

        },created(){
            this.getForm();
            this.getDrugType();
            this.getPaymentType();
            this.getData();
            
        },
        methods: {
            getData(){
                let that = this
                this.$api.get("/drug/getAllDrug", null,
                    res => {
                        if (res.code === "100") {
                           that.data=res.data        
                           for(let i=0;i<that.data.length;i++){          
                              that.data[i].formulationName=that.formulationName.get(that.data[i].formulation)
                              that.data[i].drugTypeName=that.drugTypeMap.get(that.data[i].drugType)
                              that.data[i].paymentType=that.paymentTypeMap.get(that.data[i].feeTypeId)               
                           }
                            that.wholeData=that.data
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
             getForm () {
                let that=this
                this.$api.get("/constant_variable/getForm", null,

                res => {
                let a=res.data
                that.formulation=a
                that.formulationName
                var tem=new Map()
                for(let i=0;i<a.length;i++){
                    tem.set(a[i].id,a[i].name) 
                }
                that.formulationName=tem
                }, res => {
                this.$message.error(res)
                })
            },getDrugType(){
                let that=this
                 this.$api.get("/drug/getAllDrugType", null,
                 res => {
                        if (res.code === "100") {
                           var map=new Map();
                           var name=res.data.name
                           var id=res.data.id
                           for(let i=0;i<name.length;i++){
                              that.durgTypeList.push({
                                  name:name[i],
                                  id:id[i]
                              })
                              map.set(id[i],name[i])
                           }
                           that.drugTypeMap=map
                        }
                    }, res => {
                        that.$message.error(res)
                })
            },getPaymentType(){
                let that=this
                this.$api.get("/payment_type/getAll", null,
                 res => {
                        if (res.code === "100") {
                           var map=new Map();
                           var name=res.data.name
                           var id=res.data.id
                           for(let i=0;i<name.length;i++){
                                that.paymentTypeList.push({
                                  name:name[i],
                                  id:id[i]
                              })
                              map.set(id[i],name[i])
                           }
                           that.paymentTypeMap=map
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
                var list=[{id:-1,code:null,name:null,delete:false,drugType:1103,drugTypeName:'西药',factory:null,feeTypeId:201,formulation:1401,formulationName:'散剂'
                ,packageCompany:null,price:0.0,spell:null,standard:null}]
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
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                 if (target) {
                    delete target.editable
                    that.data = newData

                    this.$api.post("/drug/modify", target,
                            res => {
                                if (res.code === "100") {
                                   
                                    that.$message.success("更新成功！")
                                } else {
                                   
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
                    // Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
                    delete target.editable
                    this.data = newData
                }
            },
            edit (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    this.data = newData
                }
            
            },formChange(value,key){   
                let i=0
                let name=this.formulationName.get(value)
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    target.formulationName=name
                    target.formulation=value
                    this.data = newData
                }
            },drugTypeChange(value,key){ 
                let i=0
                let name=this.drugTypeMap.get(value)
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    target.drugTypeName=name
                    target.drugType=value
                    this.data = newData
                }
            },paymentTypeChange(value,key){

            },deleteRow(key){
                let that=this
                 this.$api.post("/drug/delete/"+key, null,
                            res => {
                                  console.log(key)
                                if (res.code === "100") {                                  
                                    that.$message.success("删除成功！")
                                    const newData = [...that.data]
                                    this.data = newData.filter(item => key !== item.id)[0]
                                    this.wholeData=this.data
                                } else {
                                   
                                    that.$message.error(res.msg)
                                }
                            }, () => {
                            that.$message.error("网络异常！")
                         })
            },getName(value){
                return this.formulationName.get(value)
            },onSearchByName(value){
                   var tem = []
                   var i=0
                   for(;i<this.wholeData.length;i++){
                       if(this.wholeData[i].name.indexOf(value)>=0){
                           tem.push(this.wholeData[i])
                    }
                    this.data=tem   
                }
            },onSearchByCode(value){
                   var tem = []
                   var i=0
                   for(;i<this.wholeData.length;i++){
                       if(this.wholeData[i].code.indexOf(value)>=0){
                           tem.push(this.wholeData[i])
                    }
                    this.data=tem   
                }
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