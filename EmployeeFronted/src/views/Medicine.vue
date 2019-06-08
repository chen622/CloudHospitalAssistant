<template>
    <div>
    <a-row type="flex"  align="middle" justify="center" class="info-medicine" >
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
                        <a-button @click="deleteAll" type="danger"><a-icon type="plus-circle"/>全部删除</a-button>
                    </a-col>
                </a-row>
            

            <a-table :columns="columns" :dataSource="data" :scroll="{ x: 1500 }"  bordered  :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChange}" >
                <template slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                    <span v-if="record.editable">
                    <a @click="() => saveRow(record.id)">保存</a>
                    <a-divider type="vertical" />
                    <!-- <a-popconfirm title='确定取消吗?' @confirm="() => cancel(record.id)"> -->
                    <a style="color:red"  @click="() => cancel(record.id)">取消</a>
                    <!-- </a-popconfirm> -->
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



    <template>
        
        <div id="components-modal-demo-position" style="width:700px">
            <a-modal
            title="药品信息"
            bodyStyle="top: 20px;width:450px"
            width="500px"
            :visible="modelVisiable"
            @ok="() => ok()"
            @cancel="() => cancel(this.drugTemp)"
            
            >
            <a-form  :columns="columns" :dataSource="drugTemp" :form="form" style="width:500px" layout="inline">
                <a-form-item label="药品编码" >
                    <a-input  
                    style="width:300px"
                    v-decorator="[
                        'code',
                        {
                            initialValue: [this.drugTemp.code],
                            rules: [{ required: true, message: '请输入药品编码!' }],
                        }
                        ]" 
                    getFieldDecorator="('code',)"
                    />
                </a-form-item>

                <a-form-item label="药品名称" style="margin:5px 5px 0 0">
                     <a-input  
                     style="width:300px"
                      
                        v-decorator="[
                        'name',
                        {
                            initialValue: [this.drugTemp.name],
                            rules: [{ required: true, message: '请输入药品名称!' }],
                        }
                        ]"
                    />
                </a-form-item>

                
                <a-form-item label="药品规格">
                     <a-input  
                    
                        style="width:300px"
                        v-decorator="[
                        'standard',
                        {
                            initialValue: [this.drugTemp.standard],
                            rules: [{ required: true, message: '请输入药品规格!' }],
                        }
                        ]"
                    />
                </a-form-item>

                <a-form-item label="药品单位">
                     <a-input  
                      
                        style="width:300px"
                        v-decorator="[
                        'packageCompany',
                        {
                            initialValue: [this.drugTemp.packageCompany],
                            rules: [{ required: true, message: '请输入药品单位!' }],
                        }
                        ]"
                    />
                </a-form-item>

                <a-form-item label="药品单价">
                     <a-input  
                        style="width:300px"
                        v-decorator="[
                        'price',
                        {
                            initialValue: this.drugTemp.price,
                            rules: [{ required: true, validator:checkPrice }],
                        }
                        ]"
                    />
                </a-form-item>

                <a-form-item label="生产厂家">
                     <a-input  
                       
                        style="width:300px"
                        v-decorator="[
                        'factory',
                        {
                            initialValue: [this.drugTemp.factory],
                            rules: [{ required: true, message: '请输入生产厂家!' }],
                        }
                        ]"
                    />
                </a-form-item>

                <a-form-item label="药品拼音">
                     <a-input  
                     
                        style="width:300px"
                        v-decorator="[
                        'spell',
                        {
                            initialValue: [this.drugTemp.spell],
                            rules: [{ required: true, message: '请输入拼音!' }],
                        }
                        ]"
                    />
                </a-form-item>

                <a-form-item label="药品剂型">
                      <a-select
                      v-decorator="[
                        'formulationName',
                        {
                            initialValue: [this.drugTemp.formulationName],
                            rules: [{ required: true, message: '请输入药品剂型!' }]}
                        ]" 
                        style="width:300px"
                        @change="e => formChange(e)"
                        >
                        <a-select-option v-for="d in formulation" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                </a-form-item>


                <a-form-item label="药品类型">
                      <a-select
                       v-decorator="[
                        'drugTypeName',
                        {
                            initialValue: [this.drugTemp.drugTypeName],
                            rules: [{ required: true, message: '请输入药品类型!' }]}
                        ]"
                        style="width:300px"
                        @change="e => drugTypeChange(e)"
                        >
                        <a-select-option v-for="d in durgTypeList" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item label="费用类型">
                      <a-select
                        v-decorator="[
                        'paymentType',
                        {
                            initialValue: [this.drugTemp.paymentType],
                            rules: [{ required: true, message: '请输入费用类型!' }]}
                        ]"
                        style="width:300px"
                        @change="e => paymentTypeChange(e)"
                        >
                         <a-select-option v-for="d in paymentTypeList" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                </a-form-item>

            </a-form>
                
            </a-modal>
        </div>
    
    
    </template>




    </div>
