<template>
    <div>
        <a-modal
                :confirmLoading="newNonDrug.createLoading"
                :visible="newNonDrug.isCreating"
                @cancel="newNonDrug.isCreating = false"
                @ok="addNonDrug"
                title="创建非药品项目"
                v-if="newNonDrug.isCreating">
            <a-form :form="newNonDrug.item">
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }" label="非药品名称">
                    <a-input v-decorator="['name',{rules: rules.name}]"
                             placeholder="请输入对应名称"/>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }" label="非药品编号">
                    <a-input v-decorator="['code',{rules: rules.code}]"
                             placeholder="请输入非药品对应编号"/>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }" label="规格">
                    <a-input v-decorator="['standard',{rules: rules.standard}]"
                             placeholder="请输入非药品对应编号"/>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }" label="价格">
                    <a-input-number style="width: 100%" v-decorator="['price',{rules: rules.price}]"
                                    placeholder="请输入对应价格"/>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }" label="类别">
                    <a-select v-decorator="['feeTypeId',{rules: rules.feeTypeId}]"
                              placeholder="请输入项目类别">
                        <a-select-option v-for="paymentType in paymentTypes" :key="paymentType.id">
                            {{paymentType.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :label-col="{ span: 6 }"
                             :wrapper-col="{ span: 16 }" label="执行科室">
                    <a-cascader v-decorator="['department',{rules: rules.department}]"
                                :fieldNames="{ label: 'name', value: 'id', children: 'children' }"
                                :options="departmentKinds" :loadData="loadData" placeholder="选择科室"/>
                </a-form-item>
            </a-form>
        </a-modal>

        <a-card hoverable title="非药品管理" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding: '5px 0'}">
            <a-row type="flex" align="middle" justify="center" style="margin: 5px 0 10px 0">
                <a-col span="5">
                    <a-input-search
                            placeholder="通过项目名称搜索"
                            @search="onSearch"
                            enterButton></a-input-search>
                </a-col>
                <a-button @click="showCreate" type="primary" style="margin-left: 10px">
                    <a-icon type="plus-circle"/>
                    新建
                </a-button>
                <a-button @click="portNonDrug = 1" type="primary" style="margin-left: 10px">
                    导入
                </a-button>
                <a-button @click="portNonDrug = 2" type="primary" style="margin-left: 10px">
                    导出
                </a-button>
            </a-row>

            <a-modal v-if="portNonDrug===1" :visible="portNonDrug===1" :footer="false" @cancel="portNonDrug = false">
                <a-upload name="file" :multiple="true"
                          :action="$url+'/non_drug/excelIn'" :headers="header" @change="submit"
                          :beforeUpload="beforeUpload"
                          accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                >
                    <a-button>
                        <a-icon type="upload"/>
                        Click to Upload
                    </a-button>
                </a-upload>
            </a-modal>

            <a-modal v-else-if="portNonDrug===2" :visible="portNonDrug===2" :footer="false" @cancel="portNonDrug=false">
                <a :href="$url+'/non_drug/excelOut'" download="">下载</a>
            </a-modal>

            <a-table :columns="nonDrugColumns" :dataSource="nonDrugs" :pagination="{defaultPageSize: 10}"
                     rowKey="id"
                     :loading="loading.nonDrug">
                <template v-for="col in ['name', 'code','price']" :slot="col" slot-scope="text, record">
                    <div :key="col">
                        <a-input
                                v-if="record.editable"
                                :value="text"
                                @change="e => handleChange(e.target.value, record.id, col)"/>
                        <template v-else>{{text}}</template>
                    </div>
                </template>

                <template slot="operation" slot-scope="text, record">
                    <div class='editable-row-operations'>
                            <span v-if="record.editable">
                                <a-popconfirm title='确定保存次修改吗?' @confirm="save(record)">
                                    <a>保存</a>
                                </a-popconfirm>
                            <a @click="() => cancel(record.id)">取消</a>
                            </span>
                        <span v-else>
                                <a @click="() => edit(record.id)">编辑</a>
                                <a-popconfirm title='确定删除该科室吗?' @confirm="remove(record.id)">
                                    <a style="color: #f5222d;">删除</a>
                                </a-popconfirm>
                            </span>
                    </div>
                </template>
            </a-table>
        </a-card>
    </div>
</template>

<script>
    export default {
        name: "NonDrug",
        data: () => ({
            header: {
                authorization: sessionStorage.getItem('token')
            },
            paymentTypes: [],
            departmentKinds: [],
            cacheData: null,
            newNonDrug: {
                item: null,
                isCreating: false,
                createLoading: false,
            },
            loading: {
                nonDrug: true
            },
            nonDrugColumns: [
                {
                    title: '名称',
                    dataIndex: 'name',
                    align: 'center',
                    scopedSlots: {customRender: 'name'},
                }, {
                    title: '编码',
                    dataIndex: 'code',
                    align: 'center',
                    scopedSlots: {customRender: 'code'},
                }, {
                    title: '项目类别',
                    dataIndex: 'paymentType.name',
                    align: 'center',
                }, {
                    title: '价格',
                    dataIndex: 'price',
                    scopedSlots: {customRender: 'price'},
                    align: 'center',
                }, {
                    title: '执行科室',
                    dataIndex: 'department.name',
                    align: 'center'
                }, {
                    title: '操作',
                    dataIndex: 'operation',
                    scopedSlots: {customRender: 'operation'},
                }
            ],
            nonDrugs: [],
            rules: {
                name: [{required: true, message: '请输入非药品名称', trigger: 'blur'}],
                code: [{required: true, message: '请输入非药品编号', trigger: 'blur'}],
                price: [{required: true, type: 'number', message: '请输入非药品价格', trigger: 'blur'}],
                standard: [{required: true, message: '请输入非药品规格', trigger: 'blur'}],
                feeTypeId: [{required: true, message: '请选择项目类别'}],
                department: [{required: true, message: '请输入执行科室'}],
            },
            portNonDrug: 0
        }),
        methods: {
            importNonDrug () {

            },
            exportNonDrug () {

            },
            showCreate () {
                this.newNonDrug.isCreating = true
            },
            addNonDrug () {
                this.newNonDrug.createLoading = true
                let that = this
                this.newNonDrug.item.validateFields((err) => {
                    if (!err) {
                        let data = this.newNonDrug.item.getFieldsValue()
                        data.executiveDepartment = data.department[2]
                        data.department = null
                        this.$api.post("/non_drug/insert", data,
                            res => {
                                if (res.code === "100") {
                                    that.$message.success("创建成功！")
                                    that.newNonDrug.isCreating = false
                                    that.getNonDrug()
                                } else {
                                    that.$message.error(res.msg)
                                }
                                that.newNonDrug.createLoading = false

                            }, () => {
                                that.newNonDrug.createLoading = false
                            })
                    }
                })
            },
            getNonDrug () {
                let that = this
                that.loading.nonDrug = true
                this.$api.get("/non_drug/getTypeAndNonDrug/", null,
                    res => {
                        if (res.code === "100") {
                            that.nonDrugs = res.data
                            that.cacheData = res.data.map(item => ({...item}))
                            that.loading.nonDrug = false
                        } else {
                            that.$message.error(res)
                        }
                    },
                    () => {
                    })
            },
            getDepartmentKind () {
                let that = this
                this.$api.get("/department_kind/getAll", null,
                    res => {
                        if (res.code === '100')
                            res.data.type.forEach(
                                type => {
                                    res.data.departmentKinds[type.id].forEach(kind => {
                                        kind.name = kind.kindName
                                        kind.isLeaf = false
                                    })
                                    type.children = res.data.departmentKinds[type.id]
                                    that.departmentKinds.push(type)
                                }
                            )
                    }, () => {
                    }
                )
            },
            getPaymentType () {
                let that = this
                this.$api.get("/payment_type/getByTypeId/" + 1, null,
                    res => {
                        if (res.code === "100") {
                            that.paymentTypes = res.data
                        } else {
                            that.$message.error(res)
                        }
                    }, () => {
                    })
            },
            loadData (selectedOptions) {
                let targetOption = selectedOptions[selectedOptions.length - 1];
                targetOption.loading = true;

                let that = this
                this.$api.get('/department/getByKind/' + targetOption.id, null,
                    res => {
                        targetOption.loading = false;
                        if (res.code === '100') {
                            targetOption.children = res.data
                        } else {
                            targetOption.children = []
                        }
                        that.departmentKinds = [...that.departmentKinds]
                    }, () => {
                    })
            },
            onSearch (value) {
                if (value === null || value === undefined)
                    value = null
                let that = this
                that.loading.department = true
                this.$api.get("/non_drug/getTypeAndNonDrug/name/" + value, null,
                    res => {
                        if (res.code === "100") {
                            that.nonDrugs = res.data
                            that.cacheData = res.data.map(item => ({...item}))
                            that.loading.nonDrug = false
                        } else {
                            that.$message.error(res)
                        }
                    }, () => {
                    })
            },
            handleChange (value, key, column) {
                const newData = [...this.nonDrugs]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target[column] = value
                    this.nonDrugs = newData
                }
            },
            edit (key) {
                const newData = [...this.nonDrugs]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    target.editable = true
                    this.nonDrugs = newData
                }
            },
            save (record) {
                let that = this
                this.$api.post("/non_drug/modify", record,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("修改成功")
                            that.getNonDrug()
                        } else {
                            that.$message.error(res.msg)
                        }
                    },
                    () => {
                    })

            },
            remove (key) {
                let that = this
                this.$api.post("/non_drug/delete/" + key, null,
                    res => {
                        if (res.code === '100') {
                            that.$message.success("删除成功")
                            that.getNonDrug()
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            cancel (key) {
                const newData = [...this.nonDrugs]
                const target = newData.filter(item => key === item.id)[0]
                if (target) {
                    Object.assign(target, this.cacheData.filter(item => key === item.id)[0])
                    delete target.editable
                    this.nonDrugs = newData
                }
            },
            submit (info) {
                console.log(info)
                if (info.file.status !== 'uploading') {
                    console.log(info.file, info.fileList);
                }
                if (info.file.status === 'done') {
                    var state = false;
                    if (info.file.response.data.success !== 0){
                        this.$message.success(`${info.file.response.data.success} file uploaded successfully` )
                        this.state = true;
                    }
                    if (info.file.response.data.error !== 0){
                        this.$message.error(`${info.file.response.data.error} 错误`)
                    }
                    if (this.state) {
                        this.portNonDrug = 0
                        this.getNonDrug()
                    }
                } else if (info.file.status === 'error') {
                    this.$message.error(`${info.file.name} file upload failed.`);
                    this.$message.error(`${info.file.response.data.error} file upload failed`)
                    this.getNonDrug()
                }
            },
            beforeUpload (file) {
                let that = this
                return new Promise((resolve, reject) => {
                    const is2M = file.size / 1024 / 1024 < 2
                    if (!is2M) {
                        that.$message.error("上传文件大小不能超过2M")
                        return reject(false)
                    } else {
                        return resolve(true)
                    }
                })
            },
        }
        ,
        mounted () {
            this.newNonDrug.item = this.$form.createForm(this)
            this.getNonDrug()
            this.getPaymentType()
            this.getDepartmentKind()
        }
        ,
    }
</script>

<style scoped>


</style>