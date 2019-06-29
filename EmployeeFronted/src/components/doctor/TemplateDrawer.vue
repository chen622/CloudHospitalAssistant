<template>
    <a-drawer
            title="常用与模板"
            placement="top"
            :closable="false"
            :visible="showDrawer"
            @close="closeDrawer"
            height="500"
    >
        <a-row type="flex" align="top" justify="space-around">
            <a-col span="22" v-if="type===1">
                <p class="drawer-title">病历模板
                </p>
                <a-collapse :loading="loading">
                    <a-collapse-panel v-for="mrt in mrts" :key="mrt.id">
                        <span slot="header" class="title">
                            {{mrt.name}}-{{mrt.level.name}}
                            <a-button style="margin-left: 10px" @click="$emit('useTemplate',mrt);closeDrawer()">使用</a-button>
                        </span>
                        <a-row type="flex" align="top" justify="space-around">
                            <a-col span="10">
                                <p><span class="drawer-label">患者自述病况：</span>{{mrt.selfDescription}}</p>
                                <p><span class="drawer-label">现病史：</span>{{mrt.currentSymptom}}</p>
                                <p><span class="drawer-label">现病治疗情况：</span>{{mrt.previousTreatment}}</p>
                            </a-col>
                            <a-col span="10">
                                <p><span class="drawer-label">既往史：</span>{{mrt.historySymptom}}</p>
                                <p><span class="drawer-label">过敏史：</span>{{mrt.allergyHistory}}</p>
                                <p><span class="drawer-label">体格检查：</span>{{mrt.bodyExamination}}</p>
                            </a-col>
                            <a-col span="3">
                                <p>
                                    <a-button type="primary" @click="editMrt(mrt)">编辑
                                    </a-button>
                                </p>
                                <p>
                                    <a-button type="danger" @click="deleteMrt(mrt)">删除
                                    </a-button>
                                </p>
                            </a-col>
                        </a-row>
                    </a-collapse-panel>
                </a-collapse>
            </a-col>
            <a-col span="22" v-else-if=" type===3">
                <p class="drawer-title">诊疗信息模板
                </p>
                <a-collapse :loading="loading">
                    <a-collapse-panel v-for="it in its" :key="it.id">
                        <span slot="header" class="title">
                            {{it.name}}-{{it.lev.name}}
                            <a-button style="margin-left: 10px" @click="addInspection(it)">使用</a-button>
                        </span>
                        <a-row type="flex" align="top" justify="space-around">
                            <a-col span="10">
                                <p>诊疗项目</p>
                                <a-list
                                        :grid="{ md: 2 }"
                                        :dataSource="it.applications"
                                >
                                    <a-list-item slot="renderItem" slot-scope="item">
                                        <a-card :title="item.nonDrug.name">
                                            <p><b>价格: </b>
                                                {{item.nonDrug.price}}</p>
                                            <p><b>数量: </b>
                                                {{item.quantity}}</p>
                                        </a-card>
                                    </a-list-item>
                                </a-list>
                            </a-col>
                            <a-divider type="vertical" style="min-height: 100px"></a-divider>
                            <a-col span="10">
                                <p>处方</p>
                                <a-list
                                        :grid="{ md: 2 }"
                                        :dataSource="it.prescriptions"
                                >
                                    <a-list-item slot="renderItem" slot-scope="item">
                                        <a-card :title="item.drug.name">
                                            <p><b>价格: </b>
                                                {{item.drug.price}}</p>
                                            <p><b>数量: </b>
                                                {{item.amount}}</p>
                                        </a-card>
                                    </a-list-item>
                                </a-list>
                            </a-col>
                            <a-col span="3">
                                <p>
                                    <a-button type="primary" @click="editMrt(it)">编辑
                                    </a-button>
                                </p>
                                <p>
                                    <a-button type="danger" @click="deleteMrt(it)">删除
                                    </a-button>
                                </p>
                            </a-col>
                        </a-row>
                    </a-collapse-panel>
                </a-collapse>
            </a-col>
            <!--            <a-col span="1" v-if="type === 1" style="text-align: center">-->
            <!--                <a-divider type="vertical" style="height: 290px"></a-divider>-->
            <!--            </a-col>-->
        </a-row>
        <edit-drawer v-if="secondDrawer" :type="drawerType" :showDrawer="secondDrawer" :editMRT="MRT"
                     @changeDrawer="e=>changeDrawer(e)" @closeDrawer="closeDrawer"></edit-drawer>
    </a-drawer>
