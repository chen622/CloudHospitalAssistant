<template>
    <div>
    <a-row type="flex"  align="middle" justify="center" class="info-medicine" >
        <a-col span="23">
            <a-card hoveracble title="药房工作站" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                  <a-tabs>
                    <a-tab-pane tab="药品管理" key="1">
                        <a-row type="flex" align="top" justify="space-between" style="margin:5px 0 10px 0;text-align: center">
                            <a-col span="5" style="margin-left:20px;" >
                                <a-input-search placeholder="请输入药品助记码" @search="onSearchByCode" enterButton></a-input-search>
                            </a-col>
                            <a-col span="5"  >
                                <a-input-search placeholder="请输入药品名称" @search="onSearchByName" enterButton></a-input-search>
                            </a-col>
                            <a-col span="3" >
                                <a-button  @click="add" style="color:#1890FF"><a-icon type="edit" />新增药品</a-button>
                            </a-col>
                          
                            <a-col span="3"  >
                                <a-button @click="deleteAll" type="danger"><a-icon type="plus-circle"/>全部删除</a-button>
                            </a-col>

                              <a-col span="3" >
                                <a-button  @click="insert" type="primary"><a-icon type="plus-circle"/>导入药品</a-button>
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
                    </a-tab-pane>
                    
                    <a-tab-pane tab="发药管理" key="2">

                        <a-row type="flex" align="top" justify="space-around" class="info-card">
                            <a-col :span="showList?3:3" :xl="showList?6:2" style="width:20%;" >
                                <a-card v-if="showList" hoverable :body-style="{padding: '2px 0 0 0'}">
                                    <span slot="title" style="font-size: 22px">
                                        <span style="margin-left:35px">患者列表</span>
                                         <a-button @click="getPatient" type="primary" shape="circle" icon="reload"
                                                style="float:right;margin-top:12px" size="small"></a-button>
                                    </span>
                                   

                                    <a-collapse defaultActiveKey="1" :bordered="false">
                                    
                                        

                                        <a-col  style="text-align: center" >
                                            <a-range-picker style="width:95%;margin-top:10px"  v-model="time" >
                                            <template slot="dateRender" slot-scope="current">
                                                <div class="ant-calendar-date" :style="getCurrentStyle(current)">
                                                   {{current.date()}}
                                                </div>
                                            </template>
                                            </a-range-picker>
                                        </a-col>

                                         <a-col span="3" style="text-align: center;margin-top:6px" >
                                            <a-input-search style="text-align: center;width:95%" placeholder="患者病历id" @search="onSearchByPid" enterButton></a-input-search>
                                        </a-col>
                    
                                        <a-collapse-panel header="患者信息" key="1">
                                            <a-list itemLayout="horizontal" :dataSource="patients"
                                                    style="overflow: auto;height: 400px;margin-top:5px">
                                                <a-list-item slot="renderItem" slot-scope="item" @click="selectPatient(item)">
                                                    <a-list-item-meta>
                                                    <span slot="title"
                                                        style="font-size: 15px;line-height: 20px">{{item.realName}}</span>
                                                        <span slot="description" style="text-align:center">
                                                            <span style="width:33%;margin-left:10px">ID: {{item.id}}   </span>
                                                            <span style="width:33%;margin-right:10px">年龄: {{item.age}}岁  </span>
                                                            <span style="width:33%;margin-right:10px">性别: {{item.sex?'男':'女'}}</span>
                                                        </span>
                                                    </a-list-item-meta>
                                                </a-list-item>
                                            </a-list>
                                        </a-collapse-panel>

                                    </a-collapse>
                                </a-card>
                                <a-affix v-else :offsetTop="50">
                                    <a-button type="primary" @click="showList = true">患者列表</a-button>
                                </a-affix>
                            </a-col>

                            <a-col :span="showList?16:21" :xl="showList?30:30" style="width:78%;padding:0 0 0 0 ; margin:0 0 0 0">
                                <a-card :body-style="{padding: 0}">
                                    <span slot="title" style="font-size: 22px" >
                                      


                                    <a-form :layout="formLayout" :form="form2" style="text-align: center"  >
                                        
                                        <a-form-item>
                                            <a-icon type="user" />
                                        </a-form-item>


                                        <a-form-item
                                            label="姓名"
                                            :label-col="formItemLayout.labelCol"
                                            :wrapper-col="formItemLayout.wrapperCol"
                                        >
                                            <a-button type="dashed" disabled style="color:black;width:100px;margin-right:20px">{{this.patient.realName}}</a-button> 
                                        </a-form-item>


                                        <a-form-item
                                            label="性别"
                                            :label-col="formItemLayout.labelCol"
                                            :wrapper-col="formItemLayout.wrapperCol"
                                        >
                                           <a-button type="dashed" disabled style="color:black;width:100px;margin-right:20px">{{this.patient.sex?'男':'女'}}</a-button> 
                                        </a-form-item>

                                         <a-form-item
                                            label="年龄"
                                            :label-col="formItemLayout.labelCol"
                                            :wrapper-col="formItemLayout.wrapperCol"
                                        >
                                           <a-button type="dashed" disabled style="color:black;width:100px;margin-right:20px">{{this.patient.age}}</a-button> 
                                        </a-form-item>

                                         <a-form-item
                                            label="ID"
                                            :label-col="formItemLayout.labelCol"
                                            :wrapper-col="formItemLayout.wrapperCol"
                                        >
                                          
                                            <a-button type="dashed" disabled style="color:black;width:100px;margin-right:20px">{{this.patient.id}}</a-button> 
                                        </a-form-item>

                                        <a-form-item>
                                             
                                            <a-button @click="allSend"  style="width:100px;margin-left:120px;color:#1890FF">全部取药</a-button>                          
                                            
                                        </a-form-item>

                                        <a-form-item>
                                            <a-button @click="allRetreat"  style="width:100px"  type="danger" >全部退药</a-button>            
                                            
                                        </a-form-item>
                                       
                                    </a-form>

                                    
                                    <a-divider style="margin-top:20px;font-size:20px;">取药信息</a-divider>
                                    
                                    <a-table :columns="paymentColumns" :dataSource="paymentData" :scroll="{ x: 1200 }"    :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChangeSendDrug}" >
                                        
                                        <template slot="state" slot-scope="text">                                            
                                            <a-tag color="orange" style="font-size:15px" v-if="text=='未取药'">{{text}}</a-tag>
                                        </template>

                                        <template slot="isFrozen" slot-scope="text">
                                            <template v-if="text==true">
                                                <a-tag color="blue" style="font-size:15px">已冻结</a-tag>
                                            </template>
                                            <template v-else><a-tag color="green" style="font-size:15px">未冻结</a-tag></template>
                                        </template>

                                        <template slot="actionc" slot-scope="text, record">
                                            <div class='editable-row-operations'>
                                                <a @click="() => sendDrug(record)">发药</a>                                  
                                            </div>
                                        </template>

                                        <template slot="create_time" slot-scope="text">
                                            <span name>{{text| formatDate}}</span>
                                        </template>
                           
                                    </a-table>   

                                     <a-divider style="margin-top:20px;font-size:20px;">退药信息</a-divider>
                                    
                                    
                                    
                                    
                                    
                                    
                                    <a-table :columns="paymentColumns" :dataSource="returnData"  :scroll="{ x: 1200 }"   :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChangeRetreatDrug}" >
                                        
                                        <template slot="state" slot-scope="text">
                                            
                                            <a-tag color="green" style="font-size:15px" v-if="text=='已取药'">{{text}}</a-tag>
                                            <a-tag color="purple" style="font-size:15px" v-if="text=='曾退药'">{{text}}</a-tag>
                                            <a-tag color="red" style="font-size:15px" v-if="text=='药全退'">{{text}}</a-tag>
                                        </template>
                                        
                                        <template slot="actionc" slot-scope="text, record">
                                            <div class='editable-row-operations'>                             
                                                 <a @click="() => returnDrug(record)" style="color:red">退药</a>                                             
                                            </div>
                                        </template>

                                        <template slot="create_time" slot-scope="text">
                                            <span>{{text| formatDate}}</span>
                                        </template>

                                        <template slot="isFrozen" slot-scope="text">
                                         
                                            <template v-if="text==true">
                                                <a-tag color="blue" style="font-size:15px">已冻结</a-tag>
                                            </template>
                                            <template v-else><a-tag color="green" style="font-size:15px">未冻结</a-tag></template>
                                                
                                            
                                        </template>

                           
                                    </a-table>         
      

                                                             
                                    </span>
                                    
                                   
                                </a-card>
                            </a-col>
                        </a-row>

                    </a-tab-pane>
                    
                </a-tabs>
       
            </a-card>
        </a-col>
    </a-row>



    <template>
        
        <div id="components-modal-demo-position" style="width:700px">
            <a-modal
            title="药品信息"
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

    <template>

        <a-modal
            title=""

            style="text-align:center"
            width="500px"
            :visible="visible"
            @ok="() => okReturn()"
            @cancel="() => cancelReturn()"            
            >

            <a-form  :columns="columns"  :form="form2" style="width:400px;text-align:center" layout="inline">

                <a-form-item label="请输入退药数量" style="margin-left:35px;text-align:center" >
                    <a-input  
                    style="width:100px;text-align:center"
                    v-decorator="[
                        'quantity',
                        {
                            initialValue: [this.quantity],
                             rules: [{ required: true, validator:'请输入数字' }],
                        }
                        ]" 
                    getFieldDecorator="('quantity',)"
                    />
                </a-form-item>

                <a-form-item style="text-align:center">
                    <a-button style="width:80px;text-align:center" type="dashed" disabled block >上限({{this.restDrug}})</a-button>
                </a-form-item>

            </a-form>

        </a-modal>
    </template>


    </div>
