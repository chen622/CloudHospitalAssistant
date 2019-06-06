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
                <a-table :columns="columns" :dataSource="data" :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChange}">
                    <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                    <span slot="action" slot-scope="text, record">
                        <a href="javascript:;">编辑</a>
                        <a-divider type="vertical" />
                        <a href="javascript:;">删除</a>
                     </span>
                </a-table>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>
    export default {
        data() {
            return {
                columns:[{
                    title:'药品编码',
                    dataIndex: 'id',
                    key:'id',
                    scopedSlots:{customRender:'id'}
                },{
                    title:'药品名称',
                    dataIndex: 'name',
                    key:'name',
                    scopedSlots:{customRender:'name'}
                },{
                    title:'药品规格',
                    dataIndex: 'specification',
                    key:'specification',
                    scopedSlots:{customRender:'specification'}
                },{
                    title:'药品单位',
                    dataIndex: 'unit',
                    key:'unit',
                    scopedSlots:{customRender:'unit'}
                },{
                    title:'药品单价',
                    dataIndex: 'price',
                    key:'price',
                    scopedSlots:{customRender:'price'}
                },{
                    title:'药品剂型',
                    dataIndex: 'form',
                    key:'form',
                    scopedSlots:{customRender:'form'}
                },{
                    title:'药品类型',
                    dataIndex: 'type',
                    key:'type',
                    scopedSlots:{customRender:'type'}
                },{
                    title:'操作',
                    key:'action',
                    dataIndex:'action',
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

            }

        },
    }
</script>
<style scoped>
    .info-medicine{
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>