</template>


<script>
import { constants } from 'crypto';

    export default {
        data() {
          
            return {
                rowKeys:[],
                wholeData:[],
                count:0,
                form: this.$form.createForm(this),
                drugTemp:{
                   id:'',code:'',name:'',delete:false,drugType:'',drugTypeName:'',factory:'',feeTypeId:'',paymentType:'',formulation:1401,formulationName:''
                ,packageCompany:'',price:0.0,spell:'',standard:''},
                modelVisiable:false,
                paymentTypeMap:{},
                paymentTypeList:[],
                drugTypeMap:{},
                durgTypeList:[],
                formulationNameMap:{},
                formulation:[],
                columns:[{
                    title:'编码',
                    dataIndex: 'code',
                    key:'code',
                    sorter:true,
                    width: '150px',
                    scopedSlots:{
                        customRender:'code'}
                },{
                    title:'名称',
                    dataIndex: 'name',
                    key:'name',
                    sorter:true,
                    width: '200px',
                    scopedSlots:{customRender:'name'}
                },{
                    title:'规格',
                    dataIndex: 'standard',
                    key:'standard',
                    width: '150px',
                    scopedSlots:{customRender:'standard'}
                },{
                    title:'单位',
                    dataIndex: 'packageCompany',
                    key:'packageCompany',
                    width: '100px',
                    scopedSlots:{customRender:'packageCompany'}
                },{
                    title:'生产厂家',
                    dataIndex: 'factory',
                    key:'factory',
                    width: '300px',
                    scopedSlots:{customRender:'factory'}
                },{
                    title:'单价',
                    dataIndex: 'price',
                    key:'price',
                    width: '150px',
                    sorter:true,
                    scopedSlots:{customRender:'price'}
                },{
                    title:'剂型',
                    dataIndex: 'formulationName',
                    key:'formulationName',
                    width: '150px',
                    scopedSlots:{customRender:'formulationName'}
                },{
                    title:'药品类型',
                    dataIndex: 'drugTypeName',
                    key:'drugTypeName',
                    sorter:true,
                    width: '150px',
                    scopedSlots:{customRender:'drugTypeName'}
                },{
                    title:'拼音',
                    dataIndex: 'spell',
                     width: '150px',
                    key:'spell',
                    scopedSlots:{customRender:'spell'}
                },{
                    title:'支付类型',
                    dataIndex: 'paymentType',
                     width: '150px',
                    key:'paymentType',
                    scopedSlots:{customRender:'paymentType'}
                },{
                    title:'操作',
                    key:'action',
                    dataIndex:'action',
                    width: '120px',
                    align:'middle',
                    fixed:'right',
                    scopedSlots:{customRender:'action'}
                }],
                data:[]

            }
        },
        components: {

        },
        computed:{

        },created() {
            this.getData(); 
        },methods: {
            getData(){
                
                let that = this
                
                // this.getPaymentType();
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

                // this.getForm();
                this.$api.get("/constant_variable/getForm", null,

                    res => {
                    let a=res.data
                    that.formulation=a
                    that.formulationNameMap
                    var tem=new Map()
                    for(let i=0;i<a.length;i++){
                        tem.set(a[i].id,a[i].name) 
                    }
                    that.formulationNameMap=tem
                    }, res => {
                    this.$message.error(res)
                })


                 // this.getDrugType();
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


                
                this.$api.get("/drug/getAllDrug", null,
                    res => {
                        if (res.code === "100") {
                           that.data=res.data        
                           for(let i=0;i<that.data.length;i++){
                              that.data[i].key=that.data[i].id                           
                              that.data[i].formulationName=that.formulationNameMap.get(that.data[i].formulation)
                              that.data[i].drugTypeName=that.drugTypeMap.get(that.data[i].drugType)
                              that.data[i].paymentType=that.paymentTypeMap.get(that.data[i].feeTypeId)               
                           }
                        }
                    }, res => {
                        that.$message.error(res)
                    })
            },
             getForm () {
               
            },getDrugType(){
               
            },getPaymentType(){

            },
            onSearch(value){
                alert(value)
            },
            add(value){ 
                this.drugTemp={id:0,code:null,name:'',delete:false,drugType:1103,drugTypeName:'西药',factory:null,feeTypeId:13,paymentType:'西药费',formulation:1401,formulationName:'散剂'
                ,packageCompany:null,price:2.0,spell:null,standard:null}
                this.modelVisiable=true
                this.drugTemp.isCancel=false
                this.drugTemp.add=true
            },
            insert(value){
               
            },deleteAll(){
                if(this.rowKeys.length>0){
                    for(var i=0;i<this.rowKeys.length;i++){
                        console.log(this.rowKeys)
                        this.deleteRow(this.rowKeys[i])
                    }
                }
                this.rowKeys=[]
            },onSelectChange(rowKeys){
                this.rowKeys=rowKeys
            },
            handleChange (value, key, column) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },
            saveRow () {
                let target=this.drugTemp
                let that=this
                const newData = [...this.data]
                const target2 = newData.filter(item => target.id === item.id)[0]
                 if (target2) {
                    if(!target.isCancel){
                        this.$api.post("/drug/modify", target,
                        res => {
                            if (res.code === "100") {
                                Object.assign(target2,target)
                                delete target2.editable
                                that.data=newData         
                                that.$message.success("更新成功！")      
                            } else {
                                that.$message.error(res.msg)
                            }
                        }, () => {
                        that.$message.error("网络异常！")
                        })
                    }    
                }

            },
            cancel (key) {
               this.drugTemp.isCancel=true
               this.modelVisiable=false
               let that=this
                const newData = [...this.data]
                const a=newData.filter(item => key !== item.id)
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                        if(target.add){
                            that.data=a
                        } else{
                            // Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
                            delete target.editable
                            this.data = newData
                        }             
                    
                }
            },
            edit (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    this.data = newData  
                    this.setModal1Visible(true)
                    this.drugTemp=target
                     this.drugTemp.isCancel=false
                    this.preDrugTemp=target
                }     
            },formChange(value){       
                let name=this.formulationNameMap.get(value)
                 this.drugTemp.formulation=value
                this.drugTemp.formulationName=name
            },drugTypeChange(value){     
                let name=this.drugTypeMap.get(value)              
                this.drugTemp.drugTypeName=name
                this.drugTemp.drugType=value
            },paymentTypeChange(value){       
                let name=this.paymentTypeMap.get(value)   
                this.drugTemp.feeTypeId=value
                this.drugTemp.paymentType=name
            },codeChange(value,key){
                let i=0
                let name=this.drugTypeMap.get(value)
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    target.code=value
                    this.data = newData
                }
            },deleteRow(key){
                let that=this  
                 this.$api.post("/drug/delete/"+key, null,
                            res => {
                                if (res.code === "100") {
                                    const newData = [...that.data]
                                    const tem = newData.filter(item => key !== item.id)
                                    that.data=tem           
                                    that.$message.success("删除成功！")
                                } else {
                                    that.$message.error(res.msg)
                                }
                            }, () => {
                            that.$message.error("网络异常！")
                         })
            },getName(value){
                return this.formulationNameMap.get(value)
            },onSearchByName(value){
                if(this.wholeData.length==0){
                    this.wholeData=this.data
                }else{
                    this.data=this.wholeData
                }
                if(value){
                    var tem = []
                    var i=0
                    for(;i<this.data.length;i++){
                        if(this.data[i].name.indexOf(value)>=0){
                            tem.push(this.data[i])
                    }
                    this.data=tem  
                    }   
                }
            },onSearchByCode(value){
                if(this.wholeData.length==0){
                    this.wholeData=this.data
                }else{
                    this.data=this.wholeData
                }
                if(value){
                    var tem = []
                    var i=0
                    for(;i<this.data.length;i++){
                        if(this.data[i].code.indexOf(value)>=0){
                            tem.push(this.data[i])
                        }
                 
                    }
                    this.data=tem                  
                }
            },setModal1Visible(value){
                this.modelVisiable=value
            },ok(){                
                this.drugTemp.code=this.form.getFieldValue('code')
                this.drugTemp.name=this.form.getFieldValue('name')
                this.drugTemp.factory=this.form.getFieldValue('factory')
                this.drugTemp.packageCompany=this.form.getFieldValue('packageCompany')
                this.drugTemp.price=this.form.getFieldValue('price')
                this.drugTemp.standard=this.form.getFieldValue('standard')
                 this.drugTemp.spell=this.form.getFieldValue('spell')
                this.setModal1Visible(false)
                if(this.drugTemp.add){
                    this.$api.post("/drug/insert", this.drugTemp,
                    res => {
                        if (res.code === "100") {                                  
                            this.$message.success("插入成功！")
                            this.drugTemp.id=res.data
                            console.log(res.data)
                            this.data.unshift(this.drugTemp)             
                            if (this.data[0]) {
                                this.data[0].add=true
                                this.data[0].editable = true
                                console.log(this.data[0])
                            }
                        } else {
                            this.$message.error(res.msg)
                        }
                    }, () => {
                    this.$message.error("网络异常！")
                })
                delete this.drugTemp.add
                }
            },checkPrice(rule, value, callback){
                if (value.number > 0) {
                callback();
                return;
                }
                callback('单价应大于0!');
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