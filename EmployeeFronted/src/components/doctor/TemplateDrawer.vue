<template>
    <a-drawer
            title="常用与模板"
            placement="top"
            :closable="false"
            :visible="showDrawer"
            @close="closeDrawer"
    >
        <a-row type="flex" align="top" justify="space-around">
            <a-col span="11">
                <a-table>
                    <a-collapse>
                        <a-collapse-panel header="This is panel header 1" key="1">
                            <p>123</p>
                        </a-collapse-panel>
                    </a-collapse>
                </a-table>
            </a-col>
        </a-row>
    </a-drawer>
</template>

<script>
    export default {
        name: "IndexTemplate",
        props: ['showDrawer'],
        data: () => ({
            mrt: []
        }),
        methods: {
            closeDrawer () {
                this.$emit("changeDrawer", false)
            },
            getMRT () {
                let that = this
                this.$api.get('/MRT/getMRT', null,
                    res => {
                        if (res.code === '100') {
                            that.mrt = res.data
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

<style scoped>

</style>