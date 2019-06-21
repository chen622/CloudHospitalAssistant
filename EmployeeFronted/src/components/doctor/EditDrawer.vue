<template>
    <a-drawer
            title="编辑"
            placement="top"
            :closable="false"
            :visible="showDrawer"
            @close="closeDrawer"
            height="500"
    >
        <a-row type="flex" align="top" justify="space-around">
            <a-col span="20">
                <a-alert message="以下内容将被保存为病历模板，后续可一键应用。" type="info" showIcon/>
            </a-col>
            <a-col span="8" style="padding-top: 20px">
                <p><span class="drawer-label">患者自述病况：</span>{{editMRT.selfDescription}}</p>
                <p><span class="drawer-label">现病史：</span>{{editMRT.currentSymptom}}</p>
                <p><span class="drawer-label">现病治疗情况：</span>{{editMRT.previousTreatment}}</p>
                <p><span class="drawer-label">既往史：</span>{{editMRT.historySymptom}}</p>
                <p><span class="drawer-label">过敏史：</span>{{editMRT.allergyHistory}}</p>
                <p><span class="drawer-label">体格检查：</span>{{editMRT.bodyExamination}}</p>
            </a-col>
            <a-col span="14">
                <diagnose :isTemplate="true">
                </diagnose>
            </a-col>
        </a-row>
        <a-row type="flex" align="top" justify="center" style="margin-top: 20px">
            <a-col span="24">
                <a-divider style="width: 90%"></a-divider>
            </a-col>

            <a-form :form="templateForm" layout="inline">
                <a-form-item label="名称">
                    <a-input v-decorator="['name',{rules: rules.name}]">
                    </a-input>
                </a-form-item>
                <a-form-item label="级别">
                    <a-select v-decorator="['levelId',{rules: rules.levelId}]" style="width: 200px;">
                        <a-select-option v-for="level in levels" :key="level.id">
                            {{level.name}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="saveTemplate">
                        保存
                    </a-button>
                </a-form-item>
            </a-form>
        </a-row>
    </a-drawer>
</template>

<script>
    import Diagnose from "./Diagnose";

    export default {
        name: "EditDrawer",
        components: {Diagnose},
        props: ['showDrawer', 'type', 'editMRT'],//type: 1 保存当前模板
        data: () => ({
            levels: [],
            rules: {
                name: [{required: true, message: '请输入模板名称', trigger: 'blur'}, {}],
                levelId: [{required: true, message: '请选择模板应用级别'}],
            },
            templateForm: null
        }),
        methods: {
            saveTemplate () {
                let that = this;
                this.templateForm.validateFields(err => {
                    if (!err) {
                        let data = this.editMRT;
                        data.name = this.templateForm.getFieldsValue().name;
                        data.levelId = this.templateForm.getFieldsValue().levelId;
                        data.firstDiagnose = this.$store.state.diagnose
                        this.$api.post("/MRT/saveMRT", data,
                            res => {
                                if (res.code === '100') {
                                    that.$message.success("保存模板成功！")
                                } else {
                                    that.$message.error(res.msg)
                                }
                            }, () => {
                            })
                    }
                })
            },
            closeDrawer () {
                if (this.type === 1)
                    this.$emit("closeDrawer");
                this.$emit("changeDrawer", false, null)
            },
            getLevel () {
                let that = this;
                this.$api.get('/constant_variable/getType/5', null,
                    res => {
                        if (res.code === '100') {
                            that.levels = res.data
                        }
                    },
                    () => {
                    })
            }
        },
        mounted () {
            if (this.type === 1) {
                this.getLevel();
                this.templateForm = this.$form.createForm(this)
            }
        }
    }
</script>

<style scoped>

</style>