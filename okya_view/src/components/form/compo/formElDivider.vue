<template>
  <el-col :span="span">
    <div>
      <el-divider :content-position="properties.align" v-show="properties.hidden">{{model}}</el-divider>
    </div>
  </el-col>
</template>

<script>
    export default {
        name: "formElDivider",
        props: {
            span: {
                type: Number,
                default: 24
            },
            model: {
                type: String,
                default: ''
            },
            properties: {
                type: Object,
                default: {}
            }
        }
    }
</script>

<style scoped>

</style>
