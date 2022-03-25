<template>
  <el-col :span="span">
    <div v-show="properties.hidden" :style="properties.style">{{label}}</div>
  </el-col>
</template>

<script>
    export default {
        name: "formWord",
        props: {
            span: {
                type: Number,
                default: 24
            },
            label: {
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
