<template>
    <a-row type="flex" align="middle" justify="center" class="info-card">
        <a-col span="20">
            <department></department>
        </a-col>
    </a-row>
</template>

<script>

    import Department from '../../components/admin/Department'

    export default {
        name: "Other",
        components: {
            department: Department
        }

    }
</script>

<style>
    .info-card {
        margin-top: 40px;
        margin-bottom: 20px;
    }

    .editable-row-operations a {
        margin-right: 8px;
    }

    .ant-table {
        font-size: 15px !important;
    }
</style>