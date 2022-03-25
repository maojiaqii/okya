<template>
  <el-col :span="span">
    <el-form-item :label="label" :prop="prop" v-show="properties.hidden" :label-width="properties.titleWidth">
      <el-date-picker
        v-model="childValue"
        :type="properties.type"
        :format="properties.format"
        :value-format="properties.format"
        :disabled="properties.disabled"
        style="width: 100%;"
        @change="$emit('change', $event)">
      </el-date-picker>
    </el-form-item>
  </el-col>
</template>

<script>
    export default {
        name: "formElDatePicker",
        data() {
            return {
                childValue: ''
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
                default: '日期选择器'
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
            if(this.value == ''){

            } else {
                this.childValue = new Date(this.value)
            }
        }
    }
</script>

<style scoped>

</style>
