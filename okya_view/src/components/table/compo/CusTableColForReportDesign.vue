<template>
  <el-table-column v-if="column.children && column.children.length > 0" :show-overflow-tooltip="true" :prop="column.prop"
                   :label="column.label" :fixed="column.fixed" :align="column.align ? column.align : 'center'" :index="column.cid">
    <CusTableColForReportDesign v-for="(columnC, index) in column.children" :column="columnC" :key="index"/>
  </el-table-column>
  <el-table-column v-else-if="!column.children || column.children.length == 0" :show-overflow-tooltip="true"
                   :prop="column.prop" :label="column.label" :width="column.width" :min-width="column.minWidth" :fixed="column.fixed"
                   :key="column.prop" :align="column.align ? column.align : 'center'" :index="column.cid">
  </el-table-column>
</template>

<script>
    export default {
        name: "CusTableColForReportDesign",
        props: {
            column: {
                type: Object,
                required: true
            }
        },
        methods: {
        }
    }
</script>

<style scoped>

</style>
