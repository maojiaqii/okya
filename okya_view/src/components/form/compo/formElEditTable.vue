<template>
  <el-col :span="span">
      <el-divider content-position="left">{{label}}</el-divider>
      <!--表格内容栏-->
      <CusEditTable :permsEdit="properties.permsEdit" :permsDelete="properties.permsDelete"
                    :permsAddOne="properties.permsAddOne" :permsCancelDelete="properties.permsCancelDelete"
                    :showOperation="true" :addModel="addModel" :maxHeight="properties.maxHeight"
                    v-model="pageResult" :columns="properties.editTableColumns" @change="$emit('change', $event)">
      </CusEditTable>
  </el-col>
</template>

<script>

    export default {
        name: "formElEditTable",
        data() {
            return {
                pageResult: this.value,
                refresh: true
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            showOperation: {
                type: Boolean,
                default: true
            },
            addModel: {
                type: Object,
                default: {}
            },
            span: {
                type: Number,
                default: 24
            },
            label: {
                type: String,
                default: ''
            },
            value: {},
            properties: {
                type: Object,
                default: {}
            }
        },
        methods: {
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>
