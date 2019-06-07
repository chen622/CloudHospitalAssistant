<template>
    <a-row type="flex" align="middle" justify="center" class="info-medicine">
        <a-col span="20">
            <a-card hoveracble title="药品管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                <a-row type="flex" align="top" justify="space-between" style="margin:5px 0 10px 0;">
                    <a-col span="5">
                        <a-input-search placeholder="请输入药品助记码" @search="onSearch" enterButton></a-input-search>
                    </a-col>
                    <a-col span="5">
                        <a-button @click="add" type="primary"><a-icon type="edit" />新增药品</a-button>
                    </a-col>
                    <a-col span="5">
                        <a-button @click="insert" type="primary"><a-icon type="plus-circle"/>导入药品</a-button>
                    </a-col>
                </a-row>
            

            <a-table :columns="columns" :dataSource="data" bordered  :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChange}">
                <template v-for="coll in  ['id','specification','unit','price','type']" :slot="coll" slot-scope="text, record">
                <div :key="coll">
                    <a-input
                    v-if="record.editable"
                    style="margin: -5px 0"
                    :value="text"
                    @change="e => handleChange(e.target.value, record.key, coll)"
                    />
                    <template v-else>{{text}}</template>
                </div>
                </template>
                
                <template slot="form" slot-scope="text,record">
                    <a-select
                    v-if="record.editable"
                    defaultValue="散剂"
                    style="width:100px"
                    >
                      <a-select-option v-for="d in formulation" :key="d.key">{{d.value}}</a-select-option>
                    </a-select>
                <template v-else>{{text}}</template>
            
                </template>
                <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                
                <template slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                    <span v-if="record.editable">
                    <a @click="() => save(record.key)">保存</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title='确定取消?' @confirm="() => cancel(record.key)">
                        <a>取消</a>
                    </a-popconfirm>
                    </span>
                    <span v-else>
                    <a @click="() => edit(record.key)">编辑</a>
                    <a-divider type="vertical" />
                    <a href="javascript:;">删除</a>
                    </span>
                </div>
                </template>
            </a-table>

                
            </a-card>
        </a-col>
    </a-row>
</template>


<script>


    export default {
        data() {
            return {
                formulation:[]
                ,columns:[{
                    title:'药品编码',
                    dataIndex: 'id',
                    key:'id',
                    sorter:true,
                    scopedSlots:{customRender:'id'}
                },{
                    title:'药品名称',
                    dataIndex: 'name',
                    key:'name',
                    sorter:true,
                    scopedSlots:{customRender:'name'}
                },{
                    title:'药品规格',
                    dataIndex: 'specification',
                    key:'specification',
                    sorter:true,
                    scopedSlots:{customRender:'specification'}
                },{
                    title:'药品单位',
                    dataIndex: 'unit',
                    key:'unit',
                    sorter:true,
                    scopedSlots:{customRender:'unit'}
                },{
                    title:'药品单价',
                    dataIndex: 'price',
                    key:'price',
                    sorter:true,
                    scopedSlots:{customRender:'price'}
                },{
                    title:'药品剂型',
                    dataIndex: 'form',
                    key:'form',
                    sorter:true,
                    scopedSlots:{customRender:'form'}
                },{
                    title:'药品类型',
                    dataIndex: 'type',
                    key:'type',
                    sorter:true,
                    scopedSlots:{customRender:'type'}
                },{
                    title:'操作',
                    key:'action',
                    dataIndex:'action',
                    width: '10%',
                    scopedSlots:{customRender:'action'}
                }],
                data:[{
                    key:'1',
                    id:'8697474000208',
                    name:'注射用甲氨喋呤',
                    specification:'1gx1支',
                    unit:'支',
                    price:15.73,
                    form:'针剂',
                    type:'西药'
                },{
                    key:'2',
                    id:'8697474000208',
                    name:'注射用甲氨喋呤',
                    specification:'1gx1支',
                    unit:'支',
                    price:15.73,
                    form:'针剂',
                    type:'西药'
                },{
                    key:'3',
                    id:'8697474000208',
                    name:'注射用甲氨喋呤',
                    specification:'1gx1支',
                    unit:'支',
                    price:15.73,
                    form:'针剂',
                    type:'西药'
                },{
                    key:'4',
                    id:'8697474000208',
                    name:'注射用甲氨喋呤',
                    specification:'1gx1支',
                    unit:'支',
                    price:15.73,
                    form:'针剂',
                    type:'西药'
                }]


            }
        },
        components: {

        },
        computed:{

        },
        methods: {
        
            onSearch(value){
                alert(value)
            },
            add(value){

            },
            insert(value){

            },
            handleChange (value, key, column) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },
            save (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    delete target.editable
                    this.data = newData
                    this.cacheData = newData.map(item => ({ ...item }))
                }
            },
            cancel (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    Object.assign(target, this.cacheData.filter(item => key === item.key)[0])
                    delete target.editable
                    this.data = newData
                }
            }, getPatient () {
            this.$api.get("/constant_variable/getUnit", null,
                
                res => {
                    console.log(res)
                      let a=res.data              
                      let i=0
                      for(i =0;i< a.name.length; i++){
                          console.log(a.name[i])
                          this.formulation.push({
                              value:a.name[i],
                              key: a.id[i]
                        
                          });
                      }
                    
                
                }, res => {
                    that.$message.error(res)
                })
        },
            edit (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    target.editable = true
                    this.data = newData
                }
                this.getPatient();

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