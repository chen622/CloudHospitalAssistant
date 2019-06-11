<template>
    <div>
        <a-row type="flex"  align="middle" justify="center" class="info-medicine" >
            <a-col span="23">
                <a-card hoveracble title="门诊财务管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                            
                    <a-table :columns="columns" :dataSource="paymentTypeList" :scroll="{ x: 1500 }"  bordered  :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChange}" >
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
            title="支付类型信息"
            width="500px"
            :visible="modalVisible"
            @ok="() => ok()"
            @cancel="() => cancel()"            
            >
            <a-form  :columns="columns" :dataSource="paymentTypeTemp" :form="form" style="width:500px" layout="inline">
                <a-form-item label="编码" >
                    <a-input  
                    style="width:300px"
                    v-decorator="[
                        'code',
                        {
                            initialValue: [this.paymentTypeTemp.code],
                            rules: [{ required: true, message: '请输入编码!' }],
                        }
                        ]" 
                    getFieldDecorator="('code',)"
                    />
                </a-form-item>

                <a-form-item label="名称" style="margin:5px 5px 0 0">
                     <a-input  
                     style="width:300px"
                      
                        v-decorator="[
                        'name',
                        {
                            initialValue: [this.paymentTypeTemp.name],
                            rules: [{ required: true, message: '请输入名称!' }],
                        }
                        ]"
                    />
                </a-form-item>

                
                <a-form-item label="大类">
                    <a-select
                      v-decorator="[
                        'type',
                        {
                            initialValue: [this.paymentTypeTemp.type],
                            rules: [{ required: true, message: '请输入大类类型!' }]}
                        ]" 
                        style="width:300px"
                        @change="e => formChange(e)"
                        >
                        <a-select-option v-for="d in typeList" :key="d.id">{{d.name}}</a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item label="是否删除">
                    <a-select
                      v-decorator="[
                        'type',
                        {
                            initialValue: [this.paymentTypeTemp.isDelete],
                            rules: [{ required: true, message: '是否删除？' }]}
                        ]" 
                        style="width:300px"
                        @change="e => formChange(e)"
                        >
                        <a-select-option key="1">是</a-select-option>
                        <a-select-option key="2">否</a-select-option>
                    </a-select>
                </a-form-item>

        

            </a-form>
                
            </a-modal>
        </div>
    
    
    </template>
    </div>
    
    
</template>


<script>


    export default {
        data() {
            return {
                selectedRowKeys:[],
                form:this.$form.createForm(this),
                paymentTypeTemp:[{id:-1,code:'0',name:'default',type:'default',isDelete:false}],
                showList: true,
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
                        title:'大类',
                        dataIndex: 'type',
                        key:'type',
                        sorter:true,
                        width: '150px',
                        scopedSlots:{customRender:'type'}
                    },{
                        title:'是否删除',
                        dataIndex: 'isDelete',
                        key:'isDelete',
                        sorter:true,
                        width: '150px',
                        scopedSlots:{customRender:'isDelete'}
                    },{
                        title:'操作',
                        key:'action',
                        dataIndex:'action',
                        width: '120px',
                        align:'middle',
                        fixed:'right',
                        scopedSlots:{customRender:'action'}
                        }],
                paymentTypeList:[],
                typeList:[],
                idKeyMap:{},
                nameKeyMap:{},
                modalVisible:false,
            }
        },created(){
            this.getPaymentList()
        },methods:{
            getPaymentList(){
                 let that=this
                 console.log('/////')
                this.$api.get("/payment_type/getAll", null,
                    res => {
                        if (res.code === "100") {
                               console.log('11111/')
                            console.log(res.data)
                            console.log(res.data.list.filter(item => {item.id<100}))
                            Object.assign(that.typeList,res.data.list.filter(item => {item.id<100})) 
                            Object.assign(that.paymentTypeList,res.data.list)
                        } else {
                               console.log(res.msg)
                            that.$message.error(res.msg)
                        }
                    }, res => {
                           console.log('333333/')
                        that.$message.error(res)
                })
            },edit(value){
                    const newData = [...this.paymentTypeList]
                    const target = newData.filter(item => key === item.id)[0]
                    if (target) {
                        target.editable = true
                        this.modalVisible=true

                    } 
            },ok(){
                
            },cancle(){
                this.modalVisible=false
            },onSelectChange(){

            }
        }

    }
</script>

<style scoped>


</style>