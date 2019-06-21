<template>
    <a-drawer
            title="编辑"
            placement="top"
            :closable="false"
            :visible="showDrawer"
            @close="closeDrawer"
            height="400"
    >
        <a-row type="flex" align="top" justify="space-around">
            <a-col span="11">
                <p><span class="drawer-label">患者自述病况：</span>{{editMRT.selfDescription}}</p>
                <p><span class="drawer-label">现病史：</span>{{editMRT.currentSymptom}}</p>
                <p><span class="drawer-label">现病治疗情况：</span>{{editMRT.previousTreatment}}</p>
                <p><span class="drawer-label">既往史：</span>{{editMRT.historySymptom}}</p>
                <p><span class="drawer-label">过敏史：</span>{{editMRT.allergyHistory}}</p>
                <p><span class="drawer-label">体格检查：</span>{{editMRT.bodyExamination}}</p>
            </a-col>
            <a-col  span="11">
                <diagnose>
                </diagnose>
            </a-col>
        </a-row>
    </a-drawer>
</template>

<script>
    import Diagnose from "./Diagnose";
    export default {
        name: "EditDrawer",
        components: {Diagnose},
        props: ['showDrawer', 'type', 'editMRT'],//type: 1 保存当前模板
        data: () => ({}),
        methods: {
            closeDrawer () {
                this.$emit("changeDrawer", false, null)
            },
        }
    }
</script>

<style scoped>

</style>