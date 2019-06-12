<template>
    <div>
    <a-row type="flex"  align="middle" justify="center" class="info-medicine" >
        <a-col span="23">
            <a-card hoveracble title="药房工作站" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                 

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
                                       
                                    </a-form>

                                    
                                    <a-divider style="margin-top:20px;font-size:20px;">取药信息</a-divider>
                                    
                                    <a-table :columns="paymentColumns" :dataSource="paymentData" :scroll="{ x: 1200 }"    :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChangeSendDrug}" >
                                      
                           
                                    </a-table>   

                                     <a-divider style="margin-top:20px;font-size:20px;">退药信息</a-divider>
                                    
                                    
                                    
                                    
                                    
                                    
                                    <a-table :columns="paymentColumns" :dataSource="returnData"  :scroll="{ x: 1200 }"   :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChangeRetreatDrug}" >
                                        
                                       

                           
                                    </a-table>         
      

                                                             
                                    </span>
                                    
                                   
                                </a-card>
                            </a-col>
                        </a-row>
                
       
            </a-card>
        </a-col>
    </a-row>


    </div>
</template>


<script>
import { constants } from 'crypto';
import { Promise, resolve, reject } from 'q';
 import moment from 'moment'//导入文件 
import { stat } from 'fs';

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
            }
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
            },getPaymentType(){
                let that=this
                var p=new Promise((resolve,reject) => {
                this.$api.get("/payment_type/getAll", null,
                res => {
                        if (res.code === "100") {
                          var map=new Map();
                           var name=res.data
                           var id=[]
                           for(let i=0;i<name.length;i++){
                                that.paymentTypeList.push({
                                    name:name[i].name,
                                    id:name[i].id
                                })
                                map.set(name[i].id,name[i].name) 
                           }
                           that.paymentTypeMap=map
     
                        }
                    }, res => {
                        that.$message.error(res)
                  })

                })
            },selectPatient(item){ 
                
            },async getPatient(value){     
                await this.onSearchByPid(this.patient.id)
                var item=this.patients.filter(item => this.patient.id === item.id )[0]
                await this.selectPatient(item)
                
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
                     console.log(start)
                     console.log(this.getTimeStamp(start))
                    var m={
                        start:this.getTimeStamp(start),
                        end:this.getTimeStamp(end),
                        patientId:value
                    }
                    console.log(m)
                    that.getOnePatient(m)
                })
            },getOnePatient(m){
                let that=this
                this.$api.post("/payment/getForStatistics", m,
                    res => {
                        if (res.code === "100") {
                            
                            console.log(res.data)
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
            },onSelectChangeRetreatDrug(rowKeys){
                Object.assign(this.retreatRowKeys,rowKeys)
            },getPayments(){

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