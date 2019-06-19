<template>
    <a-drawer
            title="常用与模板"
            placement="top"
            :closable="false"
            :visible="showDrawer"
            @close="closeDrawer"
    >
        <p>Some contents...</p>
        <p>Some contents...</p>
        <p>Some contents...</p>
    </a-drawer>
</template>

<script>
    export default {
        name: "IndexTemplate",
        props: ['showDrawer'],
        data: () => ({}),
        methods: {
            closeDrawer () {
                this.$emit("changeDrawer", false)
            }
        }
    }
</script>

<style scoped>

</style>