</template>


<script>
import { constants } from 'crypto';
import { Promise, resolve, reject } from 'q';
 import moment from 'moment'//导入文件 

    export default {
        data() {
          
        return {
            selectedRowKeys:[],
            sendRowKeys:[],
            retreatRowKeys:[],
            value:[],
            restId:0,
            restDrug:0,
            time:null,
            visible:false,
            quantity:1,
            formLayout: 'inline',
            load: {
                patient: true
            },
            patient: { 
            },     
            currentPatient: {
                name: "当前患者"
            },
            rules: {},
            record: null,
            showList: true,
                rowKeys:[],
                wholeData:[],
                count:0,
                form: this.$form.createForm(this),
                form2: this.$form.createForm(this),
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
                paymentColumns:[
                    {
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
                        width: '200px',
                        scopedSlots:{customRender:'name'}
                    },{
                        title:'状态',
                        dataIndex: 'state',
                        key:'state',
                        width: '100px',
                        scopedSlots:{customRender:'state'}
                    },{
                        title:'是否冻结',
                        dataIndex:'isFrozen',
                        key:'isFrozen',
                        width:'120px',
                        scopedSlots:{customRender:'isFrozen'}
                    },{
                        title:'药品数量',
                        dataIndex: 'quantity',
                        key:'quantity',
                        width: '150px',
                        scopedSlots:{customRender:'quantity'}
                    },{
                        title:'已退数量',
                        dataIndex: 'return',
                        key:'return',
                        width: '150px',
                        scopedSlots:{customRender:'return'}
                    },{
                        title:'单价',
                        dataIndex: 'unit_price',
                        key:'unit_price',
                        width: '150px',
                        scopedSlots:{customRender:'unit_price'}
                    },{
                        title:'总价',
                        dataIndex: 'totalPrice',
                        key:'totalPrice',
                        width: '150px',
                        scopedSlots:{customRender:'totalPrice'}
                    },{
                        title:'支付时间',
                        dataIndex: 'create_time',
                        key:'create_time',
                        width: '150px',
                        scopedSlots:{customRender:'create_time'}
                    },{
                        title:'操作',
                        key:'actionc',
                        dataIndex:'actionc',
                        width: '1px',
                        align:'middle',
                        fixed:'right',
                        scopedSlots:{customRender:'actionc'}
                    }
                ],
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
                data:[],
                paymentData:[],
                returnData:[],
                showList:true,
                patients:[],
            }
        },
        
        computed:{
            formItemLayout () {
                const { formLayout } = this;
                return formLayout === 'horizontal' ? {
                    labelCol: { span: 4 },
                    wrapperCol: { span: 14 },
                } : {};
            },
            buttonItemLayout () {
                const { formLayout } = this;
                return formLayout === 'horizontal' ? {
                    wrapperCol: { span: 14, offset: 4 },
                } : {};
            },
        },created() {
            this.getData(); 
        },filters: {
            formatDate: function (value) {
                let date = new Date(value);
                let y = date.getFullYear();
                let MM = date.getMonth() + 1;
                MM = MM < 10 ? ('0' + MM) : MM;
                let d = date.getDate();
                d = d < 10 ? ('0' + d) : d;
                let h = date.getHours();
                h = h < 10 ? ('0' + h) : h;
                let m = date.getMinutes();
                m = m < 10 ? ('0' + m) : m;
                let s = date.getSeconds();
                s = s < 10 ? ('0' + s) : s;
                return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
            }
        },methods: {
            async  getData(){
                
                let that = this
                
                // this.getPaymentType();
                await this.getPaymentType()
     
                this.paymentTypeMap=that.paymentTypeMap

                // this.getForm();
                await this.getForm()

                 // this.getDrugType();
                await this.getDrugType()


            },getForm () {
                let that=this
               var p=new Promise((resolve,reject) => {
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
               })
               
 
            },getDrugType(){
               let that=this
                var p=new Promise((resolve,reject) => {
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
                            resolve('')
                            }
                        }, res => {
                            that.$message.error(res)
                    })
                  })
                  .then(r=>{
                      this.getAllDrug();
                  })

                 this.getAllDrug();
                  
            }, getPaymentType(){
                let that=this
                var p=new Promise((resolve,reject) => {
                this.$api.get("/payment_type/getAll", null,
                 res => {
                        if (res.code === "100") {
                           var map=new Map();
                           var name=res.data
                           for(let i=0;i<name.length;i++){
                                that.paymentTypeList.push({
                                  name:name[i].name,
                                  id:name[i].id,
                                  code:name[i].code,
                                  type:name[i].type,
                                  isDelete:name[i].delete,
                                  delete:name[i].delete
                              })
                              map.set(name[i].id,name[i].name)
                           }
                           that.paymentTypeMap=map
                        }
                    }, res => {
                        that.$message.error(res)
                  })

                })
            },getAllDrug(){
                let that=this
                var p=new Promise((resolve,reject) => {
                this.$api.get("/drug/getAllDrugWithout", null,
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
                  })
            
            },onSearch(value){
                alert(value)
            },add(value){ 
                this.drugTemp={id:0,code:null,name:'',delete:false,drugType:1103,drugTypeName:'西药',factory:null,feeTypeId:13,paymentType:'西药费',formulation:1401,formulationName:'散剂'
                ,packageCompany:null,price:2.0,spell:null,standard:null}
                this.modelVisiable=true
                this.drugTemp.isCancel=false
                this.drugTemp.add=true
            },insert(value){  
            },deleteAll(){
                if(this.rowKeys.length>0){
                    for(var i=0;i<this.rowKeys.length;i++){
                        this.deleteRow(this.rowKeys[i])
                    }
                }
                this.rowKeys=[]
            },onSelectChange(rowKeys){
                this.rowKeys=rowKeys
            },handleChange (value, key, column) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },saveRow () {
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

            },cancel (key) {
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
            },edit (key) {
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
             
                            this.data.unshift(this.drugTemp)             
                            if (this.data[0]) {
                                this.data[0].add=true
                                this.data[0].editable = true
                            
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
                callback('内容应大于0!');
            },selectPatient(item){ 
                var p=new Promise((resolve,reject) => {
                  
                Object.assign(this.patient,item) 
                   
                this.paymentData=[]
    
                this.form2.setFieldsValue()
                var notTake=[]
                
                Object.assign(notTake,item.notTake)
                for(var i=0;i<item.notTake.length;i++){ 
                    var prescription=item.notTake[i].prescription
                    this.paymentData.push({
                        key:item.notTake[i].id,
                         id:item.notTake[i].id,
                        itemId:item.notTake[i].itemId,
                        code:prescription.drug.code,       
                        name:prescription.drug.name,                  
                        quantity:prescription.amount,                 
                        return:0,                  
                        unit_price:item.notTake[i].unitPrice,                
                        totalPrice:(item.notTake[i].unitPrice * prescription.amount),
                        state:'未取药',
                        create_time: item.notTake[i].createTime ,
                        isFrozen:item.notTake[i].isFrozen,
                        drugId:prescription.drugId,
                    })
                }

                this.returnData=[]
                var taken=item.takenNotRetreat
                for(var i=0;i<taken.length;i++){ 
                    var prescription=taken[i].prescription
                    this.returnData.push({
                        key:taken[i].id,
                        id:taken[i].id,
                        itemId:taken[i].itemId,
                        code:prescription.drug.code,       
                        name:prescription.drug.name,                  
                        quantity:taken[i].quantity,                                   
                        unit_price:taken[i].unitPrice,                
                        totalPrice:(taken[i].unitPrice * prescription.amount),
                        state:'已取药',
                        create_time:taken[i].createTime,
                        return:0,
                        drugId:prescription.drugId,
                        isFrozen:taken[i].isFrozen,
                    })

                    
                }

                taken=item.happenRetreat

                for(var i=0;i<taken.length;i++){
                    var prescription=taken[i].payment.prescription
                    this.returnData.push({
                        key:taken[i].payment.id,
                        id:taken[i].payment.id,
                        itemId:taken[i].payment.itemId,
                        code:prescription.drug.code,       
                        name:prescription.drug.name,                  
                        quantity:taken[i].payment.quantity,                                  
                        unit_price:taken[i].payment.unitPrice,                
                        totalPrice:(taken[i].payment.unitPrice * prescription.amount),
                        state:'曾退药',
                        create_time:taken[i].payment.createTime,
                        return:taken[i].retreatQuantity,
                        drugId:prescription.drugId,
                        isFrozen:taken[i].payment.isFrozen,
                    })

                }

                
                taken=item.AllReturn
                for(var i=0;i<taken.length;i++){ 
                    var prescription=taken[i].prescription
                    this.returnData.push({
                        key:taken[i].id,
                         id:taken[i].id,
                        itemId:taken[i].itemId,
                        code:prescription.drug.code,       
                        name:prescription.drug.name,                  
                        quantity:taken[i].quantity,                 
                        return:taken[i].quantity,                  
                        unit_price:taken[i].unitPrice,                
                        totalPrice:(taken[i].unitPrice * prescription.amount),
                        state:'药全退',
                        create_time:taken[i].createTime,
                        drugId:prescription.drugId,
                        isFrozen:taken[i].isFrozen,
                    })
                }
                
            })
            },async getPatient(value){
                let that=this
                this.paymentData=[]
                this.returnData=[]
                that.time=that.value                
                await that.onSearchByPid(this.patient.id)
                var item=that.patients.filter(item => this.patient.id === item.id )[0]
                await that.selectPatient(item)
                if(value==1){
                    alert('发药成功')
                }
            },async sendDrug(record){
                var a={
                    paymentId:record.id,
                    drugId:record.drugId
                }
                // console.log(record.id)
                // console.log(record.drugId)
                let that=this
                this.$api.post("/drug/takeDrug/"+record.id+'/'+record.drugId,null,
                    res => {
                        if (res.code === "100") {
                            this.getPatient(1)
                        }else if(res.code=="512"){
                            alert('请稍后！')
                        }
                        else{
                            that.$message.error(res)
                        }
                    }, res => {
                        that.$message.error(res)
                })
            },returnDrug(payemny){
                if(!payemny.isFrozen && payemny.state.indexOf('药全退')<0){
                    this.visible=true
                    // console.log(this.restDrug+' '+payemny.quantity+' '+payemny.return)
                    this.restDrug=(payemny.quantity-payemny.return) 
                    this.restId=payemny.id 
                }
            },cancelReturn(){
                this.visible=false
            },okReturn(){
                this.visible=false
                var quantity1=this.form2.getFieldValue('quantity')[0]
                if(quantity1>0 && quantity1<=this.restDrug){
                    const newData=[...this.returnData]
                    const pay= newData.filter(item => this.restId === item.id)[0]
                    var m={
                        paymentId:this.restId,
                        drugId:pay.drugId,
                        quantity:quantity1
                    }

                    let that=this
                    this.$api.post("/drug/retreatDrug", m,
                            res => {
                                if (res.code === "100") {                    
                                    pay.return=pay.return-quantity1
                                    that.returnData=newData
                                }else{
                                    that.$message.error(res)
                                }
                            }, res => {
                                that.$message.error(res)
                    })  
                           
                }else{
                    alert('请输入合法数字！')
                }
            },onSearchByPid(value){
                var p=new Promise((resolve,reject) => {
                let that=this
                var start,end
                that.value=[]

                    if(this.time==null || this.time.length==0){
                        start=moment().format('YYYY-MM-DD')
                        end=moment().utc().format('YYYY-MM-DD')
                        that.value.push(moment())
                        that.value.push(moment())
                    }else{
                        that.value.push(that.time[0])
                        that.value.push(that.time[1])
                        start=this.time[0].utc().format('YYYY-MM-DD'),
                        end=this.time[1].utc().format('YYYY-MM-DD')
                    }

                    var m={
                    start:start,
                    end:end,
                    patientId:value
                    }

                    that.getOnePatient(m)
                })
            },getOnePatient(m){
                let that=this
                this.$api.post("/patient/getDrug", m,
                            res => {
                                if (res.code === "100") {
                                
                                var patient=res.data.notTake
                                this.patients=[]
                                this.patients.push(
                                    {
                                        id:patient.id,
                                        age:20,
                                        realName:patient.realName,
                                        sex:0,
                                        notTake:patient.paymentList,
                                        happenRetreat:res.data.token.happenRetreat,
                                        takenNotRetreat:res.data.token.takenNotRetreat,
                                        AllReturn:res.data.token.AllReturn,
                                    }
                                
                                )
                                }else{
                                    that.$message.error(res)
                                }
                            }, res => {
                                that.$message.error(res)
                })
            },getCurrentStyle (current, today) {
                const style = {}
                if (current.date() === 1) {
                    style.border = '1px solid #1890ff'
                    style.borderRadius = '50%'
                }
                return style
            },onSelectChangeSendDrug(rowKeys){
                Object.assign(this.sendRowKeys,rowKeys)
            },allSend(){
                var rowKeys=this.sendRowKeys
                var notTake=this.paymentData
                for(var i=0;i<rowKeys.length;i++){
                    this.sendDrug(notTake.filter(item => rowKeys[i] === item.key)[0])
                }
            },onSelectChangeRetreatDrug(rowKeys){
                Object.assign(this.retreatRowKeys,rowKeys)
            },allRetreat(){
                
                var rowKeys=this.retreatRowKeys
                
                const retreat=this.returnData
                // console.log(rowKeys)
                // console.log(retreat)
                for(var i=0;i<rowKeys.length;i++){
                
                   var temp=retreat.filter(item => rowKeys[i] === item.key)
                    if(temp.length>0){   
                        if(temp[0].state.indexOf('药全退')<0){
                            this.retreatAll(temp[0])
                        }
                    }else{               
                        alert('没有编号为：'+rowKeys[i]+'的支付数据！')      
                    }
                }
            },retreatAll(temp){
                            let that=this
                         var m={
                                paymentId:temp.id,
                                drugId:temp.drugId,
                                quantity:(temp.quantity-temp.return)
                            }   

                        this.$api.post("/drug/retreatDrug", m,
                                res => {
                                    if (res.code === "100") {                    
                                    }else{
                                        alert(res.msg)
                                        that.$message.error(res)
                                    }
                                }, res => {
                                    that.$message.error(res)
                        }) 
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