<template>
  <a-table :columns="columns" :dataSource="data" bordered>
    <template v-for="col in  ['id', 'name', 'specification','unit','price','form','type']" :slot="col" slot-scope="text, record, index">
      <div :key="col">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0"
          :value="text"
          @change="e => handleChange(e.target.value, record.key, col)"
        />
        <template v-else>{{text}}</template>
      </div>
    </template>
    <template slot="operation" slot-scope="text, record">
      <div class='editable-row-operations'>
        <span v-if="record.editable">
          <a @click="() => save(record.key)">Save</a>
          <a-popconfirm title='Sure to cancel?' @confirm="() => cancel(record.key)">
            <a>Cancel</a>
          </a-popconfirm>
        </span>
        <span v-else>
          <a @click="() => edit(record.key)">Edit</a>
        </span>
      </div>
    </template>
  </a-table>
</template>
<script>
  const columns=[{
                    title:'药品编码',
                    dataIndex: 'id1',
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
                    scopedSlots:{customRender:'action'}
                }]
const  data=[{
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

// for (let i = 0; i < 100; i++) {
//   data.push({
//     key: i.toString(),
//     name: `Edrward ${i}`,
//     age: 32,
//     address: `London Park no. ${i}`,
//   })
// }
export default {
  data () {
    this.cacheData = data.map(item => ({ ...item }))
    return {
      data,
      columns,
    }
  },
  methods: {
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
  },
}
</script>
<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
</style>
