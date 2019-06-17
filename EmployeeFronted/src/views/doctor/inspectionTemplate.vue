<template>
    <div>
        <a-row type="flex" align="middle" justify="center" class="info-medicine">
            <a-col span="23">
                <a-card hoveracble title="模板管理" :headStyle="{fontSize:'30px'}" :bodyStyle="{padding:'5px 0'}">
                     <a-col span="23">
                        <a-row type="flex" align="top" justify="space-between"
                                    style="margin:5px 0 10px 0;text-align: center">
                            <a-col span="5" style="margin-left:20px;">
                                <a-input-search placeholder="请输入模板助记码" @search="onSearchByCode"
                                                enterButton></a-input-search>
                            </a-col>
                            <a-col span="5">
                                <a-input-search placeholder="请输入模板名称" @search="onSearchByName"
                                                enterButton></a-input-search>
                            </a-col>
                            <a-col span="3">
                                <a-button @click="add" style="color:#1890FF">
                                    <a-icon type="edit"/>
                                    新增检查模板
                                </a-button>
                            </a-col>

                            <a-col span="3">
                                <a-button @click="deleteAll" type="danger">
                                    <a-icon type="plus-circle"/>
                                    全部删除
                                </a-button>
                            </a-col>
                        </a-row>

                        <a-table :columns="columns" :dataSource="data" :scroll="{ x: 1500 }" bordered
                                    :rowSelection="{slectedRowKeys:selectedRowKeys, onChange:onSelectChange}">
                            <template slot="action" slot-scope="text, record">
                                <div class='editable-row-operations'>
                            <span v-if="record.editable">
                            <a @click="() => saveRow(record.id)">保存</a>
                            <a-divider type="vertical"/>
                                <!-- <a-popconfirm title='确定取消吗?' @confirm="() => cancel(record.id)"> -->
                            <a style="color:red" @click="() => cancel(record.id)">取消</a>
                                <!-- </a-popconfirm> -->
                            </span>
                                    <span v-else>
                            <a @click="() => edit(record.id)">编辑</a>
                            <a-divider type="vertical"/>
                            <a @click="() => deleteRow(record.id)" style="color:red">删除</a>
                            </span>
                                </div>
                            </template>
                        </a-table>
                     </a-col> 

                     <a-col span="13">
                        <a-form  :form="form"  layout="inline">
                          <a-form-item label="名字"
                            :label-col="{ span: 5 }"
                            :wrapper-col="{ span: 12 }"
                            >
                                <a-input
                                    v-decorator="[
                                    'name',
                                    {rules: [{ required: true, message: '请输入模板名称!' }]}
                                    ]"
                                />
                            </a-form-item>

                            <a-form-item label="级别">
                                <a-radio-group @change="radioChange" v-model="radioValue">
                                    <a-radio :style="radioStyle" :value="1">院级</a-radio>
                                    <a-radio :style="radioStyle" :value="2">科室级</a-radio>
                                    <a-radio :style="radioStyle" :value="3">个人</a-radio>
                            </a-form-item>

                            
                        </a-form>
                     </a-col>
                </a-card>
            </a-col>
        </a-row>


        <template>
            <div id="components-modal-demo-position" style="width:700px">
                <a-modal
                        title="检查模板信息"
                        width="500px"
                        :visible="modelVisiable"
                        @ok="() => ok()"
                        @cancel="() => cancel(this.drugTemp)"
                >
                    <a-form :columns="columns" :dataSource="drugTemp" :form="form" style="width:500px" layout="inline">
                        <a-form-item label="模板名称">
                            <a-input
                                    style="width:300px"
                                    v-decorator="[
                                'name',
                                {
                                    initialValue: this.drugTemp.code,
                                    rules: [{ required: true, message: '请输入模板名称!' }],
                                }
                                ]"
                                            getFieldDecorator="('code',)"
                            />
                    </a-form-item>

                        <a-form-item label="药品名称" style="margin:5px 5px 0 0">
                            <a-input
                                    style="width:300px"

                                    v-decorator="[
                        'name',
                        {
                            initialValue: this.drugTemp.name,
                            rules: [{ required: true, message: '请输入药品名称!' }],
                        }
                        ]"
                            />
                        </a-form-item>


                        <a-form-item label="药品规格">
                            <a-input

                                    style="width:300px"
                                    v-decorator="[
                        'standard',
                        {
                            initialValue: this.drugTemp.standard,
                            rules: [{ required: true, message: '请输入药品规格!' }],
                        }
                        ]"
                            />
                        </a-form-item>

                        <a-form-item label="药品单位">
                            <a-input

                                    style="width:300px"
                                    v-decorator="[
                        'packageCompany',
                        {
                            initialValue: this.drugTemp.packageCompany,
                            rules: [{ required: true, message: '请输入药品单位!' }],
                        }
                        ]"
                            />
                        </a-form-item>

                        <a-form-item label="药品单价">
                            <a-input
                                    style="width:300px"
                                    v-decorator="[
                        'price',
                        {
                            initialValue: this.drugTemp.price,
                            rules: [{ required: true, validator:checkPrice }],
                        }
                        ]"
                            />
                        </a-form-item>

                        <a-form-item label="生产厂家">
                            <a-input

                                    style="width:300px"
                                    v-decorator="[
                        'factory',
                        {
                            initialValue: this.drugTemp.factory,
                            rules: [{ required: true, message: '请输入生产厂家!' }],
                        }
                        ]"
                            />
                        </a-form-item>

                        <a-form-item label="药品拼音">
                            <a-input

                                    style="width:300px"
                                    v-decorator="[
                        'spell',
                        {
                            initialValue: this.drugTemp.spell,
                            rules: [{ required: true, message: '请输入拼音!' }],
                        }
                        ]"
                            />
                        </a-form-item>

                        <a-form-item label="药品剂型">
                            <a-select
                                    v-decorator="[
                        'formulationName',
                        {
                            initialValue: this.drugTemp.formulationName,
                            rules: [{ required: true, message: '请输入药品剂型!' }]}
                        ]"
                                    style="width:300px"
                                    @change="e => formChange(e)"
                            >
                                <a-select-option v-for="d in formulation" :key="d.id">{{d.name}}</a-select-option>
                            </a-select>
                        </a-form-item>


                        <a-form-item label="药品类型">
                            <a-select
                                    v-decorator="[
                        'drugTypeName',
                        {
                            initialValue: this.drugTemp.drugTypeName,
                            rules: [{ required: true, message: '请输入药品类型!' }]}
                        ]"
                                    style="width:300px"
                                    @change="e => drugTypeChange(e)"
                            >
                                <a-select-option v-for="d in durgTypeList" :key="d.id">{{d.name}}</a-select-option>
                            </a-select>
                        </a-form-item>

                        <a-form-item label="费用类型">
                            <a-select
                                    v-decorator="[
                        'paymentType',
                        {
                            initialValue: this.drugTemp.paymentType,
                            rules: [{ required: true, message: '请输入费用类型!' }]}
                        ]"
                                    style="width:300px"
                                    @change="e => paymentTypeChange(e)"
                            >
                                <a-select-option v-for="d in paymentTypeList" :key="d.id">{{d.name}}</a-select-option>
                            </a-select>
                        </a-form-item>

                    </a-form>

                </a-modal>
            </div>


        </template>

        <template>

            <a-modal
                    title=""
                    style="text-align:center"
                    width="500px"
                    :visible="visible"
                    @ok="() => okReturn()"
                    @cancel="() => cancelReturn()"
            >

                <a-form :columns="columns" :form="form2" style="width:400px;text-align:center" layout="inline">

                    <a-form-item label="请输入退药数量" style="margin-left:35px;text-align:center">
                        <a-input
                                style="width:100px;text-align:center"
                                v-decorator="[
                        'quantity',
                        {

                             rules: [{ required: true, validator:'请输入数字' }],
                        }
                        ]"
                                getFieldDecorator="('quantity',)"
                        />
                    </a-form-item>

                    <a-form-item style="text-align:center">
                        <a-button style="width:80px;text-align:center" type="dashed" disabled block>
                            上限({{this.restDrug}})
                        </a-button>
                    </a-form-item>

                </a-form>

            </a-modal>
        </template>


    </div>
</template>


<script>
    import {Promise} from 'q';
    import moment from 'moment'//导入文件
    export default {
        data () {
            return {
                radioValue:3,
                form: this.$form.createForm(this),
            }
        },

        computed: {
            formItemLayout () {
                const {formLayout} = this;
                return formLayout === 'horizontal' ? {
                    labelCol: {span: 4},
                    wrapperCol: {span: 14},
                } : {};
            },
            buttonItemLayout () {
                const {formLayout} = this;
                return formLayout === 'horizontal' ? {
                    wrapperCol: {span: 14, offset: 4},
                } : {};
            },
        }, created () {
            this.getData();
        }, filters: {
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
        }, methods: {

        }
    }

</script>

<style scoped>
    .info-medicine {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .form-title {
        font-size: 18px;
    }
</style>