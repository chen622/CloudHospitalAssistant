<template>
    <a-row type="flex" justify="center" align="middle" class="constant">
        <a-col span="20">
            <a-card hoverable title="常量管理" :headStyle="{fontSize:'30px'}" >
                <a-row type="flex" justify="space-around" align="top" >
                    <a-col span="5">
                        <a-input-search placeholder="输入信息" @search="onSearch" enterButton></a-input-search>
                    </a-col>
                    <a-col span="5">
                        <a-button type="primary" @click="showCreate"><a-icon type="plus" />新建</a-button>
                    </a-col>
                </a-row>
                <br/>
                <a-table :columns="columns" :dataSource="data" rowKey="id" style="width: 1000px; margin-left: 100px">
                    <template v-for="col in ['id','name','type']" :slot="col" slot-scope="text, record">
                        <div :key="col">
                            <a-input
                                    v-if="record.editable"
                                    style="margin: -5px 0"
                                    :value="text"
                                    @change="e => handleChange(e.target.value, record.id, col)"/>
                            <template v-else>{{text}}</template>
                        </div>
                    </template>
                    <span slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                       <span v-if="record.editable">
                          <a-popconfirm title='确定保存修改吗?' @confirm="save(record)">
                              <a>保存</a>
                          </a-popconfirm>
                            <a-divider type="vertical"/>
                           <a @click="() => cancel(record.id)">取消</a>
                       </span>
                       <span v-else>
                           <a @click="() => edit(record.id)">修改</a>
                        </span>
                <a-divider type="vertical"  v-if="!record.editable"/>
                <a-popconfirm v-if="data.length" title="Sure to delete?" @confirm="() => onDelete(record.id )" ><a href="javascript:;" v-if="!record.editable">删除</a></a-popconfirm>
                </div>
              </span>
                    <span slot="info" slot-scope="text,record">
                    <a-button type="primary" @click="getInfo(record)">详情</a-button>
                </span>
                </a-table>

                <a-table :columns="columns" :dataSource="data3" rowKey="id" v-if="seen" style="width: 1000px; margin-left: 100px">
                    <template v-for="col in ['id','name','type']" :slot="col" slot-scope="text, record">
                        <div :key="col">
                            <a-input
                                    v-if="record.editable"
                                    style="width: 100%"
                                    :value="text"
                                    @change="e => handleChange(e.target.value, record.id, col)"/>
                            <template v-else>{{text}}</template>
                        </div>
                    </template>
                    <span slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                       <span v-if="record.editable">
                            <a @click="() => save(record)">保存</a>
                           <a-divider type="vertical"/>
                          <a-popconfirm title='Sure to cancel?' @confirm="() => cancel(record.id)">
                              <a>取消</a>
                           </a-popconfirm>
                       </span>
                       <span v-else>
                           <a @click="() => edit(record.id)">修改</a>
                        </span>
                <a-divider type="vertical"  v-if="!record.editable"/>
                <a-popconfirm v-if="data.length" title="Sure to delete?" @confirm="() => onDelete(record.id)" ><a href="javascript:;" v-if="!record.editable">删除</a></a-popconfirm>
                </div>
              </span>
                </a-table>
                <template>
                    <a-modal
                            title="插入常量"
                            :visible="newConstant.isCreating"
                            @ok="handleAdd"
                            :confirmLoading="confirmLoading"
                            @cancel="newConstant.isCreating = false"
                    >
                        <a-form :form="newConstant.item">
                            <a-form-item label="常量id">
                                <a-input v-decorator="['id',{rules: rules.id}]" placeholder="请输入常量id"></a-input>
                            </a-form-item>
                            <a-form-item label="常量名称">
                                <a-input v-decorator="['name',{rules: rules.name}]" placeholder="请输入常量名称"></a-input>
                            </a-form-item>
                            <a-form-item label="常量类型">
                                <a-input v-decorator="['type',{rules: rules.type}]" placeholder="请输入常量类型"></a-input>
                            </a-form-item>
                        </a-form>
                    </a-modal>
                </template>
            </a-card>
        </a-col>
    </a-row>
</template>

