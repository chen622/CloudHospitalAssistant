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
                            <a-button style="margin-left: 10px" @click="$emit('useTemplate',mrt)">使用</a-button>
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
            <a-col span="22" v-if="type===2">
                <p class="drawer-title">诊疗信息模板
                </p>
                <a-collapse :loading="loading">
                    <a-collapse-panel v-for="it in its" :key="it.id">
                        <span slot="header" class="title">
                            {{it.name}}-{{it.level.name}}
                            <a-button style="margin-left: 10px" @click="$emit('useTemplate',it)">使用</a-button>
                        </span>
                        <a-row type="flex" align="top" justify="space-around">
                            <a-col span="10">
                                <p><span class="drawer-label">患者自述病况：</span>{{it.selfDescription}}</p>
                                <p><span class="drawer-label">现病史：</span>{{it.currentSymptom}}</p>
                                <p><span class="drawer-label">现病治疗情况：</span>{{it.previousTreatment}}</p>
                            </a-col>
                            <a-col span="10">
                                <p><span class="drawer-label">既往史：</span>{{it.historySymptom}}</p>
                                <p><span class="drawer-label">过敏史：</span>{{it.allergyHistory}}</p>
                                <p><span class="drawer-label">体格检查：</span>{{it.bodyExamination}}</p>
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
        props: ['showDrawer', 'type', 'MRT'],// tpye: 1病历主页，2保存模板，3组套
        data: () => ({
            mrts: [
                {id: -1, name: '', level: {name: ''}}
            ],
            its: [
                {id: -1, name: '', level: {name: ''}}
            ],
            editMRT: null,
            loading: true,
            drawerType: null,
            secondDrawer: false
        }),
        methods: {
            editMrt (mrt) {
                let that = this
                that.$store.commit('setLoading', true)
                this.$api.post("/MRT/delete/" + mrt.id, null,
                    res => {
                        if (res.code === '100') {
                            that.$store.commit('setLoading', false)
                            this.$emit("changeDrawer", false, null)

                            that.$message.info("修改完成后点击保存即可")
                            that.$emit('useTemplate', mrt)
                        } else {
                            that.$message.error(res.msg)
                        }
                    }, () => {
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
                this.$api.get("/IT/getIT", null,
                    res => {
                        if (res.code === '100') {
                            that.its = res.data
                        } else {
                            that.$message.error(res.msg)
                        }
                        that.loading = false
                    },
                    () => {
                        that.loading = false
                    })
            },
            getMRT () {
                let that = this
                this.loading = true
                this.$api.get('/MRT/getMRT', null,
                    res => {
                        if (res.code === '100') {
                            that.mrts = res.data
                            that.loading = false
                        } else {
                            that.$message.error(res.msg)
                        }
                    },
                    () => {
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