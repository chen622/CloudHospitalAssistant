<template>
    <a-row type="flex" justify="center" align="middle" class="constant">
        <a-col span="20">
            <a-card hoverable title="常量管理" :headStyle="{fontSize:'30px'}" >
                <a-row type="flex" justify="space-around" align="top" >
                    <a-col span="5">
                        <a-input-search placeholder="输入信息" @search="onSearch" enterButton></a-input-search>
                    </a-col>
                    <a-col span="5">
                        <a-button type="primary" @click="handleAdd"><a-icon type="plus" />新建</a-button>
                    </a-col>
                </a-row>
                <br/>
                <a-table :columns="columns" :dataSource="data">
                    <template v-for="col in ['id','name','type','isdelete']" :slot="col" slot-scope="text, record">
                        <div :key="col">
                            <a-input
                                    v-if="record.editable"
                                    style="margin: -5px 0"
                                    :value="text"
                                    @change="e => handleChange(e.target.value, record.key, col)"/>
                            <template v-else>{{text}}</template>
                        </div>
                    </template>
                    <span slot="action" slot-scope="text, record">
                <div class='editable-row-operations'>
                       <span v-if="record.editable">
                            <a @click="() => save(record.key)">保存</a>
                           <a-divider type="vertical"/>
                          <a-popconfirm title='Sure to cancel?' @confirm="() => cancel(record.key)">
                              <a>取消</a>
                           </a-popconfirm>
                       </span>
                       <span v-else>
                           <a @click="() => edit(record.key)">修改</a>
                        </span>
                <a-divider type="vertical"  v-if="!record.editable"/>
                <a-popconfirm v-if="data.length" title="Sure to delete?" @confirm="() => onDelete(record.key)" ><a href="javascript:;" v-if="!record.editable">删除</a></a-popconfirm>
                </div>
              </span>
                </a-table>
            </a-card>
        </a-col>
    </a-row>
</template>

<script>

    const data=[{
        key:'1',
        id:'1',
        name:'科室大类',
        type:'0',
        isdelete:'0'
    }]
    export default {
        name:'constant',
        data(){
            return{
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
                    title:'是否删除',
                    dataIndex:'isdelete',
                    scopedSlots:{customRender:'isdelete'}
                },{
                    title:'操作',
                    dataIndex:'action',
                    scopedSlots:{customRender:'action'}
                }],
                data,
                cacheData:data.map(item => ({ ...item }))

            }
        },

        methods:{
            onSearch(value){
                alert(value)
            },
            handleAdd(){
                // this.data = res.data

            },
            onDelete (key) {
                const data = [...this.data]
                this.data = data.filter(item => item.key !== key)
            },
            handleChange (value, key, column) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    target[column] = value
                    this.data = newData
                }
            },
            edit (key) {
                const newData = [...this.data]
                const target = newData.filter(item => key === item.key)[0]
                if (target) {
                    target.editable = true
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
            },

        }

    }
</script>

<style scoped>

</style>