<script>
    const data=[]
    const data3=[]
    export default {
        name:'constant',
        data(){
            return{
                seen:false,
                confirmLoading: false,
                ModalText: 'Content of the modal',
                newConstant:{
                    item: null,
                    isCreating: false,
                    createLoading:false
                },
                rules:{
                  id:[{required: true, message:'请输入常量id',trigger:'blur'}],
                  name:[{required: true, message:'请输入常量名称',trigger:'blur'}],
                  type:[{required: true, message:'请输入常量类型',trigger:'blur'}]
                },
                columns:[{
                    title:'常量',
                    dataIndex:'id',
                    scopedSlots:{customRender:'id'}
                },{
                    title:'名称',
                    dataIndex:'name',
                    scopedSlots:{customRender:'name'}
                },{
                    title:'类型',
                    dataIndex:'type',
                    scopedSlots:{customRender:'type'}
                },{
                    title:'操作',
                    dataIndex:'action',
                    scopedSlots:{customRender:'action'}
                },{
                    title:'',
                    dataIndex:'info',
                    scopedSlots:{customRender:'info'}
                }],
                data,
                data3,
                cacheData:data.map(item => ({ ...item }))

            }
        },
        mounted:function(){
         this.getAll()
        },

        methods:{
            getAll(){
              let that = this
              that.$api.get("/constant_variable/getAll",null,
              res=>{
                  if (res.code === "100"){
                      that.data = res.data
                  }
                  else {
                      that.$message.error(res)
                  }
              },res=>{
                  that.$message.error(res)
                  })
            },
            onSearch(value){
                let that = this
                that.$api.get("/constant_variable/getType/" + value, null,
                res=>{
                    if (res.code === "100"){
                        that.data = res.data
                        for (let i = 0; i < that.data.length; i++){
                            that.data[i].type = value
                        }
                        that.cacheData = res.data.map(item => ({...item}))
                    }
                    else {
                        that.$message.error(res)
                    }
                }, res=>{
                    that.$message.error(res)
                    })
            },
            getInfo(record){
                let that = this
                that.seen = true
                that.$api.get("/constant_variable/getType/" + record.id ,null,
                res=>{
                    if (res.code === "100") {
                        that.data3 = res.data
                        for (let i =0 ; i < that.data.length; i++){
                            that.data3[i].type = record.id
                        }
                        that.cacheData = res.data.map(item => ({...item}))
                    }
                    else {
                        that.$message.error(res)
                    }
                }, res =>{
                    that.$message.error(res)
                    })
            },
            showCreate(){
                this.newConstant.isCreating = true
                this.newConstant.item = this.$form.createForm(this)
            },
            handleAdd(){
                this.confirmLoading = true
                let that = this
                this.newConstant.item.validateFields((err)=>{
                    if (!err) {
                        this.$api.post("/constant_variable/insert", this.newConstant.item.getFieldsValue(),
                            res=>{
                                if (res.code === "100"){
                                    that.$message.success("创建成功")
                                    that.newConstant.isCreating = false
                                }
                                else {
                                    that.$message.error(res.msg)
                                }
                                that.newConstant.isCreating = false
                            }, res=>{
                                that.$message.error(res.msg)
                                that.newConstant.isCreating = false
                            })
                    }
                })

            },
            onDelete (key) {
                let that = this
                this.$api.get("/constant_variable/delete/"+ key, null,
                res=>{
                    if (res.code === "100"){
                        that.$message.success("删除成功")
                    }
                    else {
                        that.$message.error(res.msg)
                    }
                },()=>{

                    })
            },
            handleChange (value, id, column) {
                const newData = [...this.data]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },
            edit (id) {
                const newData = [...this.data]
                const target = newData.filter(item => id === item.id)[0]
                if (target) {
                    target.editable = true
                    this.data = newData
                }
            },
            save (record) {
                let that = this
                this.$api.post("/constant_variable/modify/",record,
                res=>{
                    if (res.code === "100"){
                        that.$message.success("修改成功")
                    }
                    else {
                        that.$message.error(res.msg)
                    }
                },()=>{

                    })
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

        }

    }
</script>

<style scoped>

</style>