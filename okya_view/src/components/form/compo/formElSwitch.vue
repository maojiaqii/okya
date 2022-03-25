<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-switch
        v-model="childValue"
        :disabled="properties.disabled"
        active-color="#13ce66"
        inactive-color="#ff4949"
        :active-text="properties.activeText"
        :inactive-text="properties.inActiveText"
        :active-value="properties.activeValue"
        :inactive-value="properties.inActiveValue"
        style="width: 100%;"
        @change="$emit('change', $event)">
      </el-switch>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElSwitch",
        data() {
            return {
                childValue: this.value
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            rule: {
                type: Array,
                default: () => []
            },
            span: {
                type: Number,
                default: 24
            },
            label: {
                type: String,
                default: '开关'
            },
            prop: {
                type: String,
                default: ''
            },
            value: {
                type: String,
                default: ''
            },
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