</template>

<script>
    import EditDrawer from "./EditDrawer";

    export default {
        name: "IndexTemplate",
        components: {
            'edit-drawer': EditDrawer
        },
        props: ['showDrawer', 'type', 'MRT'],// tpye: 1病历主页，2保存模板，3组套,4保存组套
        data: () => ({
            mrts: [
                {id: -1, name: '加载中', level: {name: 'loading'}}
            ],
            its: [
                {id: -1, name: '加载中', lev: {name: 'loading'}}
            ],
            editMRT: null,
            loading: true,
            drawerType: null,
            secondDrawer: false
        }),
        methods: {
            addInspection (it) {
                it.applications.forEach(application => {
                    application.temp = true
                    application.id = null
                    application.createTime = null
                    application.department = application.nonDrug.department
                    this.$store.commit('addInspections', application)
                })
                it.prescriptions.forEach(prescription => {
                    prescription.temp = true
                    prescription.id = null
                    prescription.createTime = null
                    this.$store.commit("addInspectionPrescriptions", prescription)
                })
                this.$message.success("模板应用成功")
            },
            editMrt (mrt) {
                let that = this
                that.$store.commit('setLoading', true)
                this.$api.post("/MRT/delete/" + mrt.id, null,
                    res => {
                        if (res.code === '100') {
                            this.$emit("changeDrawer", false, null)

                            that.$message.info("修改完成后点击保存即可")
                            that.$emit('useTemplate', mrt)
                        } else {
                            that.$message.error(res.msg)
                        }
                        that.$store.commit('setLoading', false)

                    }, () => {
                        that.$store.commit('setLoading', false)

                    })

            },
            deleteMrt (mrt) {
                let that = this
                this.$api.post("/MRT/delete/" + mrt.id, null,
                    res => {
                        if (res.code === '100') {
                            that.getMRT()
                            that.$message.success("删除成功")
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
                    })
            },
            closeDrawer () {
                this.$emit("changeDrawer", false, null)
            },
            changeDrawer (boo, type) {
                this.secondDrawer = boo
                this.drawerType = type
            },
            getIT () {
                let that = this
                this.loading = true
                that.$store.commit("setLoading", true)
                this.$api.get("/IT/getIT", null,
                    res => {
                        if (res.code === '100') {
                            that.its = res.data
                        } else {
                            that.$message.error(res.msg)
                        }
                        that.loading = false
                        that.$store.commit("setLoading", false)
                    },
                    () => {
                        that.loading = false
                        that.$store.commit("setLoading", false)
                    })
            },
            getMRT () {
                let that = this
                this.loading = true
                that.$store.commit("setLoading", true)
                this.$api.get('/MRT/getMRT', null,
                    res => {
                        if (res.code === '100') {
                            that.mrts = res.data
                        } else {
                            that.$message.error(res.msg)
                        }
                        that.loading = false
                        that.$store.commit("setLoading", false)

                    },
                    () => {
                        that.loading = false
                        that.$store.commit("setLoading", false)
                    }
                )
            },
        }, mounted () {
        }
    }
</script>

<style>
    .title {
        font-size: 15px;
        font-weight: bold;
        margin-bottom: 10px;
    }

    .drawer-title {
        font-size: 16px;
        font-weight: bold;
        line-height: 40px;
    }

    .drawer-label {
        font-weight: bold;
    }

    .ant-drawer-body {
        max-height: 430px;
        overflow-y: scroll;
    }
</style>