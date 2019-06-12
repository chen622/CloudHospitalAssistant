<template>
    <a-row type="flex" align="middle" justify="center" class="charge">
        <a-col span="20">
            <a-card hoverable title="缴费" :headStyle="{fontSize: '30px'}" :bodyStyle="{padding:'5px 0'}">
                <a-form :form="form" layout="inline" style="margin: 20px">
                    <p class="form-header">患者查询:</p>
                    <a-form-item label="身份证号">
                        <a-input placeholder="身份证号"
                                 v-decorator="['id',{rules: [{required: true, message: '请输入患者身份证号', trigger: 'blur'}]}]"></a-input>
                    </a-form-item>
                    <p class="form-header">信息确认:</p>
                    <a-form-item label="姓名">
                        <a-input v-decorator="['realName']"
                                 placeholder="姓名"></a-input>
                    </a-form-item>
                    <a-form-item label="手机号">
                        <a-input v-decorator="['phone',{rules: [{type: 'number',message: '手机号非法', trigger: 'blur'}]}]"
                                 placeholder="身份证号"></a-input>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="searchPatient">查询患者</a-button>
                    </a-form-item>
                </a-form>
                <template>
                    <a-table :columns="columns" :dataSource="data"
                             :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
                        <a slot="name" slot-scope="text" href="javascript:;">{{text}}</a>
                        <span slot="customTitle">姓名</span>
                    </a-table>
                    <a-button type="primary">收费结算</a-button>
                </template>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>

    export default {
        data () {
            return {
                form: this.$form.createForm(this),
                columns: [
                    {
                        title: '身份证号',
                        dataIndex: 'id',
                        align: 'center'
                    }, {
                        title: '姓名',
                        dataIndex: 'name',
                        align: 'center'
                    }, {
                        title: '项目名称',
                        dataIndex: 'Project',
                        align: 'center'

                    }, {
                        title: '单价',
                        dataIndex: 'price',
                        align: 'center'

                    }, {
                        title: '数量',
                        dataIndex: 'Number',
                        align: 'center'

                    }, {
                        title: '开立时间',
                        dataIndex: 'date',
                        scopedSlots: {customRender: 'date'},
                        align: 'center'
                    }, {
                        title: '状态',
                        dataIndex: 'state',
                    }],
                data: [],
            }
        },
        methods: {
            onSelectChange (selectedRowKeys) {
                this.selectedRowKeys = selectedRowKeys
            },
            onSearch (value) {
                alert(value)

            },

        },
    };
</script>
<style>
    .charge {
        margin-top: 40px;
        margin-bottom: 20px;
    }
</style>
