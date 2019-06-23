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
            <a-col span="11" v-if="type===1">
                <p class="drawer-title">病历模板
                    <a-button type="primary" style="float: right" @click="changeDrawer(true,1)">编辑</a-button>
                </p>
                <a-collapse :loading="loading">
                    <a-collapse-panel v-for="mrt in mrts" :key="mrt.id">
                        <span slot="header" class="title">
                            {{mrt.name}}-{{mrt.level.name}}
                            <a-button style="margin-left: 10px" @click="$emit('useTemplate',mrt)">使用</a-button>
                        </span>
                        <p><span class="drawer-label">患者自述病况：</span>{{mrt.selfDescription}}</p>
                        <p><span class="drawer-label">现病史：</span>{{mrt.currentSymptom}}</p>
                        <p><span class="drawer-label">现病治疗情况：</span>{{mrt.previousTreatment}}</p>
                        <p><span class="drawer-label">既往史：</span>{{mrt.historySymptom}}</p>
                        <p><span class="drawer-label">过敏史：</span>{{mrt.allergyHistory}}</p>
                        <p><span class="drawer-label">体格检查：</span>{{mrt.bodyExamination}}</p>
                    </a-collapse-panel>
                </a-collapse>
            </a-col>
            <a-col span="1" v-if="type === 1" style="text-align: center">
                <a-divider type="vertical" style="height: 290px"></a-divider>
            </a-col>
        </a-row>
        <edit-drawer :type="drawerType" :showDrawer="secondDrawer" :editMRT="MRT"
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
        props: ['showDrawer', 'type','MRT'],// tpye: 1病历主页，2保存模板
        data: () => ({
            mrts: [
                {id: -1, name: '', level: {name: ''}}
            ],
            editMRT: null,
            loading: true,
            drawerType: null,
            secondDrawer: false
        }),
        methods: {
            closeDrawer () {
                this.$emit("changeDrawer", false, null)
            },
            changeDrawer (boo, type) {
                this.secondDrawer = boo
                this.drawerType = type
            },
            getMRT () {
                let that = this
                this.load = true
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
            this.getMRT()
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
        max-height: 450px;
        overflow-y: scroll;
    }
</style>