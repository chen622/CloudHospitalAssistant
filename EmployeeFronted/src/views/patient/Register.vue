<template>
    <a-row type="flex" align="middle" justify="center" class="info-search">
        <a-col span="20">
            <a-card hoveracble title="查询" :headStyle="{fontSize: '30px'}" :body-style="{padding: 0}">
                <a-form layout="inline" style="margin: 20px">
                    <p class="form-header">患者查询:</p>
                    <a-form-item label="身份证号">
                        <a-input placeholder="身份证号" v-decorator="['id']"></a-input>
                    </a-form-item>
                    <p class="form-header">信息确认:</p>
                    <a-form-item label="姓名">
                        <a-input v-decorator="['realName']"
                                 placeholder="姓名"></a-input>
                    </a-form-item>
                    <a-form-item label="手机号">
                        <a-input v-decorator="['phone']"
                                 placeholder="身份证号"></a-input>
                    </a-form-item>
                </a-form>
                <a-table :columns="columns" :dataSource="patient">
                    <template slot="sex" slot-scope="text">
                        <tag v-if="text === false" color="pink">女</tag>
                        <tag v-else color="blue">男</tag>
                    </template>
                </a-table>
            </a-card>
        </a-col>
    </a-row>
</template>
<script>

    export default {
        data () {
            return {
                form: this.$form.createForm(this),
                columns: [{
                    title: '身份证号',
                    dataIndex: 'id',
                    key: 'id',
                    scopedSlots: {customRender: 'id'}
                }, {
                    title: '姓名',
                    dataIndex: 'name',
                    key: 'name',
                    scopedSlots: {customRender: 'name'},
                }, {
                    title: '年龄',
                    dataIndex: 'age',
                    key: 'age',
                    scopedSlots: {customRender: 'age'},
                }, {
                    title: '性别',
                    dataIndex: 'sex',
                    key: 'sex',
                    scopedSlots: {customRender: 'sex'},
                }, {
                    title: '操作',
                    key: 'action',
                    dataIndex: 'action',
                    scopedSlots: {customRender: 'action'}
                }],
                patient: [{
                    id: 123,
                    name: "畅晨铭",
                    age: 18,
                    sex: false,
                }]
            }
        },
        methods: {
            getInfo () {
                let that = this
                this.$api.get("", null,
                    res => {
                        if (res.code === "") {
                            that.data = res.data

                        } else {
                            that.$message.error(res)
                        }
                    })

            },
            onSearch (value) {
                alert(value)
            },

        },
    };
</script>
<style>
    .info-search {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .form-header {
        font-size: 20px;
        font-weight: bold;
        margin: 6px 0;
    }
</style>
